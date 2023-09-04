package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Controller class for the room view. */
public class MainRoomController extends Commander implements TimerObserver {

  @FXML private TextArea objective;
  @FXML private Text timer;
  @FXML private Rectangle leftDoor;
  @FXML private Rectangle rightDoor;
  @FXML private Rectangle middleDoor;

  /** Initializes the room view, it is called when the room loads. 
   * @throws ApiProxyException */
  public void initialize() throws ApiProxyException {

    // Initialization code goes here
    // Initialise phone.
    super.initialize();
    objective.setText("This is the MAIN ROOM");
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
    Pane pane = (Pane) event.getSource();
    Scene currentScene = pane.getScene();
    if (event.getCode().toString().equals(("LEFT"))) {
      currentScene.setRoot(SceneManager.getuserInterface(AppUI.LEFT));
    } else if (event.getCode().toString().equals("RIGHT")) {
      currentScene.setRoot(SceneManager.getuserInterface(AppUI.RIGHT));
    }
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
    System.out.println("door clicked");

    if (!GameState.isRiddleResolved) {
      showDialog("Info", "Riddle", "You need to resolve the riddle!");
      App.setRoot("chat");
      return;
    }

    if (!GameState.isKeyFound) {
      showDialog(
          "Info", "Find the key!", "You resolved the riddle, now you know where the key is.");
    } else {
      showDialog("Info", "You Won!", "Good Job!");
    }
  }

  /**
   * Handles the click event on the left door.
   *
   * @param event the mouse event
   */
  @FXML
  public void onLeftClick(MouseEvent event) {
    // move the user to the left room
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene currentScene = rectangle.getScene();
    currentScene.setRoot(SceneManager.getuserInterface(AppUI.LEFT));
  }

  /**
   * Handles the click event on the middle door.
   *
   * @param event the mouse event
   */
  @FXML
  public void onMiddleClick(MouseEvent event) {
    if (GameState.difficulty == 1) {
      dialogue.setText("You must gather 1 more piece of intel before you may leave.");
    } else if (GameState.difficulty == 2) {
      dialogue.setText("You must gather 2 more pieces of intel before you may leave.");
    } else if (GameState.difficulty == 3) {
      dialogue.setText("You must gather 3 more pieces of intel before you may leave.");
    }
  }

  /**
   * Handles the click event on the right door.
   *
   * @param event the mouse event
   */
  @FXML
  public void onRightClick(MouseEvent event) {
    // move the user to the right room
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene currentScene = rectangle.getScene();
    currentScene.setRoot(SceneManager.getuserInterface(AppUI.RIGHT));
  }

  /**
   * Handles the hover event on the left door.
   *
   * @param event the mouse event
   */
  @FXML
  public void onLeftEnter(MouseEvent event) {
    // enable indicator
    leftDoor.setOpacity(1);
  }

  /**
   * Handles the un-hover event on the left door.
   *
   * @param event the mouse event
   */
  @FXML
  public void onLeftExit(MouseEvent event) {
    // disable indicator
    leftDoor.setOpacity(0);
  }

  /**
   * Handles the hover event on the middle door.
   *
   * @param event the mouse event
   */
  @FXML
  public void onMiddleEnter(MouseEvent event) {
    // enable indicator
    middleDoor.setOpacity(1);
  }

  /**
   * Handles the un-hover event on the left door.
   *
   * @param event the mouse event
   */
  @FXML
  public void onMiddleExit(MouseEvent event) {
    // disable indicator
    middleDoor.setOpacity(0);
  }

  /**
   * Handles the hover event on the right door.
   *
   * @param event the mouse event
   */
  @FXML
  public void onRightEnter(MouseEvent event) {
    // enable indicator
    rightDoor.setOpacity(1);
  }

  /**
   * Handles the un-hover event on the left door.
   *
   * @param event the mouse event
   */
  @FXML
  public void onRightExit(MouseEvent event) {
    // didsable indicator
    rightDoor.setOpacity(0);
  }

}
