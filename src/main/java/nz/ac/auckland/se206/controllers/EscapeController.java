package nz.ac.auckland.se206.controllers;

import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.TextRollout;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.ChatMessage;

public class EscapeController extends TextRollout {

  @FXML private TextArea dialogue;
  @FXML private Button exit;
  @FXML private Button playAgain;
  @FXML private Pane winPane;
  @FXML private Pane losePane;

  public void initialize() {

    System.out.println(GameState.isGameWon.get());

    if (GameState.isGameWon.get()) {
      winPane.setVisible(true);
      losePane.setVisible(false);
      // update the dialogue area with final message from commander. (UPDATE LATER BASED ON IF ALL 3
      // Intel COLLECTED)
      Platform.runLater(
          () -> {
            if (GameState.numOfIntel.get() == 1) {
              textRollout(Dialogue.WINDIALOGUE1);
            } else if (GameState.numOfIntel.get() == 2) {
              textRollout(Dialogue.WINDIALOGUE2);
            } else if (GameState.numOfIntel.get() == 3) {
              textRollout(Dialogue.WINDIALOGUE3);
            }
          });
    } else {
      winPane.setVisible(false);
      losePane.setVisible(true);
    }
  }

  @FXML
  public void onClick(MouseEvent event) throws Exception {
    // Get the button that was clicked to check against some conditionals
    Button button = (Button) event.getSource();
    if (button == playAgain) {
      // Reset game variables and GPT
      reset();
      // Swich to difficulty select screen.
      Scene currentScene = (Scene) SceneManager.getCurrentSceneRoot().getScene();
      currentScene.setRoot(SceneManager.getuserInterface(AppUi.TITLE));
    } else {
      // in the case user wishes to exit the game upon losing or winning
      System.exit(0);
    }
  }

  private void reset() throws Exception {
 
    // In the case user wants to retry the game upon winning or losing, reset the game state.
    GameState.resetGameState();

    // Save phone and dialogue boxes from previous commander controller.
    List<ListView<ChatMessage>> phoneScreens = CommanderController.getInstance().getPhoneScreens();
    List<TextArea> dialogues = CommanderController.getInstance().getDialogues();
    // Reset the commander (GPT) and the timer.
    CommanderController.resetInstance();
    // Create new commander controller give all the phoneScreens and dialogue boxes to new commander.
    CommanderController.getInstance().setPhoneScreens(phoneScreens);
    CommanderController.getInstance().setDialogues(dialogues);
    // Clear all the phone screens.
    CommanderController.getInstance().clearPhones();
    
    // Reset the timer.
    TimerClass.resetInstance();

  }
}
