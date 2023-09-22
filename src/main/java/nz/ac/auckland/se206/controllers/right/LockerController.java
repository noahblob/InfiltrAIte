package nz.ac.auckland.se206.controllers.right;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

/** Controller class for the room view. */
public class LockerController extends Commander {

  public static LockerController instance;

  public static LockerController getInstance() {
    return instance;
  }

  @FXML private Label first;
  @FXML private Label second;
  @FXML private Label third;
  @FXML private Label fourth;
  @FXML private ImageView intelFile;
  @FXML private ImageView roomimage;
  @FXML private Button checkAns;
  @FXML private Button goBack;

  private int one;
  private int two;
  private int three;
  private int four;

  public LockerController() {
    instance = this;
  }

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws Exception
   */
  public void initialize() throws Exception {
    super.initialize();
    one = two = three = four = 0;
    objective.setText("Whats the correct combination?");
  }

  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickDoor(MouseEvent event) throws IOException {
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene currentScene = rectangle.getScene();
    // Update the scene to the main room
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.MAIN));
  }

  /**
   * Checks the answer
   *
   * @param event
   * @throws Exception
   */
  @FXML
  public void onCheckAns(MouseEvent event) throws Exception {
    // If the right cabinet intel has not been found, check if the answer is correct
    if (!GameState.cabinetRightIntelfound) {
      int answer = GameState.lastNumbers.get() + 1900;
      int userAnswer = (one * 1000 + two * 100 + three * 10 + four);
      if (answer == userAnswer) {
        // if the user answers correct, show them the intel file
        intelFile.setDisable(false);
        intelFile.setVisible(true);
        roomimage.setVisible(false);
        checkAns.setVisible(false);
        goBack.setVisible(false);
        showIntel();
      } else {
        // Otherwise update commander dialogue to alert player they have guessed incorrectly
        updateDialogue(Dialogue.WRONGYEAR);
      }
    }
  }

  private void showIntel() {
    // On the event of the intel file being clicked, update intel count if the right cabinet intel
    // is yet to be found, and update commander dialogue
    intelFile.setOnMouseClicked(
        event -> {
          if (!GameState.cabinetRightIntelfound) {
            GameState.numOfIntel.set(GameState.numOfIntel.get() + 1);
          }
          Scene currentScene = input.getScene();
          GameState.cabinetRightIntelfound = true;
          try {
            updateDialogue(Dialogue.CORRECTYEAR);
          } catch (Exception e) {
            e.printStackTrace();
          }
          // go back to room upon clicking on intel
          currentScene.setRoot(SceneManager.getuserInterface(AppUi.RIGHT));
        });
  }

  /**
   * Handles the increase event.
   *
   * @param event the mouse event
   */
  @FXML
  public void increase(MouseEvent event) {
    Rectangle rect = (Rectangle) event.getSource();

    // Check which rectangle is clicked then increase the number shown.
    switch (rect.getId()) {
      case "upOne":
        one = (one == 9) ? 0 : ++one; // increment unless 9, then set to 0.
        first.setText(String.valueOf(one));
        break;
      case "upTwo":
        two = (two == 9) ? 0 : ++two;
        second.setText(String.valueOf(two));
        break;
      case "upThree":
        three = (three == 9) ? 0 : ++three;
        third.setText(String.valueOf(three));
        break;
      case "upFour":
        four = (four == 9) ? 0 : ++four;
        fourth.setText(String.valueOf(four));
        break;
      default:
        break;
    }
  }

  /**
   * Handles the decrease event.
   *
   * @param event the mouse event
   */
  @FXML
  public void decrease(MouseEvent event) {
    Rectangle rect = (Rectangle) event.getSource();
    // Check which rectangle was clicked and then decrement the number shown.
    switch (rect.getId()) {
      case "downOne":
        one = (one == 0) ? 9 : --one; // decrement unless 0, then set to 1.
        first.setText(String.valueOf(one));
        break;
      case "downTwo":
        two = (two == 0) ? 9 : --two;
        second.setText(String.valueOf(two));
        break;
      case "downThree":
        three = (three == 0) ? 9 : --three;
        third.setText(String.valueOf(three));
        break;
      case "downFour":
        four = (four == 0) ? 9 : --four;
        fourth.setText(String.valueOf(four));
        break;
      default:
        break;
    }
  }

  public void resetRoom() {
    // Reset all the variables to their original state in the locker once the user restarts the game
    one = two = three = four = 0;
    // set visibility of all relevant elements
    intelFile.setDisable(true);
    intelFile.setVisible(false);
    roomimage.setVisible(true);
    checkAns.setVisible(true);
    goBack.setVisible(true);
    // set text of all relevant elements
    first.setText(String.valueOf(one));
    second.setText(String.valueOf(two));
    third.setText(String.valueOf(three));
    fourth.setText(String.valueOf(four));
  }

  /**
   * Handles the return event
   *
   * @param event the mouse event
   */
  @FXML
  public void onGoBack(MouseEvent event) {
    Button rectangle = (Button) event.getSource();
    Scene currentScene = rectangle.getScene();
    // Update the scene to the right room
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.RIGHT));
  }
}
