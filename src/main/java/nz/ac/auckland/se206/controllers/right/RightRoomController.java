package nz.ac.auckland.se206.controllers.right;

import java.io.IOException;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

/** Controller class for the room view. */
public class RightRoomController extends Commander implements TimerObserver {

  public enum NumberGroup {
    ANS1,
    ANS2,
    ANS3,
  }

  static NumberGroup answer;

  @FXML private Polygon riddle;
  @FXML private Polygon blackboard;

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws Exception
   */
  public void initialize() throws Exception {

    // Initialise phone.
    super.initialize();
    objective.setText("You must find the clue!!");
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
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.MAIN));
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
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.LOCKER));
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
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.BLACKBOARD));
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
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.BOOKSHELF));
  }
}
