package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

/** Controller class for the keypad view. */
public class KeyPadController extends Commander implements TimerObserver {

  @FXML private Button clearButton;
  @FXML private Button submitButton;
  @FXML private Button exitButton;
  @FXML private Label numberLabel;
  @FXML private TextArea dialogue;
  @FXML private TextArea objective;
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

  /**
   * Initializes the keypad view, it is called when the keypad loads.
   *
   * @throws Exception
   */
  public void initialize() throws Exception {
    super.initialize();
    objective.setText("Figure out the combination!");
    numberLabel.setText("");
    TimerClass.add(this);
  }

  @Override
  public void timerStart() {
    TimerClass timerText = TimerClass.getInstance();
    timer.setText(timerText.getTimerLeft());
  }

  /**
   * Handles the click event for the keypad buttons.
   *
   * @param event the mouse event
   * @throws Exception
   */
  @FXML
  public void onClick(MouseEvent event) throws Exception {
    Button button = (Button) event.getSource();
    Scene currentScene = button.getScene();
    // switch case for the different buttons, including clear, submit and exit
    switch (button.getId()) {
      case "clearButton":
        numberLabel.setText("");
        break;
      case "submitButton":
        CommanderController commander = CommanderController.getInstance();
        // Check if the user has added anything
        if (numberLabel.getText().equals("")) {
          commander.updateDialogueBox("You haven't entered anything!");
          break;
        }
        // Check if user has typed in the correct value to the keypad
        if (Integer.parseInt(numberLabel.getText()) == BlackBoardController.getKeypadAns()) {
          // Update the game state (keypad is solved)
          GameState.setKeypadSolved(true);
          commander.updateDialogueBox("Nice work, you cracked the code to the door!");
        } else {
          commander.updateDialogueBox("That's not the right code, try again!");
        }
        break;
      case "exitButton":
        currentScene.setRoot(SceneManager.getuserInterface(AppUi.MAIN));
        break;
      default:
        break;
    }
  }

  /**
   * Handles the click event for the keypad numbers.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickNum(MouseEvent event) {
    Rectangle button = (Rectangle) event.getSource();
    StringBuilder currentText = new StringBuilder(numberLabel.getText());
    String number = button.getAccessibleText();
    // Limit the user to typing only 3 numbers at a time
    if (currentText.length() >= 3) {
      return;
    } else {
      // update current Text with the number clicked
      currentText.append(number);
    }
    String newText = currentText.toString();
    numberLabel.setText(newText);
  }
}
