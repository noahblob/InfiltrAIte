package nz.ac.auckland.se206.controllers.main;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.right.BlackBoardController;

/** Controller class for the keypad view. */
public class KeyPadController extends Commander {

  @FXML private Button clearButton;
  @FXML private Button submitButton;
  @FXML private Button exitButton;
  @FXML private Label numberLabel;
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
    Sound.getInstance().playClickMinor();
    // switch case for the different buttons, including clear, submit and exit
    switch (button.getId()) {
      case "clearButton":
        numberLabel.setText("");
        break;
      case "submitButton":
        // Check if the user has added anything
        if (numberLabel.getText().equals("")) {
          updateDialogue(Dialogue.NOCODE);
          break;
        }
        // Check if user has typed in the correct value to the keypad
        if (Integer.parseInt(numberLabel.getText()) == BlackBoardController.getKeypadAns()) {
          // Update the game state (keypad is solved)
          GameState.isKeypadSolved.set(true);
          updateDialogue(Dialogue.DOORUNLOCK);
        } else {
          updateDialogue(Dialogue.WRONGCODE);
        }
        break;
      case "exitButton":
        numberLabel.setText("");
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
    Sound.getInstance().playClickMinor();
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
