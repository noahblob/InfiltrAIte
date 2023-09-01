package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Controller class for the room view. */
public class LockerController extends Phone implements TimerObserver {
  @FXML private Label objective;
  @FXML private Text timer;
  @FXML private Label first;
  @FXML private Label second;
  @FXML private Label third;
  private int one = 0;
  private int two = 0;
  private int three = 0;

  /** Initializes the room view, it is called when the room loads. 
   * @throws ApiProxyException */
  public void initialize() throws ApiProxyException {
    super.initialize();
    objective.setText("Whats the correct combination?");
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
    System.out.println("door clicked");
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene currentScene = rectangle.getScene();
    // Update the scene to the main room
    currentScene.setRoot(SceneManager.getuserInterface(AppUI.MAIN));
  }

  /**
   * Handles the increase event.
   *
   * @param event the mouse event
   */
  @FXML
  public void increase(MouseEvent event) {
    Rectangle rect = (Rectangle) event.getSource();
    switch (rect.getId()) {
      case "upOne":
        if (one == 9) one = 0;
        else one++;
        first.setText(String.valueOf(one));
        break;

      case "upTwo":
        if (two == 9) two = 0;
        else two++;
        second.setText(String.valueOf(two));
        break;

      case "upThree":
        if (three == 9) three = 0;
        else three++;
        third.setText(String.valueOf(three));
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
    switch (rect.getId()) {
      case "downOne":
        if (one == 0) one = 9;
        else one--;
        first.setText(String.valueOf(one));
        break;

      case "downTwo":
        if (two == 0) two = 9;
        else two--;
        second.setText(String.valueOf(two));
        break;

      case "downThree":
        if (three == 0) three = 9;
        else three--;
        third.setText(String.valueOf(three));
        break;

      default:
        break;
    }
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
    currentScene.setRoot(SceneManager.getuserInterface(AppUI.RIGHT));
  }
}
