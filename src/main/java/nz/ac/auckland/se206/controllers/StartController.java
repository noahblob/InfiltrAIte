package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.TextRollout;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;

public class StartController extends TextRollout {

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
}
