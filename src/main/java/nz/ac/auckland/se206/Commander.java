package nz.ac.auckland.se206;

import javafx.beans.binding.Bindings;
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

public abstract class Commander {

  @FXML protected TextArea input;
  @FXML protected ListView<ChatMessage> output;
  @FXML protected Button send;
  @FXML protected TextArea dialogue;
  @FXML protected TextArea notes;
  @FXML protected Text timer;
  @FXML protected Label intel;
  @FXML protected ImageView key;

  @FXML
  protected void initialize() throws Exception {

    CommanderController.getInstance().addListView(output);
    CommanderController.getInstance().addDialogueBox(dialogue);

    key.visibleProperty().bind(GameState.isKeyFound);
    intel.textProperty().bind(Bindings.concat("x", GameState.numOfIntel.asString()));
    if (notes == null) {
      notes = new TextArea();
    }
    notes.textProperty().bindBidirectional(CommanderController.getInstance().notesProperty());
    input
        .textProperty()
        .bindBidirectional(CommanderController.getInstance().lastInputTextProperty());

    output.scrollTo(output.getItems().size() - 1);

    if (dialogue != null) {
      dialogue.setEditable(false);
      dialogue.setWrapText(true);
    }
    updateTimerFont();
  }

  protected void updateTimerFont() {
    Font.loadFont(getClass().getResourceAsStream("/fonts/DS-DIGI.TTF"), 20);
    timer.setStyle("-fx-font-family: 'DS-Digital'; -fx-font-size: 30px; -fx-text-fill: black;");
  }

  @FXML
  protected void onSendMessage(MouseEvent event) throws Exception {
    // Send the message to the commander controller.
    CommanderController.getInstance().onSendMessage(event, input);
  }

  @FXML
  protected void onEnterPressed(KeyEvent event) throws Exception {
    // if the user tries to send message using enter, send message to commander controller
    if (event.getCode().toString().equals("ENTER")) {
      CommanderController.getInstance().onSendMessage(event, input);
    }
  }
}
