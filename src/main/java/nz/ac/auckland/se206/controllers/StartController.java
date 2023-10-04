package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.TextRollout;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

public class StartController extends TextRollout {

  @FXML private Button accept;
  @FXML private Button decline;
  @FXML private TextArea dialogue;

  public void initialize() {

    // Initialise the timer.
    TimerClass.initialize();

    initialiseDialogue();
  }

  @FXML
  private void onClick(MouseEvent event) {

    // Play sound effect.
    Sound.getInstance().playClickMajor();

    // Get source of the click event to check which button was clicked
    Button whichButton = (Button) event.getSource();

    // If user accepts the mission, move on to the game otherwise close
    if (whichButton.getId().equals("accept")) {
      Scene currentScene = whichButton.getScene();
      currentScene.setRoot(SceneManager.getuserInterface(AppUi.TITLE));
      // Stop the media player.
      Sound.getInstance().stopRollout();
    } else {
      System.exit(0);
    }
  }

  @FXML
  private void onHover(MouseEvent event) {
    Sound.getInstance().playHover();
  }

  private void initialiseDialogue() {
    dialogue.setEditable(false);
    Platform.runLater(() -> textRollout(Dialogue.BACKSTORY));
  }
}
