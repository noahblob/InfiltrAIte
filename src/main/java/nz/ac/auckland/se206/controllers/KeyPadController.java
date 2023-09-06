package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class KeyPadController extends Commander implements TimerObserver {
  @FXML private Text timer;
  @FXML private Button clearButton;
  @FXML private Button submitButton;
  @FXML private Button exitButton;
  @FXML private Label objectiveMiddle;
  @FXML private Label numberLabel;
  @FXML private TextArea dialogue;
  @FXML private Rectangle one, two, three, four, five, six, seven, eight, nine, zero;

  public void initialize() throws ApiProxyException {
    super.initialize();
    objectiveMiddle.setText("Figure out the combination!");
    numberLabel.setText("");
    TimerClass.add(this);
  }

  @Override
  public void timerStart() {
    TimerClass timerText = TimerClass.getInstance();
    timer.setText(timerText.getTimerLeft());
  }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("key " + event.getCode() + " pressed");
  }

  @FXML
  public void onClick(MouseEvent event) throws ApiProxyException {
    Button button = (Button) event.getSource();
    Scene currentScene = button.getScene();
    switch (button.getId()) {
      case "clearButton":
        numberLabel.setText("");
        break;
      case "submitButton":
        if (numberLabel.getText().equals("123")) {
          // dialogue.setText("Nice work, you cracked the code to the door!");
          System.out.println("user solved the code");
          CommanderController commander = CommanderController.getInstance();
          commander.updateDialogueBox("Nice work, you cracked the code to the door!");
        }
        break;
      case "exitButton":
        currentScene.setRoot(SceneManager.getuserInterface(AppUI.MAIN));
        break;
      default:
        break;
    }
  }

  @FXML
  public void clickNum(MouseEvent event) {
    Rectangle number = (Rectangle) event.getSource();
    String currentText = numberLabel.getText();
    switch (number.getId()) {
      case ("one"):
        currentText = currentText + "1";
        break;
      case ("two"):
        currentText = currentText + "2";
        break;
      case ("three"):
        currentText = currentText + "3";
        break;
      case ("four"):
        currentText = currentText + "4";
        break;
      case ("five"):
        currentText = currentText + "5";
        break;
      case ("six"):
        currentText = currentText + "6";
        break;
      case ("seven"):
        currentText = currentText + "7";
        break;
      case ("eight"):
        currentText = currentText + "8";
        break;
      case ("nine"):
        currentText = currentText + "9";
        break;
      case ("zero"):
        currentText = currentText + "0";
        break;
      default:
        break;
    }
    numberLabel.setText(currentText);
  }
}
