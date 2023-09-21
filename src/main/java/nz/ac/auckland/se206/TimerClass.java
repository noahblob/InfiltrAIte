package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import nz.ac.auckland.se206.controllers.CommanderController;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class TimerClass {

  private static volatile TimerClass instance;
  private static List<TimerObserver> observe = new ArrayList<>();

  public static void resetInstance() {
    instance = null;
  }

  public static void add(TimerObserver observer) {
    observe.add(observer);
  }

  public static void initialize(int time) {
    if (instance == null) {
      instance = new TimerClass(time);
      for (TimerObserver observer : observe) {
        observer.timerUpdated();
      }
    } else {
      throw new IllegalStateException("There already is a timer instance!!");
    }
  }

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
  private Timeline timeline;

  private TimerClass(int minutes) {
    this.timeLeft = minutes * 60 + 1;
    this.timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    KeyFrame frame = new KeyFrame(Duration.seconds(1), e -> timerAction());
    timeline.getKeyFrames().add(frame);
  }

  public void setFinished(Runnable call) {
    this.finished = call;
  }

  public void start() {
    shouldRun = true;
    timeline.play();
  }

  public void pause() {
    shouldRun = false;
    timeline.pause();
  }

  private void timerAction() {
    if (shouldRun) {
      timeLeft--;

      for (TimerObserver observer : observe) {
        observer.timerUpdated();
      }

      if (timeLeft == 30) {
        Platform.runLater(
            () -> {
              try {
                CommanderController.getInstance()
                    .updateDialogueBox(Dialogue.INTRUDERDETECED.toString());

                new Thread(
                        () ->
                            tts.speak("ENEMY DETECTED IN OUR BASE!! ENEMY DETECTED IN OUR BASE!!"))
                    .start();

              } catch (Exception e) {
                e.printStackTrace();
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

  public String getTimerLeft() {
    int minutes = timeLeft / 60;
    int seconds = timeLeft % 60;
    return String.format("%02d:%02d", minutes, seconds);
  }

  public int getTimeLeftInt() {
    return timeLeft;
  }
}
