package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.Items.CustomSliderSkin;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Controller class for the room view. */
public class LeftRoomController extends Commander implements TimerObserver {

  @FXML private TextArea objective;
  @FXML private Text timer;
  @FXML private ImageView room;
  @FXML private Rectangle popUpBackGround;
  @FXML private Button back;
  @FXML private Rectangle communications;
  @FXML private Rectangle drawer;
  @FXML private Rectangle painting;
  @FXML private Rectangle painting1;
  @FXML private Polygon painting2;
  @FXML private Polygon door;
  @FXML private Polygon newspaper;
  @FXML private ImageView p;
  @FXML private ImageView p1;
  @FXML private ImageView p2;
  @FXML private ImageView comms;
  @FXML private ImageView comms1;
  @FXML private Slider s;
  @FXML private Slider s1;
  @FXML private Slider s2;
  @FXML private Slider s3;
  @FXML private Slider s4;
  @FXML private Slider s5;

  private Map<Shape, Object> objects;
  private List<ImageView> visiblePopups;
  private List<Slider> sliders;


  private enum Object {
    COMMS,
    DRAWER,
    PAINT,
    PAINT1,
    PAINT2,
    DOOR,
    NEWS
  }

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws ApiProxyException
   */
  public void initialize() throws ApiProxyException {
    super.initialize();
    objective.setText("This is the LEFT ROOM");
    
    createRoom();
    setPopups();
    setHoverEvents();
    setSliders();

    TimerClass.add(this);
    
  }

  @Override
  public void timerStart() {
    TimerClass timerText = TimerClass.getInstance();
    timer.setText(timerText.getTimerLeft());
  }

  private void clickDoor() {
    Scene currentScene = door.getScene();
    currentScene.setRoot(SceneManager.getuserInterface(AppUI.MAIN));
  }

  private void createRoom() {
    objects = new HashMap<>();
    objects.put(communications, Object.COMMS);
    objects.put(drawer, Object.DRAWER);
    objects.put(painting, Object.PAINT);
    objects.put(painting1, Object.PAINT1);
    objects.put(painting2, Object.PAINT2);
    objects.put(door, Object.DOOR);
    objects.put(newspaper, Object.NEWS);
  }

  private void setHoverEvents() {
    door.setOnMouseEntered(event -> { door.setOpacity(0.5);});
    door.setOnMouseExited(event -> { door.setOpacity(0);});
    painting.setOnMouseEntered(event -> { painting.setOpacity(0.5);});
    painting.setOnMouseExited(event -> { painting.setOpacity(0);});
    painting1.setOnMouseEntered(event -> { painting1.setOpacity(0.5);});
    painting1.setOnMouseExited(event -> { painting1.setOpacity(0);});
    painting2.setOnMouseEntered(event -> { painting2.setOpacity(0.5);});
    painting2.setOnMouseExited(event -> { painting2.setOpacity(0);});
    newspaper.setOnMouseEntered(event -> { newspaper.setOpacity(0.5);});
    newspaper.setOnMouseExited(event -> { newspaper.setOpacity(0);});
    drawer.setOnMouseEntered(event -> { drawer.setOpacity(0.5);});
    drawer.setOnMouseExited(event -> { drawer.setOpacity(0);});
    communications.setOnMouseEntered(event -> { communications.setOpacity(0.5);});
    communications.setOnMouseExited(event -> { communications.setOpacity(0);});
  }

  private void showPopup(ImageView popup) {
      popup.setVisible(true);
      popUpBackGround.setVisible(true);
      visiblePopups.add(popup);
      back.setVisible(true);
  }

  private void toggleSliders(Boolean flag) {
    for (Slider s : sliders) {
      s.setVisible(flag);
    }
  }

  private void setPopups() {
    visiblePopups = new ArrayList<>();
    popUpBackGround.setVisible(false);
    back.setVisible(false);

    // Individual popup items.
    p.setVisible(false);
    p1.setVisible(false);
    p2.setVisible(false);
    comms.setVisible(false);
    comms1.setVisible(false);
   

    back.setOnAction(event -> {
        for (ImageView popup : visiblePopups) {
            popup.setVisible(false);
        }
        visiblePopups.clear();
        back.setVisible(false);
        popUpBackGround.setVisible(false);
        // Disables sliders.
        toggleSliders(false);
    });
  }

  private void setSliders() {
    this.sliders = new ArrayList<>();
    sliders.add(s);
    sliders.add(s1);
    sliders.add(s2);
    sliders.add(s3);
    sliders.add(s4);
    sliders.add(s5);

    for (int i = 0; i < sliders.size(); i++) {
      Slider s = sliders.get(i);
      s.setVisible(false);
      s.setSkin(new CustomSliderSkin(s));
    }
  }

  @FXML public void onClick(MouseEvent event) {

    Shape clickedObject = (Shape) event.getSource();
    Object type = objects.get(clickedObject);

    // Add more items that can be clicked on.
    switch(type) {
      case DOOR:
        clickDoor();
        break;
        case PAINT1:
        showPopup(p1);
        break;
        case PAINT:
        showPopup(p);
        break;
        case NEWS:
        showPopup(p2);
        break;
        case COMMS:
        showPopup(comms);
        showPopup(comms1);
        toggleSliders(true);
      default:
        break;
    }

  }

  

}
