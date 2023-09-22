package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.controllers.CommanderController;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class TimerClass {

  private static volatile TimerClass instance;
  private static List<Text> timers = new ArrayList<>();

  public static List<Text> getTimers() {
    return timers;
  }

  public static void setTimers(List<Text> timers) {
    TimerClass.timers = timers;
  }

  public static void resetInstance() {
    instance = null;
    System.gc();
  }

  public static void add(Text timer) {
    timers.add(timer);
  }

  public static void initialize() {
    if (instance == null) {
      instance = new TimerClass();
    } else {
      System.out.println("Timer Exists");
    }
  }

  public static TimerClass getInstance() {
    if (instance == null) {
      System.out.println("NO TIMER EXISTS");
    }
    return instance;
  }

  private TextToSpeech tts = new TextToSpeech();
  private int timeLeft;
  private Timeline timeline;

  private TimerClass() {}

  public void start(int minutes) {
    this.timeLeft = minutes * 60;
    this.timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    KeyFrame frame = new KeyFrame(Duration.seconds(1), e -> {
      try {
        timerAction();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    timeline.getKeyFrames().add(frame);
    timeline.play();
  }

  public void stop() {
    for (Text timer : timers) {
      timer.setText("00:00");
    }
    timeline.stop();
  }

  private void timerAction() throws IOException {
    timeLeft--;
    for (Text timer : timers) {
      String time = getTimerLeft();
      timer.setText(String.valueOf(time));
    }

    if (timeLeft == 30) {
      try {
        CommanderController.getInstance().updateDialogueBox(Dialogue.INTRUDERDETECED.toString());
        tts.speak("ENEMY DETECTED IN OUR BASE!! ENEMY DETECTED IN OUR BASE!!");
        // Delete the thread straight after.
        tts.terminate();
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (timeLeft == 0) {
        timeline.stop();
        // Switch to lose screen.
        Scene currentScene = SceneManager.getCurrentSceneRoot().getScene();
        SceneManager.addUserInterface(AppUi.END, App.loadFxml("escape"));
        currentScene.setRoot(SceneManager.getuserInterface(AppUi.END));
      }
    }
  }

  public String getTimerLeft() {
    int minutes = timeLeft / 60;
    int seconds = timeLeft % 60;
    return String.format("%02d:%02d", minutes, seconds);
  }

  // Method to add each rooms timer label to the timer.
  public void addTimer(Text timer) {
    timers.add(timer);
  }
}
