package nz.ac.auckland.se206.controllers;

import java.util.List;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.TextRollout;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.right.BlackBoardController;
import nz.ac.auckland.se206.controllers.right.BookController;
import nz.ac.auckland.se206.controllers.right.LockerController;
import nz.ac.auckland.se206.gpt.ChatMessage;

public class EscapeController extends TextRollout {

  @FXML private TextArea dialogue;
  @FXML private Button exit;
  @FXML private Button playAgain;
  @FXML private Pane winPane;
  @FXML private Pane losePane;

  public void initialize() {

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

    // Bind the win lose pane to the gamstate (IsgameWon)
    winPane.visibleProperty().bind(GameState.isGameWon);
    losePane.visibleProperty().bind(Bindings.not(GameState.isGameWon));
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

    // Clear the notes and phones of previous game.
    CommanderController.getInstance().clearNotes();
    CommanderController.getInstance().clearPhones();

    List<TextArea> dialogues = CommanderController.getInstance().getDialogues();
    List<ListView<ChatMessage>> phoneScreens = CommanderController.getInstance().getPhoneScreens();
    // Reset the game master.
    CommanderController.resetInstance();

    // Update the new Commander controller with the list of dialogues and phonescreens.
    CommanderController.getInstance().setDialogues(dialogues);
    CommanderController.getInstance().setPhoneScreens(phoneScreens);

    // get the list of timers before resetting.
    List<Text> timers = TimerClass.getTimers();

    // Reset BlackBoard Numbers
    BlackBoardController.getInstance().refreshBoard();

    // Reset Book order in bookshelf
    BookController.getInstance().resetFont();
    BookController.getInstance().setupContent();

    // Reset Right Room Locker
    LockerController.getInstance().resetRoom();

    // Reset the timer.
    TimerClass.resetInstance();
    // Create a new Timer.
    TimerClass.initialize();
    // Upate new Timer with old rooms.
    TimerClass.setTimers(timers);
  }
}
