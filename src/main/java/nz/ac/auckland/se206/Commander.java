package nz.ac.auckland.se206;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.controllers.CommanderController;
import nz.ac.auckland.se206.gpt.ChatMessage;

/** Abstract class representing the commander */
public abstract class Commander {

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
  @FXML protected ImageView soundButton;

  @FXML protected AnchorPane roomUi;

  /**
   * Initializes the commander for each room and stores all relevant information to be passed
   * through each room.
   *
   * @throws Exception if the commander fails to initialize
   */
  @FXML
  protected void initialize() throws Exception {

    TimerClass.getInstance().addTimer(timer);
    updateCommander();
    setUpUI();
    setUpHints();
    updateTimerFont();
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

  /**
   * Handles when the sound/volume button is pressed to mute and/or unmute the tts in the game.
   *
   * @param event
   */
  @FXML
  protected void onMute(MouseEvent event) {
    ImageView image = (ImageView) event.getSource();
    // if the game is muted, change image to muted image and disable tts
    if (!GameState.isMuted) {
      GameState.isMuted = true;
      image.setImage(new Image("/images/icons8-mute-100-red.png"));
    } else {
      GameState.isMuted = false;
      image.setImage(new Image("/images/icons8-volume-100-green.png"));
    }
  }

  // Method to update the commander's dialogue box.
  protected void updateDialogue(Dialogue dialogue) throws Exception {
    String msg = dialogue.toString();
    CommanderController.getInstance().updateDialogueBox(msg);
  }

  /** Updates styling for timer to correct font and size upon game launch. */
  private void updateTimerFont() {
    Font.loadFont(getClass().getResourceAsStream("/fonts/DS-DIGI.TTF"), 20);
    timer.setStyle("-fx-font-family: 'DS-Digital'; -fx-font-size: 30px; -fx-text-fill: black;");
  }

  private void setupHints() {
    unbindHints();
    setHintsBasedOnDifficulty();
  }

  private void unbindHints() {
    hints.textProperty().unbind();
  }

  private void setHintsBasedOnDifficulty() {
    int difficulty = GameState.difficulty.get();
    switch (difficulty) {
      case 1:
        setInfiniteHints();
        break;
      case 2:
        setMediumHints();
        break;
      case 3:
        setNoHints();
        break;
      default:
        break;
    }
  }

  private void setInfiniteHints() {
    hints.setText("\u221E");
  }

  private void setMediumHints() {
    hints.textProperty().bind(Bindings.concat("x", GameState.numHints.asString()));
  }

  private void setNoHints() {
    hints.setText("x0");
  }

  private void setUpHints() {
    GameState.difficulty.addListener(
        (observable, oldValue, newValue) -> {
          setupHints();
        });
  }

  private void setUpUI() throws Exception {
    // Bind key, intel, notes, input and output elements to commander controller to be passed
    // through rooms
    BooleanBinding keyVisibilityBinding =
        Bindings.and(GameState.isKeyFound, Bindings.not(GameState.isKeyUsed));
    key.visibleProperty().bind(keyVisibilityBinding);

    intel.textProperty().bind(Bindings.concat("x", GameState.numOfIntel.asString()));
    notes.textProperty().bindBidirectional(CommanderController.getInstance().notesProperty());
    input
        .textProperty()
        .bindBidirectional(CommanderController.getInstance().lastInputTextProperty());

    output.scrollTo(output.getItems().size() - 1);

    objective.setEditable(false);
    objective.setWrapText(true);
    dialogue.setEditable(false);
    dialogue.setWrapText(true);
    output.setFixedCellSize(-1);
    notes.setWrapText(true);
    notes.setPromptText("NOTEPAD: Write your findings");
  }

  private void updateCommander() throws Exception {
    // Add the relevant FXML elements to the commander controller
    CommanderController.getInstance().addListView(output);
    CommanderController.getInstance().addDialogueBox(dialogue);
    CommanderController.getInstance().addNotes(notes);
  }
}
