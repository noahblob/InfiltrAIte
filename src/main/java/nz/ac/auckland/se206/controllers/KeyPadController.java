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
  @FXML private Rectangle one;
  @FXML private Rectangle two;
  @FXML private Rectangle three;
  @FXML private Rectangle four;
  @FXML private Rectangle five;
  @FXML private Rectangle six;
  @FXML private Rectangle seven;
  @FXML private Rectangle eight;
  @FXML private Rectangle nine;
  @FXML private Rectangle zero;
  private int factor = 1;

  public void initialize() throws ApiProxyException {
    super.initialize();
    objectiveMiddle.setText("Figure out the combination!");
    numberLabel.setText("0");
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
  public void onClick(MouseEvent event) {
    Button button = (Button) event.getSource();
    Scene currentScene = button.getScene();
    switch (button.getId()) {
      case "clearButton":
        numberLabel.setText("0");
        factor = 1;
        break;
      case "submitButton":
        factor = 1;
        if (numberLabel.getText() == "123") {
          dialogue.setText("Nice work, you cracked the code to the door!");
        }
        currentScene.setRoot(SceneManager.getuserInterface(AppUI.MAIN));
        break;
      case "exitButton":
        factor = 1;
        currentScene.setRoot(SceneManager.getuserInterface(AppUI.MAIN));
        break;
      default:
        break;
    }
  }

  @FXML
  public void clickNum(MouseEvent event) {
    Rectangle number = (Rectangle) event.getSource();
    int currentValue = Integer.parseInt(numberLabel.getText());
    switch (number.getId()) {
      case ("one"):
        currentValue += 1 * factor;
        factor *= 10;
        break;
      case ("two"):
        currentValue += 2 * factor;
        factor *= 10;
        break;
      case ("three"):
        currentValue += 3 * factor;
        factor *= 10;
        break;
      case ("four"):
        currentValue += 4 * factor;
        factor *= 10;
        break;
      case ("five"):
        currentValue += 5 * factor;
        factor *= 10;
        break;
      case ("six"):
        currentValue += 6 * factor;
        factor *= 10;
        break;
      case ("seven"):
        currentValue += 7 * factor;
        factor *= 10;
        break;
      case ("eight"):
        currentValue += 8 * factor;
        factor *= 10;
        break;
      case ("nine"):
        currentValue += 9 * factor;
        factor *= 10;
        break;
      case ("zero"):
        String text = numberLabel.getText();
        text = "0" + text;
        numberLabel.setText(text);
        factor *= 10;
        break;
      default:
        break;
    }
    numberLabel.setText(String.valueOf(currentValue));
  }
}
