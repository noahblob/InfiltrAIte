package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Random;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Controller class for the room view. */
public class RightRoomController extends Commander implements TimerObserver {

  @FXML private Label objectiveRight;
  @FXML private Polygon riddle;
  @FXML private Polygon blackboard;
  @FXML private Label intel;

  static NumberGroup answer;

  public enum NumberGroup {
    ANS1,
    ANS2,
    ANS3,
  }

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws ApiProxyException
   */
  public void initialize() throws ApiProxyException {
    intel.textProperty().bind(Bindings.concat("x", GameState.numOfIntel.asString()));

    // Initialise phone.
    super.initialize();
    objectiveRight.setText("You must find the clue!!");
    TimerClass.add(this);
    final NumberGroup[] answerGroup = NumberGroup.values();
    // Randomnly select a number group
    answer = answerGroup[new Random().nextInt(answerGroup.length)];

    riddle.setOnMouseEntered(
        event -> {
          riddle.setOpacity(0.5);
        });
    riddle.setOnMouseExited(
        event -> {
          riddle.setOpacity(0);
        });

    blackboard.setOnMouseEntered(
        event -> {
          blackboard.setOpacity(0.5);
        });
    blackboard.setOnMouseExited(
        event -> {
          blackboard.setOpacity(0);
        });
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
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("key " + event.getCode() + " released");
  }

  /**
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message the message content of the dialog box
   */
  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickDoor(MouseEvent event) throws IOException {
    System.out.println(BlackBoardController.getKeypadAns());
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene currentScene = rectangle.getScene();
    // Update the scene to the main room
    currentScene.setRoot(SceneManager.getuserInterface(AppUI.MAIN));
  }

  /**
   * Handles the click event on the vase.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickRiddle(MouseEvent event) {

    Polygon rectangle = (Polygon) event.getSource();
    Scene currentScene = rectangle.getScene();
    // Update the scene to the main room
    currentScene.setRoot(SceneManager.getuserInterface(AppUI.LOCKER));
  }

  /**
   * Shows the blackboard this is connected to the answer for the keypad
   *
   * @param event
   */
  @FXML
  public void clickBlackBoard(MouseEvent event) throws IOException {
    Polygon poly = (Polygon) event.getSource();
    Scene currentScene = poly.getScene();
    // Update the scene to the blackboard
    currentScene.setRoot(SceneManager.getuserInterface(AppUI.BLACKBOARD));
  }

  /**
   * Handles the hovering of rectangles
   *
   * @param event the mouse
   */
  @FXML
  public void onHover(MouseEvent event) {
    Rectangle rectangle = (Rectangle) event.getSource();
    rectangle.setOpacity(1);
  }

  /**
   * Handles the un-hovering of rectangles
   *
   * @param event the mouse
   */
  @FXML
  public void onHoverExit(MouseEvent event) {
    Rectangle rectangle = (Rectangle) event.getSource();
    rectangle.setOpacity(0);
  }

  /**
   * Handles the click event on the window.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickBook(MouseEvent event) {
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene currentScene = rectangle.getScene();
    // Update the scene to the main game.
    currentScene.setRoot(SceneManager.getuserInterface(AppUI.BOOKSHELF));
  }
}
