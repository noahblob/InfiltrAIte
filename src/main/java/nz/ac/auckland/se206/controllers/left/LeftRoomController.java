package nz.ac.auckland.se206.controllers.left;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

  public static int year;

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

  /** The key in the inventory box. It is currently set to visible. */
  private Map<Shape, Object> objects;

  private List<ImageView> visiblePopups;
  private int lastNumbers;
  private boolean isRadioOpened = false;

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws Exception
   */
  public void initialize() throws Exception {

    super.initialize();
    objective.setText("I wonder where the enemy would hide their intelligence...");
    createRoom();
    setPopups();
    setHoverEvents();
    generateYear();
  }

  private void clickDoor() {
    Scene currentScene = door.getScene();
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.MAIN));
  }

  private void createRoom() {
    objects = new HashMap<>();
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

  private void setHoverEvents() {
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

  private void setOpacityOnHover(Shape shape) {
    shape.setOnMouseEntered(event -> shape.setOpacity(0.5));
    shape.setOnMouseExited(event -> shape.setOpacity(0));
  }

  private void showPopup(ImageView popup) {
    popup.setVisible(true);
    popUpBackGround.setVisible(true);
    back.setVisible(true);
    visiblePopups.add(popup);
  }

  private void toggleYear(Boolean flag) {
    this.lastDigits.setVisible(flag);
  }

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
          popUpBackGround.setVisible(false);
          back.setVisible(false);
          lastDigits.setVisible(false);
        });
  }

  private void generateYear() {

    Random random = new Random();
    lastNumbers = random.nextInt(41) + 20; // Generates number between 20 and 60

    // Update the year passcode: 19XX
    LeftRoomController.year = 1900 + lastNumbers;
    // Update the tear piece to show the last digits.
    lastDigits.setText(String.valueOf(lastNumbers));
  }

  @FXML
  public void onClick(MouseEvent event) throws Exception {

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
        if (!isRadioOpened) {
          updateDialogue(Dialogue.SLIDERHINT);
          isRadioOpened = true;
        }
        currentScene.setRoot(SceneManager.getuserInterface(AppUi.RADIO));
        break;
      case DESK:
        showPopup(tear);
        toggleYear(true);
        break;
      case DRAWER:
        currentScene.setRoot(SceneManager.getuserInterface(AppUi.DRAWER));
        break;
      case HANGER:
        // Find the key to main room drawer.
        GameState.doePlayerHaveKey.set(true);
        updateDialogue(Dialogue.KEYFOUND);
        hanger.setDisable(true);
        break;
      case MORSE:
        showPopup(morse);
      break;
      default:
        updateDialogue(Dialogue.EMPTY);
        break;
    }
  }
}
