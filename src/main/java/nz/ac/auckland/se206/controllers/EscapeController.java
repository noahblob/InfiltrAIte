package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.TextRollout;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

/** Class to store all functionality relevant to the escape/end screen. */
public class EscapeController extends TextRollout {

  @FXML private Button exit;
  @FXML private Button playAgain;
  @FXML private Pane winPane;
  @FXML private Pane losePane;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    // If we are on the win screen.
    setWinScreen();
    lose(true);
  }

  /**
   * Handles the event of clicking accept or decline button on the end screen.
   *
   * @param event the mouse event
   * @throws Exception if there is an error loading the chat view
   */
  @FXML
  private void onClick(MouseEvent event) throws Exception {
    Sound.getInstance().playClickMajor();
    Sound.getInstance().stopRollout();
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

  /** Plays a sound upon hovering over the buttons on screen. */
  @FXML
  private void onHover() {
    Sound.getInstance().playClickMinor();
  }

  /** Set the win screen to be visible if the user has won. */
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

  /**
   * Set the lose screen to be visible if the user has lost.
   *
   * @param flag true if the user has lost, false otherwise
   */
  private void lose(boolean flag) {
    winPane.setVisible(!flag);
    losePane.setVisible(flag);
  }

  /** Displays the dialogue for the end screen. */
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
