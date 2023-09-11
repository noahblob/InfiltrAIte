package nz.ac.auckland.se206.controllers;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Controller class for the room view. */
public class MainRoomController extends Commander implements TimerObserver {

  @FXML private Label objectiveMiddle;
  @FXML private Button back;
  @FXML private Polygon leftDoor;
  @FXML private Polygon rightDoor;
  @FXML private Rectangle middleDoor;
  @FXML private Rectangle background;
  @FXML private Rectangle topDrawer;
  @FXML private Rectangle midDrawer;
  @FXML private Rectangle botDrawer;
  @FXML private ImageView filingCabinet;
  @FXML private Label intel;

  /**
   * s Initializes the room view, it is called when the room loads.
   *
   * @throws ApiProxyException
   */
  public void initialize() throws ApiProxyException {
    intel.textProperty().bind(Bindings.concat("x", GameState.numOfIntel.asString()));

    super.initialize();
    objectiveMiddle.setText("This is the MAIN ROOM");
    TimerClass.add(this);

    // separate method for left and right door hover and click events
    setDoorEvents();
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

  public void setDoorEvents() {
    // set click functionaltiy for left and right door
    leftDoor.setOnMouseClicked(
        event -> {
          Polygon object = (Polygon) event.getSource();
          Scene scene = object.getScene();
          scene.setRoot(SceneManager.getuserInterface(AppUI.LEFT));
        });
    rightDoor.setOnMouseClicked(
        event -> {
          Polygon object = (Polygon) event.getSource();
          Scene scene = object.getScene();
          scene.setRoot(SceneManager.getuserInterface(AppUI.RIGHT));
        });
    // set hover effects for left door and right door
    leftDoor.setOnMouseEntered(
        event -> {
          leftDoor.setOpacity(1);
        });
    leftDoor.setOnMouseExited(
        event -> {
          leftDoor.setOpacity(0);
        });
    rightDoor.setOnMouseEntered(
        event -> {
          rightDoor.setOpacity(1);
        });
    rightDoor.setOnMouseExited(
        event -> {
          rightDoor.setOpacity(0);
        });
  }

  @FXML
  public void onClick(MouseEvent event) throws ApiProxyException {
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene rectangleScene = rectangle.getScene();
    switch (rectangle.getId()) {
      case ("middleDoor"):
        CommanderController commander = CommanderController.getInstance();
        if (GameState.isKeypadSolved) {
          switch (GameState.numOfIntel.get()) {
            case 0:
              commander.updateDialogueBox(
                  "Don't forget, there is still "
                      + (GameState.difficulty - GameState.numOfIntel.get())
                      + " more intel to find!");
              break;
            case 1:
              if (GameState.difficulty == 1) {
                commander.updateDialogueBox("You have found all required intel, time to escape!");
              } else {
                commander.updateDialogueBox(
                    "Don't forget, there is still "
                        + (GameState.difficulty - GameState.numOfIntel.get())
                        + " more intel to find!");
              }
              break;
            case 2:
              if (GameState.difficulty <= 2) {
                commander.updateDialogueBox("You have found all required intel, time to escape!");
              } else {
                commander.updateDialogueBox(
                    "Don't forget, there is still "
                        + (GameState.difficulty - GameState.numOfIntel.get())
                        + " more intel to find!");
              }
              break;
            case 3:
              commander.updateDialogueBox("You have found all required intel, time to escape!");
              break;
            default:
              break;
          }
        } else {
          commander.updateDialogueBox("You need to solve the keypad first!");
        }
        break;
      case ("keyPad"):
        rectangleScene.setRoot(SceneManager.getuserInterface(AppUI.KEYPAD));
        break;
      case ("cabinet"):
        filingCabinet.setVisible(true);
        background.setVisible(true);
        back.setVisible(true);
        topDrawer.setVisible(true);
        midDrawer.setVisible(true);
        botDrawer.setVisible(true);
        break;
      default:
        break;
    }
  }

  @FXML
  public void clickBack(MouseEvent event) {
    filingCabinet.setVisible(false);
    background.setVisible(false);
    back.setVisible(false);
    topDrawer.setVisible(false);
    midDrawer.setVisible(false);
    botDrawer.setVisible(false);
  }

  /**
   * Handles the hover event on the left door.
   *
   * @param event the mouse event
   */
  @FXML
  public void onHover(MouseEvent event) {
    // enable indicator
    Rectangle rectangle = (Rectangle) event.getSource();
    rectangle.setOpacity(1);
  }

  /**
   * Handles the un-hover event on the left door.
   *
   * @param event the mouse event
   */
  @FXML
  public void onHoverExit(MouseEvent event) {
    // disable indicator
    Rectangle rectangle = (Rectangle) event.getSource();
    rectangle.setOpacity(0);
  }
}
