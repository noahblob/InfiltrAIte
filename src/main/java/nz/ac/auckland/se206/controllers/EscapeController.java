package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.TextRollout;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

public class EscapeController extends TextRollout {

  @FXML private TextArea dialogue;
  @FXML private Button exit;
  @FXML private Button playAgain;
  @FXML private Pane winPane;
  @FXML private Pane losePane;

  public void initialize() {
    // If we are on the win screen.
    setWinScreen();
    lose(true);
  }

  @FXML
  private void onClick(MouseEvent event) throws Exception {
    Sound.getInstance().playClickMajor();
    // Get the button that was clicked to check against some conditionals
    Button button = (Button) event.getSource();
    if (button == playAgain) {
      GameState.resetGame();

      // Update game screen to lose screen by default.
      lose(true);

      // Swich to difficulty select screen.
      Scene currentScene = (Scene) SceneManager.getCurrentSceneRoot().getScene();
      currentScene.setRoot(SceneManager.getuserInterface(AppUi.TITLE));
    } else {
      System.exit(0);
    }
  }

  @FXML
  private void onHover() {
    Sound.getInstance().playClickMinor();
  }

  private void setWinScreen() {
    // If the user has won, set the win screen based on how much intel the user has found
    GameState.isEndScreen.addListener(
        (observable, oldValue, newValue) -> {
          if (newValue) {
            // set the win Pane to be visible in the case of a win
            lose(false);
            Platform.runLater(
                () -> {
                  showDialogue();
                });
          }
        });
  }

  private void lose(boolean flag) {
    winPane.setVisible(!flag);
    losePane.setVisible(flag);
  }

  private void showDialogue() {
    dialogue.clear();
    // change commander dialogue based on number of intel user has found
    Dialogue msg =
        (GameState.numOfIntel.get() == 1)
            ? Dialogue.WIN1
            : (GameState.numOfIntel.get() == 2) ? Dialogue.WIN2 : Dialogue.WIN3;
    textRollout(msg);
  }
}
