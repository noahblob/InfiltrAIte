package nz.ac.auckland.se206.controllers.left;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

/** Controller class for the room view. */
public class LeftRoomController extends Commander {

  private enum Object {
    RADIO,
    DRAWER,
    PAINT,
    PAINT1,
    PAINT2,
    DOOR,
    DESK,
    NEWS,
    HANGER,
    MORSE
  }

  @FXML private Rectangle popUpBackGround;
  @FXML private Rectangle communications;
  @FXML private Rectangle drawer;
  @FXML private Rectangle painting;
  @FXML private Rectangle painting1;
  @FXML private Rectangle hanger;
  @FXML private Polygon painting2;
  @FXML private Polygon door;
  @FXML private Polygon desk;
  @FXML private Polygon newspaper;
  @FXML private ImageView poster;
  @FXML private ImageView poster1;
  @FXML private ImageView poster2;
  @FXML private ImageView morse;
  @FXML private ImageView tear;
  @FXML private Label lastDigits;
  @FXML private Label intel;
  @FXML private Button back;

  private Map<Shape, Object> objects;
  private List<ImageView> visiblePopups;

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws Exception if there is an error loading the chat view
   */
  public void initialize() throws Exception {
    // Initialize the commander and set up all relevant items in the room
    super.initialize();
    objective.setText("I wonder where the enemy would hide their intelligence...");
    createRoom();
    setPopups();
    setHoverEvents();
    generateYear();
    updateDigitFont();
  }

  /** When door is clicked, navigate user back to main room. */
  private void clickDoor() {
    Scene currentScene = door.getScene();
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.MAIN));
  }

  /** Creates a hashmap of all objects in the room. */
  private void createRoom() {
    // Initialize all objects in the room into a hashmap
    objects = new HashMap<>();
    // This will allow us to check which object was clicked on
    objects.put(communications, Object.RADIO);
    objects.put(drawer, Object.DRAWER);
    objects.put(painting, Object.PAINT);
    objects.put(painting1, Object.PAINT1);
    objects.put(painting2, Object.PAINT2);
    objects.put(door, Object.DOOR);
    objects.put(newspaper, Object.NEWS);
    objects.put(desk, Object.DESK);
    objects.put(hanger, Object.HANGER);
    objects.put(painting2, Object.MORSE);
  }

  /** Sets opacity of objects upon user hovering them. */
  private void setHoverEvents() {
    // When a user hovers over some relevant object in the room, it should highlight yellow
    // indicating the user can interact with it, this sets that hover event up for all objects in
    // the left room
    setOpacityOnHover(door);
    setOpacityOnHover(painting);
    setOpacityOnHover(painting1);
    setOpacityOnHover(painting2);
    setOpacityOnHover(newspaper);
    setOpacityOnHover(drawer);
    setOpacityOnHover(communications);
    setOpacityOnHover(desk);
    setOpacityOnHover(hanger);
  }

  /**
   * Sets the opacity of specified shape that has been hovered.
   *
   * @param shape The shape that has been hovered.
   */
  private void setOpacityOnHover(Shape shape) {
    shape.setOnMouseEntered(event -> shape.setOpacity(0.5));
    shape.setOnMouseExited(event -> shape.setOpacity(0));
  }

  /**
   * Shows the popup of the specified image.
   *
   * @param popup The image to be shown.
   */
  private void showPopup(ImageView popup) {
    popup.setVisible(true);
    popUpBackGround.setVisible(true);
    back.setVisible(true);
    visiblePopups.add(popup);
  }

  /**
   * Toggles the visibility of the last digits of the year.
   *
   * @param flag Whether the digits should be visible or not.
   */
  private void toggleYear(Boolean flag) {
    this.lastDigits.setVisible(flag);
  }

  /** Sets up the popups for the room, all initialized as not visible. */
  private void setPopups() {
    visiblePopups = new ArrayList<>();
    popUpBackGround.setVisible(false);

    // Individual popup items.
    poster.setVisible(false);
    poster1.setVisible(false);
    poster2.setVisible(false);
    tear.setVisible(false);
    lastDigits.setVisible(false);
    morse.setVisible(false);

    back.setOnAction(
        event -> {
          for (ImageView popUp : visiblePopups) {
            popUp.setVisible(false);
          }
          Sound.getInstance().playClickMinor();
          popUpBackGround.setVisible(false);
          back.setVisible(false);
          lastDigits.setVisible(false);
        });
  }

  /** Generates the last digits of the year for locker puzzle. */
  private void generateYear() {
    // Unbind lastDigits from lastNumbers.
    lastDigits.textProperty().unbind();
    lastDigits.textProperty().bind(Bindings.convert(GameState.lastNumbers));
  }

  /** Updates the font of digits for locker. */
  private void updateDigitFont() {
    lastDigits.setStyle("-fx-font-size: 60px;");
  }

  /**
   * Handles the event of user clicking on objects in room.
   *
   * @param event The mouse event.
   * @throws Exception If the event cannot be handled.
   */
  @FXML
  public void onClick(MouseEvent event) throws Exception {

    Sound.getInstance().playClickMinor();

    // Get the scene and type of the object that was clicked on
    Shape clickedObject = (Shape) event.getSource();
    Scene currentScene = clickedObject.getScene();
    Object type = objects.get(clickedObject);

    // Add more items that can be clicked on.
    switch (type) {
      case DOOR:
        clickDoor();
        break;
      case PAINT1:
        showPopup(poster1);
        break;
      case PAINT:
        showPopup(poster);
        updateDialogue(Dialogue.TORNHINT);
        break;
      case NEWS:
        showPopup(poster2);
        break;
      case RADIO:
        if (!GameState.isSlidersSolved) {
          // update commander dialogue accordingly and move to radio fxml
          updateDialogue(Dialogue.SLIDERHINT);
          currentScene.setRoot(SceneManager.getuserInterface(AppUi.RADIO));
        } else {
          updateDialogue(Dialogue.ALREADYSOLVED);
        }
        break;
      case DESK:
        showPopup(tear);
        toggleYear(true);
        break;
      case DRAWER:
        // move to drawer fxml
        currentScene.setRoot(SceneManager.getuserInterface(AppUi.DRAWER));
        break;
      case HANGER:
        // If the key is not yet found, give player a key.
        if (!GameState.isKeyUsed.get() && !GameState.isKeyFound.get()) {
          // Find the key to main room drawer.
          GameState.isKeyFound.set(true);
          updateDialogue(Dialogue.KEYFOUND);
        } else {
          updateDialogue(Dialogue.EMPTY);
        }
        break;
      default:
        updateDialogue(Dialogue.EMPTY);
        break;
    }
  }
}
