package nz.ac.auckland.se206;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.controllers.CommanderController;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public abstract class Commander {

  @FXML protected TextArea input;
  @FXML protected TextArea output;
  @FXML protected Button send;
  @FXML protected TextArea dialogue;
  @FXML protected Text timer;

  @FXML
  protected void initialize() throws ApiProxyException {

    CommanderController.getInstance().addTextArea(output);
    CommanderController.getInstance().addDialogueBox(dialogue);

    if (output != null) {
      output.setWrapText(true);
      output.setEditable(false);
    }
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
}
