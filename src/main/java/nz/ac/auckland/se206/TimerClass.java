package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import nz.ac.auckland.se206.controllers.CommanderController;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** Represents the timer for the game. */
public class TimerClass {

  // The timer instance, this is the only instance of the timer that should exist.
  private static TimerClass instance;
  // Observer List which contains the classes that should show the timer
  private static List<TimerObserver> observe = new ArrayList<>();

  /**
   * Adds an observer to the observer list.
   *
   * @param observer The observer to add
   */
  public static void add(TimerObserver observer) {
    observe.add(observer);
  }

  /**
   * Initalize the timer with a time.
   *
   * @param time The time in seconds to count down from
   */
  public static void initialize(int time) {
    // Ensures that only one instance can be made
    if (instance == null) {
      instance = new TimerClass(time);
      for (TimerObserver observer : observe) {
        observer.timerUpdated();
      }
    } else {
      throw new IllegalStateException("There already is a timer instance!!");
    }
  }

  // Returns this timer instance
  public static TimerClass getInstance() {
    if (instance == null) {
      throw new IllegalStateException("No timer exists");
    }
    return instance;
  }

  private Runnable finished;
  private TextToSpeech tts = new TextToSpeech();
  private int timeLeft;
  private boolean shouldRun = false;
  private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  public void setFinished(Runnable call) {
    this.finished = call;
  }
  
  /**
   * Constructor is private hence a new instance cannot be made with 'new TimerClass' Just a
   * mechanism to make sure that the TimerClass cant be made by unwanted code.
   *
   * @param minutes The time in minutes to count down from
   */
  private TimerClass(int minutes) {
    this.timeLeft = minutes * 60 + 1;
  }

  /** Starts or resumes the timer from counting. */
  public void start() {
    shouldRun = true;
    scheduler.scheduleWithFixedDelay(
        new Runnable() {
          @Override
          public void run() {

            if (shouldRun) {
              timeLeft--;

              for (TimerObserver observer : observe) {
                observer.timerUpdated();
              }

              if (timeLeft == 30) {
                Platform.runLater(
                    new Runnable() {
                      @Override
                      public void run() {
                        try {
                          CommanderController.getInstance()
                              .updateDialogueBox(Dialogue.INTRUDERDETECED.toString());

                          new Thread(
                                  new Runnable() {
                                    @Override
                                    public void run() {
                                      tts.speak(
                                          "ENEMY DETECTED IN OUR BASE!! ENEMY DETECTED IN OUR"
                                              + " BASE!!");
                                    }
                                  })
                              .start();

                        } catch (Exception e) {
                          e.printStackTrace();
                        }
                      }
                    });
              }

              if (timeLeft == 0) {
                shouldRun = false;

                observe.get(0).timerFinished();

                if (finished != null) {
                  Platform.runLater(finished);
                }
              }
            }
          }
        },
        0,
        1,
        TimeUnit.SECONDS);
  }

  /** Pauses the timer from counting and sets the shouldRun to false. */
  public void pause() {
    shouldRun = false;
    scheduler.shutdown();
    scheduler = Executors.newScheduledThreadPool(1);
  }

  /**
   * Getter method to get time remaining on clock in minutes and seconds.
   *
   * @return String of time remaining on clock in minutes and seconds
   */
  public String getTimerLeft() {
    int minutes = timeLeft / 60;
    int seconds = timeLeft % 60;
    return String.format("%02d:%02d", minutes, seconds);
  }

  /** Getter method to get time remaining on clock in seconds. */
  public int getTimeLeftInt() {
    return timeLeft;
  }
}
