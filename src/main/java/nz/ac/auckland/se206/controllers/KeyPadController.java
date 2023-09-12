package nz.ac.auckland.se206.controllers;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;

/** Controller class for the keypad view. */
public class KeyPadController extends Commander implements TimerObserver {

  @FXML private Button clearButton;
  @FXML private Button submitButton;
  @FXML private Button exitButton;
  @FXML private Label objectiveMiddle;
  @FXML private Label numberLabel;
  @FXML private TextArea dialogue;
  @FXML private Rectangle one, two, three, four, five, six, seven, eight, nine, zero;
  @FXML private Label intel;

  /**
   * Initializes the keypad view, it is called when the keypad loads.
   * @throws Exception
   */
  public void initialize() throws Exception {
    intel.textProperty().bind(Bindings.concat("x", GameState.numOfIntel.asString()));

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
        // Check if user has typed in the correct value to the keypad
        if (Integer.parseInt(numberLabel.getText()) == BlackBoardController.getKeypadAns()) {
          GameState.isKeypadSolved = true;
          commander.updateDialogueBox("Nice work, you cracked the code to the door!");
        } else {
          commander.updateDialogueBox("That's not the right code, try again!");
        }
        break;
      case "exitButton":
        currentScene.setRoot(SceneManager.getuserInterface(AppUI.MAIN));
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
    Rectangle number = (Rectangle) event.getSource();
    String currentText = numberLabel.getText();
    // Limit the user to typing only 3 numbers at a time
    if (currentText.length() >= 3) {
      return;
    } else {
      // Update text to new number after user has pressed a number button
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
    }
    numberLabel.setText(currentText);
  }
}
