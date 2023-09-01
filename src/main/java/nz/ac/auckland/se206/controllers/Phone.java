package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public abstract class Phone {

  @FXML protected TextArea input;
  @FXML protected TextArea output;
  @FXML protected Button send;

  @FXML
  protected void initialize() throws ApiProxyException {
    CommanderController.getInstance().addTextArea(output);
    if (output != null) {
      output.setWrapText(true);
      output.setEditable(false);
    }
  }

  @FXML 
  protected void onSendMessage(MouseEvent event) throws ApiProxyException, IOException {
    // Send the message to the commander controller.
    CommanderController.getInstance().onSendMessage(event,input);
  }
  
}
