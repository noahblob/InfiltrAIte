package nz.ac.auckland.se206.controllers;

import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;

/** Controller class for the room view. */
public class MainRoomController extends Commander implements TimerObserver {

  // FXML objects in room
  @FXML private TextArea objective;
  @FXML private Button back;
  @FXML private Button cabinetButton;
  @FXML private Polygon leftDoor;
  @FXML private Polygon rightDoor;
  @FXML private Rectangle middleDoor;
  @FXML private Rectangle background;
  @FXML private Rectangle topDrawer;
  @FXML private Rectangle midDrawer;
  @FXML private Rectangle botDrawer;
  @FXML private ImageView filingCabinet;
  @FXML private ImageView intelFile;
  // Initialize a random drawer to contain intel
  private Random random = new Random();
  private int randomDrawer = 1 + random.nextInt(2);

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws Exception
   */
  public void initialize() throws Exception {
    super.initialize();
    objective.setText("Find 3 pieces of intel and escape!");
    // set timer
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
    // switch rooms upon arrow key press
    if (event.getCode().toString().equals(("LEFT"))) {
      currentScene.setRoot(SceneManager.getuserInterface(AppUI.LEFT));
    } else if (event.getCode().toString().equals("RIGHT")) {
      currentScene.setRoot(SceneManager.getuserInterface(AppUI.RIGHT));
    }
  }

  /** Handles the click and hover effects for left and right door in main room. */
  public void setDoorEvents() {
    // set click functionaltiy for left and right door, this will take the player to relative rooms
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

  /**
   * Handles the onClick event for all rectangles in the middle room.
   *
   * @param event the mouse event
   * @throws Exception if the scene cannot be loaded
   */
  @FXML
  public void onClick(MouseEvent event) throws Exception {
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene rectangleScene = rectangle.getScene();
    // switch case for each rectangle, including the middle door, keypad and cabinet
    switch (rectangle.getId()) {
      case ("middleDoor"):
        CommanderController commander = CommanderController.getInstance();
        if (GameState.isKeypadSolved) {
          // set commander message based on how much intel needs to be found
          if (GameState.numOfIntel.get() < 3) {
            commander.updateDialogueBox(
                "Don't forget, there is still "
                    + (3 - GameState.numOfIntel.get())
                    + " more intel to find!");
          } else {
            // if user has found all intel, set commander message to found all intel
            commander.updateDialogueBox(Dialogue.FOUNDALLINTEL.toString());
          }
        } else {
          // if keypad is yet to be solved
          commander.updateDialogueBox(Dialogue.SOLVEKEYPAD.toString());
        }
        break;
      case ("keyPad"):
        rectangleScene.setRoot(SceneManager.getuserInterface(AppUI.KEYPAD));
        break;
      case ("cabinet"):
        // set visibility of the filing cabinet and background
        cabinetVisibility(true);
        break;
      default:
        break;
    }
  }

  /**
   * Handles the back button on cabinet popup to disable all popups.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickBack(MouseEvent event) {
    Button button = (Button) event.getSource();
    switch (button.getId()) {
      case ("back"):
        // Set visibility of filing cabinet and background to false to return to room
        cabinetVisibility(false);
        break;
      case ("cabinetButton"):
        // Return to cabinet after finding intel
        cabinetButton.setVisible(false);
        intelFile.setVisible(false);
        break;
      default:
        break;
    }
  }

  /**
   * Sets the visibility of the filing cabinet and background.
   *
   * @param visible true if the cabinet should be visible, false otherwise
   */
  public void cabinetVisibility(boolean visible) {
    // set visibility of filing cabinet and background
    filingCabinet.setVisible(visible);
    background.setVisible(visible);
    back.setVisible(visible);
    topDrawer.setVisible(visible);
    midDrawer.setVisible(visible);
    botDrawer.setVisible(visible);
  }

  /**
   * Handles the click event on the cabinet drawers.
   *
   * @param event the mouse event
   * @throws Exception if the scene cannot be loaded
   */
  @FXML
  public void clickDrawer(MouseEvent event) throws Exception {
    Rectangle drawer = (Rectangle) event.getSource();
    CommanderController commander = CommanderController.getInstance();
    // check which drawer the user has pressed
    switch (drawer.getId()) {
      case ("topDrawer"):
        if (randomDrawer == 1) {
          checkDrawer(GameState.isKeyFound.get(), GameState.cabinetIntelfound);
        } else {
          commander.updateDialogueBox(Dialogue.EMPTY.toString());
        }
        break;
      case ("midDrawer"):
        // same case for top drawer but for middle
        if (randomDrawer == 2) {
          checkDrawer(GameState.isKeyFound.get(), GameState.cabinetIntelfound);
        } else {
          commander.updateDialogueBox(Dialogue.EMPTY.toString());
        }
        break;
      case ("botDrawer"):
        // same case for top and middle drawer but for bottom drawer
        if (randomDrawer == 3) {
          checkDrawer(GameState.isKeyFound.get(), GameState.cabinetIntelfound);
        } else {
          commander.updateDialogueBox(Dialogue.EMPTY.toString());
        }
        break;
      default:
        break;
    }
  }

  /**
   * Checks if the user has found the key and cabinet intel.
   *
   * @param isKeyFound true if the key has been found, false otherwise
   * @param cabinetIntelFound true if the cabinet intel has been found, false otherwise
   * @throws Exception if the scene cannot be loaded
   */
  public void checkDrawer(boolean isKeyFound, boolean cabinetIntelFound) throws Exception {
    CommanderController commander = CommanderController.getInstance();
    if (isKeyFound && !cabinetIntelFound) {
      // if key for cabinet has been found and cabinet intel has not yet been found, update cabinet
      // intel to being found
      intelFile.setVisible(true);
      cabinetButton.setVisible(true);
      GameState.numOfIntel.set(GameState.numOfIntel.get() + 1);
      GameState.cabinetIntelfound = true;
    } else if (isKeyFound && cabinetIntelFound) {
      // user has already found cabinet intel
      commander.updateDialogueBox(Dialogue.INTELALREADYFOUND.toString());
    } else {
      // user is missing key to cabinet
      commander.updateDialogueBox(Dialogue.KEYNEEDED.toString());
    }
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
