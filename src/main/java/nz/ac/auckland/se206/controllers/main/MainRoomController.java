package nz.ac.auckland.se206.controllers.main;

import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
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
import nz.ac.auckland.se206.controllers.CommanderController;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

/** Controller class for the room view. */
public class MainRoomController extends Commander {

  // FXML objects in room
  @FXML private Button back;
  @FXML private Polygon leftDoor;
  @FXML private Polygon rightDoor;
  @FXML private Rectangle computer;
  @FXML private Rectangle middleDoor;
  @FXML private Rectangle background;
  @FXML private Rectangle topDrawer;
  @FXML private Rectangle midDrawer;
  @FXML private Rectangle botDrawer;
  @FXML private ImageView filingCabinet;
  @FXML private ImageView intelFile;
  @FXML private ImageView roomimage;
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
    objective.setText("Find intel and escape!");
    // separate method for left and right door hover and click events
    setDoorEvents();
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
      currentScene.setRoot(SceneManager.getuserInterface(AppUi.LEFT));
    } else if (event.getCode().toString().equals("RIGHT")) {
      currentScene.setRoot(SceneManager.getuserInterface(AppUi.RIGHT));
    }
  }

  /** Handles the click and hover effects for left and right door in main room. */
  public void setDoorEvents() {
    // set click functionaltiy for left and right door, this will take the player to relative rooms
    leftDoor.setOnMouseClicked(
        event -> {
          Polygon object = (Polygon) event.getSource();
          Scene scene = object.getScene();
          // if cabinet is on the screen, make it not visible when switching rooms
          setCabinetVisibility(false);
          scene.setRoot(SceneManager.getuserInterface(AppUi.LEFT));
        });
    rightDoor.setOnMouseClicked(
        event -> {
          Polygon object = (Polygon) event.getSource();
          Scene scene = object.getScene();
          setCabinetVisibility(false);
          scene.setRoot(SceneManager.getuserInterface(AppUi.RIGHT));
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
    Scene currentScene = rectangle.getScene();
    // switch case for each rectangle, including the middle door, keypad and cabinet
    switch (rectangle.getId()) {
      case ("middleDoor"):
        doorCheck(currentScene);
        break;
      case ("keyPad"):
        // if the user has solved computer password, go to keypad, otherwise update dialogue saying
        // it seems locked
        if (GameState.isPasswordSolved) {
          if (GameState.isKeypadSolved.get()) {
            // if user has already solved the keypad, let them know they have already solved this
            // puzzle, otherwise go to the keypad
            updateDialogue(Dialogue.ALREADYSOLVED);
          } else {
            currentScene.setRoot(SceneManager.getuserInterface(AppUi.KEYPAD));
          }
        } else {
          updateDialogue(Dialogue.KEYPADLOCKED);
        }
        break;
      case ("cabinet"):
        // set visibility of the filing cabinet and background (if cabinet intel has not already
        // been found)
        if (!GameState.cabinetMiddleIntelfound) {
          setCabinetVisibility(true);
        } else {
          updateDialogue(Dialogue.INTELALREADYFOUND);
        }
        break;
      case ("computer"):
        if (GameState.isPasswordSolved) {
          // if user has solved computer password already, notify them that this puzzle has already
          // been solved
          updateDialogue(Dialogue.ALREADYSOLVED);
        } else {
          updateDialogue(Dialogue.COMPUTERHINT);
          currentScene.setRoot(SceneManager.getuserInterface(AppUi.COMPUTER));
        }
        break;
      default:
        break;
    }
  }

  /**
   * Checks if keypad has been solved and how much intel has been found to exit room.
   *
   * @throws Exception if the scene cannot be loaded
   */
  public void doorCheck(Scene currentScene) throws Exception {
    if (GameState.isKeypadSolved.get()) {
      // Change scene or dialogue based on how much intel user has found
      if (GameState.numOfIntel.get() >= 1) {
        // Pause Timer
        TimerClass.getInstance().stop();
        // Reset all aspects of the room to original state for replay functionality
        roomimage.setImage(new Image("/images/startLocked.png"));
        // Allow user to leave room with any amount of intel
        currentScene.setRoot(SceneManager.getuserInterface(AppUi.END));
        System.gc();
      } else {
        // if user has not found any intel, update them reminding them to find intel
        updateDialogue(Dialogue.NOINTELFOUND);
      }
    } else if (GameState.isPasswordSolved) {
      // if keypad is yet to be solved
      updateDialogue(Dialogue.SOLVEKEYPAD);
    } else {
      // if the computer hasn't been opened yet.
      updateDialogue(Dialogue.DOORLOCKED);
    }
  }

  /**
   * Handles the back button on cabinet popup to disable all popups.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickBack(MouseEvent event) {
    // Set visibility of filing cabinet and background to false to return to room
    setCabinetVisibility(false);
  }

  /**
   * Sets the visibility of the filing cabinet and background.
   *
   * @param visible true if the cabinet should be visible, false otherwise
   */
  public void setCabinetVisibility(boolean visible) {
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
          checkDrawer(GameState.isKeyFound.get(), GameState.cabinetMiddleIntelfound);
        } else {
          commander.updateDialogueBox(Dialogue.EMPTY.toString());
        }
        break;
      case ("midDrawer"):
        // same case for top drawer but for middle
        if (randomDrawer == 2) {
          checkDrawer(GameState.isKeyFound.get(), GameState.cabinetMiddleIntelfound);
        } else {
          commander.updateDialogueBox(Dialogue.EMPTY.toString());
        }
        break;
      case ("botDrawer"):
        // same case for top and middle drawer but for bottom drawer
        if (randomDrawer == 3) {
          checkDrawer(GameState.isKeyFound.get(), GameState.cabinetMiddleIntelfound);
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
    if (isKeyFound && !cabinetIntelFound) {
      // if key for cabinet has been found and cabinet intel has not yet been found, update cabinet
      // intel to being found
      intelFile.setVisible(true);

      // Player uses key, so key disappears.
      GameState.isKeyFound.set(false);

      // Update commander dialogue to prompt player that key has been used.
      updateDialogue(Dialogue.KEYUSED);

      // Once user has collected intel, make it invisible and update number of intel collected.
      intelFile.setOnMouseClicked(
          event -> {
            intelFile.setVisible(false);
            setCabinetVisibility(false);
            GameState.numOfIntel.set(GameState.numOfIntel.get() + 1);
            GameState.cabinetMiddleIntelfound = true;
            try {
              updateDialogue(Dialogue.INTELFOUND);
            } catch (Exception e) {
              e.printStackTrace();
            }
          });
    } else if (!GameState.isKeyFound.get() && cabinetIntelFound) {
      // user has already found cabinet intel
      updateDialogue(Dialogue.INTELALREADYFOUND);
    } else if (!GameState.isKeyFound.get() && !cabinetIntelFound) {
      // user is missing key to cabinet
      updateDialogue(Dialogue.KEYNEEDED);
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
    // when hovering the keypad, if user has solved computer password then show the unlocked keypad
    // door
    if (rectangle.getId().equals("keyPad")) {
      if (GameState.isPasswordSolved) {
        roomimage.setImage(new Image("/images/startUnlocked.png"));
      }
    }

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
