package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.controllers.CommanderController;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

/** Class to store and manage the timer in the applicaiton. */
public class TimerClass {

  private static volatile TimerClass instance;
  private static List<Text> timers = new ArrayList<>();

  /**
   * Get list of all currently existing timers.
   *
   * @return list of all currently existing timers.
   */
  public static List<Text> getTimers() {
    return timers;
  }

  /**
   * Set timer list to a new list of timers.
   *
   * @param timers the new list of timers.
   */
  public static void setTimers(List<Text> timers) {
    TimerClass.timers = timers;
  }

  /** Reset timerclass instance to null upon user replaying. */
  public static void resetInstance() {
    instance = null;
    System.gc();
  }

  /** Add a new timer in newly loaded room. */
  public static void add(Text timer) {
    timers.add(timer);
  }

  /** Method to initialize the timer, creating a new instance to be used across all scenes. */
  public static void initialize() {
    if (instance == null) {
      instance = new TimerClass();
    } else {
      System.out.println("Timer Exists");
    }
  }

  /**
   * Method to get the instance of the timer to use across scenes.
   *
   * @return the instance of the timer.
   */
  public static TimerClass getInstance() {
    if (instance == null) {
      System.out.println("NO TIMER EXISTS");
    }
    return instance;
  }

  private int timeLeft;
  private Timeline timeline;

  /** Default constructor for timer class. */
  private TimerClass() {}

  /**
   * Start the timer with given number of minutes decided by user.
   *
   * @param minutes the number of minutes to start the timer with.
   */
  public void start(int minutes) {
    // Set the correct amount of time for timer and start counting
    this.timeLeft = minutes * 60;
    for (Text timer : timers) {
      timer.setText(String.format("0%d:00", minutes));
    }

    this.timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    // Create a keyframe to decrease the timer every second.
    KeyFrame frame =
        new KeyFrame(
            Duration.seconds(1),
            e -> {
              try {
                decreaseTimer();
              } catch (Exception e1) {

                e1.printStackTrace();
              }
            });
    timeline.getKeyFrames().add(frame);
    timeline.play();
  }

  /** Stop the timer and set label for timer to 00:00. */
  public void stop() {
    for (Text timer : timers) {
      timer.setText("00:00");
    }
    timeline.stop();
  }

  /**
   * Decrease timer by 1 second and update the label for the timer.
   *
   * @throws Exception if the timer fails to decrease.
   */
  private void decreaseTimer() throws Exception {
    // Decrement the timer and set the text of label to the new value.
    timeLeft--;
    for (Text timer : timers) {
      String time = getTimeLeft();
      timer.setText(String.valueOf(time));
    }
    // Warn the player if their time is running out.
    chatToPlayer(timeLeft);
    warn();
    checkLost();
  }

  /**
   * Gets current time left on clock for events related to time.
   *
   * @return the time left on the clock.
   */
  public String getTimeLeft() {
    int minutes = timeLeft / 60;
    int seconds = timeLeft % 60;
    return String.format("%02d:%02d", minutes, seconds);
  }

  /**
   * Method to add each rooms timer label to the timer.
   *
   * @param timer the timer to add.
   */
  public void addTimer(Text timer) {
    timers.add(timer);
  }

  /**
   * Method to check if the player has lost the game, in which the user will be taken to the failed
   * screen.
   */
  private void checkLost() {
    if (timeLeft == 0) {
      timeline.stop();
      // Switch to lose screen.
      Scene currentScene = SceneManager.getCurrentSceneRoot().getScene();
      currentScene.setRoot(SceneManager.getuserInterface(AppUi.END));
    }
  }

  /** Method to warn player when they have 30 seconds left. */
  public void warn() {
    if (timeLeft == 30) {
      Task<Void> warn =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              try {
                // update commander dialogue to let player know they have been detected
                CommanderController.getInstance()
                    .updateDialogueBox(Dialogue.INTRUDERDETECED.toString());
                Sound.getInstance().playTextToSpeech();
              } catch (Exception e) {
                e.printStackTrace();
              }
              return null;
            }
          };
      // This will schedule the task to be run on a background thread
      new Thread(warn).start();
    }
  }

  /**
   * Method to chat to player at certain times and create AI presence.
   *
   * @param timeLeft the time left in the game.
   * @throws Exception if the chat fails.
   */
  public void chatToPlayer(int timeLeft) throws Exception {

    String hint = null;

    // Determine the game state to get hints
    if (!GameState.isPasswordSolved && timeLeft == 60) {
      // Prompt the player abount solving the computer.
      hint = GptPromptEngineering.getComputerHint();
    } else if (GameState.isPasswordSolved && timeLeft == 60) {
      hint = GptPromptEngineering.getEscapeHint();
    } else if (GameState.isKeyFound.get()
        && !GameState.isKeyUsed.get()
        && timeLeft == 80) { // Give hint about the key if they have it.
      hint = GptPromptEngineering.getKeyHint();
    } else if (timeLeft == 80) { // Give player hint to focus on intel.
      hint = GptPromptEngineering.getIntelHint();
    }

    // Only Update GPT at certain times.
    if (timeLeft == 60 || timeLeft == 80) {
      CommanderController.getInstance().sendForUser(hint);
    }
  }
}
