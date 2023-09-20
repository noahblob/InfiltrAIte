package nz.ac.auckland.se206;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.controllers.CommanderController;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.ChatMessage;

/** Abstract class representing the commander */
public abstract class Commander implements TimerObserver {

  // Relevant FXML elements accessed across all scenes by commander
  @FXML protected TextArea objective;
  @FXML protected TextArea input;
  @FXML protected ListView<ChatMessage> output;
  @FXML protected Button send;
  @FXML protected TextArea dialogue;
  @FXML protected TextArea notes;
  @FXML protected Text timer;
  @FXML protected Label intel;
  @FXML protected Label hints;
  @FXML protected ImageView key;

  /**
   * Initializes the commander for each room and stores all relevant information to be passed
   * through each room.
   *
   * @throws Exception if the commander fails to initialize
   */
  @FXML
  protected void initialize() throws Exception {

    // Add the relevant FXML elements to the commander controller
    CommanderController.getInstance().addListView(output);
    CommanderController.getInstance().addDialogueBox(dialogue);
    objective.setEditable(false);
    output.setFixedCellSize(-1);

    // Bind key, intel, notes, input and output elements to commander controller to be passed
    // through rooms
    key.visibleProperty().bind(GameState.doePlayerHaveKey);

    intel.textProperty().bind(Bindings.concat("x", GameState.numOfIntel.asString()));
    if (notes == null) {
      notes = new TextArea();
    }
    notes.textProperty().bindBidirectional(CommanderController.getInstance().notesProperty());
    input
        .textProperty()
        .bindBidirectional(CommanderController.getInstance().lastInputTextProperty());

    output.scrollTo(output.getItems().size() - 1);

    GameState.difficulty.addListener(
        (observable, oldValue, newValue) -> {
          setupHints();
        });

    // Set dialogue box to be uneditable and wrap text
    if (dialogue != null) {
      dialogue.setEditable(false);
      dialogue.setWrapText(true);
    }
    updateTimerFont();

    notes.setPromptText("NOTEPAD!! Write your notes here!!");
    TimerClass.add(this);
  }

  /** Updates styling for timer to correct font and size upon game launch. */
  protected void updateTimerFont() {
    Font.loadFont(getClass().getResourceAsStream("/fonts/DS-DIGI.TTF"), 20);
    timer.setStyle("-fx-font-family: 'DS-Digital'; -fx-font-size: 30px; -fx-text-fill: black;");
  }

  /**
   * Handles the event of user clicking send message button to commander.
   *
   * @param event The mouse event
   * @throws Exception if the message fails to send
   */
  @FXML
  protected void onSendMessage(MouseEvent event) throws Exception {
    // Send the message to the commander controller.
    CommanderController.getInstance().onSendMessage(event, input);
  }

  @Override
  public void timerUpdated() {
    TimerClass timerText = TimerClass.getInstance();
    timer.setText(timerText.getTimerLeft());
  }

  @Override
  public void timerFinished() {
    Platform.runLater(
        () -> {
          Scene currentScene = (Scene) SceneManager.getCurrentSceneRoot().getScene();
          currentScene.setRoot(SceneManager.getuserInterface(AppUi.ESCAPE));
        });
  }

  /**
   * Handles the event of user pressing enter to send message to commander.
   *
   * @param event The key event
   * @throws Exception if the message fails to send
   */
  @FXML
  protected void onEnterPressed(KeyEvent event) throws Exception {
    // if the user tries to send message using enter, send message to commander controller
    if (event.getCode() == KeyCode.ENTER) {
      CommanderController.getInstance().onSendMessage(event, input);
    }
  }

  // Method to update the commander's dialogue box.
  protected void updateDialogue(Dialogue dialogue) throws Exception {
    String msg = dialogue.toString();
    CommanderController.getInstance().updateDialogueBox(msg);
  }

  public void setupHints() {
    // Depending on difficulty setting of the game, show how many hints the user has
    switch (GameState.difficulty.get()) {
      case 1:
        // infinite for easy
        hints.setText("\u221E");
        break;
      case 2:
        // 5 for medium
        hints.textProperty().bind(Bindings.concat("x", GameState.numHints.asString()));
        break;
      case 3:
        // none for hard
        hints.setText("x0");
        break;
      default:
        break;
    }
  }
}
