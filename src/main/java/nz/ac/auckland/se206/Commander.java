package nz.ac.auckland.se206;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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

    // Bind key, intel, notes, input and output elements to commander controller to be passed
    // through rooms
    key.visibleProperty().bind(GameState.isKeyFound);

    // Bind the hints text property to the number of hints, based on the difficulty chosen.
    hints.textProperty().bind(Bindings.concat("x", GameState.numHints.asString()));
    intel.textProperty().bind(Bindings.concat("x", GameState.numOfIntel.asString()));
    if (notes == null) {
      notes = new TextArea();
    }
    notes.textProperty().bindBidirectional(CommanderController.getInstance().notesProperty());
    input
        .textProperty()
        .bindBidirectional(CommanderController.getInstance().lastInputTextProperty());

    output.scrollTo(output.getItems().size() - 1);

    // Set dialogue box to be uneditable and wrap text
    if (dialogue != null) {
      dialogue.setEditable(false);
      dialogue.setWrapText(true);
    }
    updateTimerFont();

    notes.setPromptText("NOTEPAD!! Write your notes here!!");
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

  /**
   * Handles the event of user pressing enter to send message to commander.
   *
   * @param event The key event
   * @throws Exception if the message fails to send
   */
  @FXML
  protected void onEnterPressed(KeyEvent event) throws Exception {
    // if the user tries to send message using enter, send message to commander controller
    if (event.getCode().toString().equals("ENTER")) {
      CommanderController.getInstance().onSendMessage(event, input);
    }
  }
}
