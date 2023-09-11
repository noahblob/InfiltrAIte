package nz.ac.auckland.se206.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;

public class StartController {

  @FXML private Button accept;
  @FXML private Button decline;
  @FXML private TextArea dialogue;

  private final String context =
      "Greetings, Agent. Your mission, should you choose to accept it, involves infiltrating"
          + " hostile territory.\n\n"
          + " The objective: retrieve vital intelligence critical to national"
          + " security in a high-stakes espionage mission. You'll need to utilize your skillset in"
          + " problem solving to complete the mission.\n"
          + "Remember, should you be caught or killed, the agency will disavow any knowledge of"
          + " your actions. Good luck, Agent.";

  public void initialize() {
    initialiseDialogue();
    Platform.runLater(() -> textRollout(context));
  }

  @FXML
  private void onClick(MouseEvent event) {

    Button whichButton = (Button) event.getSource();

    if (whichButton.getId().equals("accept")) {
      Scene currentScene = whichButton.getScene();
      currentScene.setRoot(SceneManager.getuserInterface(AppUI.TITLE));
    } else {
      System.exit(0);
    }
  }

  private void initialiseDialogue() {
    dialogue.setEditable(false);
  }

  // Method to generate backstory.
  private void textRollout(String message) {

    String[] words = message.split(" ");
    Timeline timeline = new Timeline();
    Duration timepoint = Duration.ZERO;

    for (String word : words) {
      timepoint = timepoint.add(Duration.millis(100));
      final String finalWord = word; // Make a final local copy of the word
      KeyFrame keyFrame =
          new KeyFrame(
              timepoint,
              e -> {
                dialogue.appendText(finalWord + " ");
              });
      timeline.getKeyFrames().add(keyFrame);
    }
    timeline.play();
  }
}
