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
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;

public class StartController {

  @FXML private Button accept;
  @FXML private Button decline;
  @FXML private TextArea dialogue;

  public void initialize() {
    initialiseDialogue();
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
    Platform.runLater(() -> textRollout(Dialogue.BACKSTORY.toString()));
  }

  // Method to generate backstory.
  private void textRollout(String message) {

    char[] chars = message.toCharArray();
    Timeline timeline = new Timeline();
    Duration timepoint = Duration.ZERO;

    for (char ch : chars) {
      timepoint = timepoint.add(Duration.millis(12));
      final char finalChar = ch; // Make a final local copy of the character
      KeyFrame keyFrame =
          new KeyFrame(
              timepoint,
              e -> {
                dialogue.appendText(String.valueOf(finalChar));
              });
      timeline.getKeyFrames().add(keyFrame);
    }
    timeline.play();
  }
}
