package nz.ac.auckland.se206.controllers;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Items.CustomSliderSkin;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

/** Controller class for the room view. */
public class LeftRoomController extends Commander implements TimerObserver {

  public static int year;

  @FXML private TextArea objective;
  @FXML private ImageView room;
  @FXML private Rectangle popUpBackGround;
  @FXML private Button back, decrypt;
  @FXML
  private Rectangle communications, drawer, painting, painting1, topDrawer, midDrawer, botDrawer;
  @FXML private Polygon painting2, door, desk, newspaper;
  @FXML private ImageView p, p1, p2;
  @FXML private ImageView comms, comms1, tear, drawer1;
  @FXML private Pane sliderPane;
  @FXML private Slider s, s1, s2, s3, s4, s5;
  @FXML private Pane passcodePane;
  @FXML private Label x, x1, x2, x3, x4, x5;
  @FXML private Label lastDigits;
  @FXML private Label intel;
  @FXML private ImageView paper;
  @FXML private TextArea riddle;

  /** The key in the inventory box. It is currently set to visible. */
  @FXML private ImageView key;

  private Map<Shape, Object> objects;
  private List<ImageView> visiblePopups;
  private List<Slider> sliders;
  private List<Label> passcode;
  private Map<Integer,String> sliderMap;
  private int lastNumbers;
  private String riddleCode;

  private enum Object {
    COMMS, DRAWER, PAINT, PAINT1, PAINT2, DOOR, DESK, NEWS, BOT, MID, TOP
  }

  /**
   * Initializes the room view, it is called when the room loads.
   * @throws Exception
   */
  public void initialize() throws Exception {
    intel.textProperty().bind(Bindings.concat("x", GameState.numOfIntel.asString()));

    System.out.println(GameState.getRandomWord());

    super.initialize();
    objective.setText("This is the LEFT ROOM");

    createRoom();
    setPopups();
    setHoverEvents();
    setSliders();
    createSliderMap();
    generateYear();
    openCabinet(false);
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
    objects.put(desk, Object.DESK);
    objects.put(topDrawer, Object.TOP);
    objects.put(midDrawer, Object.MID);
    objects.put(botDrawer, Object.BOT);
  }

  private void setHoverEvents() {
    door.setOnMouseEntered(
        event -> {
          door.setOpacity(0.5);
        });
    door.setOnMouseExited(
        event -> {
          door.setOpacity(0);
        });
    painting.setOnMouseEntered(
        event -> {
          painting.setOpacity(0.5);
        });
    painting.setOnMouseExited(
        event -> {
          painting.setOpacity(0);
        });
    painting1.setOnMouseEntered(
        event -> {
          painting1.setOpacity(0.5);
        });
    painting1.setOnMouseExited(
        event -> {
          painting1.setOpacity(0);
        });
    painting2.setOnMouseEntered(
        event -> {
          painting2.setOpacity(0.5);
        });
    painting2.setOnMouseExited(
        event -> {
          painting2.setOpacity(0);
        });
    newspaper.setOnMouseEntered(
        event -> {
          newspaper.setOpacity(0.5);
        });
    newspaper.setOnMouseExited(
        event -> {
          newspaper.setOpacity(0);
        });
    drawer.setOnMouseEntered(
        event -> {
          drawer.setOpacity(0.5);
        });
    drawer.setOnMouseExited(
        event -> {
          drawer.setOpacity(0);
        });
    communications.setOnMouseEntered(
        event -> {
          communications.setOpacity(0.5);
        });
    communications.setOnMouseExited(
        event -> {
          communications.setOpacity(0);
        });
    desk.setOnMouseEntered(
        event -> {
          desk.setOpacity(0.5);
        });
    desk.setOnMouseExited(
        event -> {
          desk.setOpacity(0);
        });

    topDrawer.setOnMouseEntered(
        event -> {
          topDrawer.setOpacity(0.5);
        });
    topDrawer.setOnMouseExited(
        event -> {
          topDrawer.setOpacity(0);
        });
    midDrawer.setOnMouseEntered(
        event -> {
          midDrawer.setOpacity(0.5);
        });
    midDrawer.setOnMouseExited(
        event -> {
          midDrawer.setOpacity(0);
        });
    botDrawer.setOnMouseEntered(
        event -> {
          botDrawer.setOpacity(0.5);
        });
    botDrawer.setOnMouseExited(
        event -> {
          botDrawer.setOpacity(0);
        });
  }

  private void showPopup(ImageView popup) {
    popup.setVisible(true);
    popUpBackGround.setVisible(true);
    visiblePopups.add(popup);
    back.setVisible(true);
  }

  private void toggleSliders(Boolean flag) {
    passcodePane.setVisible(flag);
    sliderPane.setVisible(flag);
  }

  private void toggleYear(Boolean flag) {
    this.lastDigits.setVisible(flag);
  }

  private void setPopups() {
    visiblePopups = new ArrayList<>();
    popUpBackGround.setVisible(false);
    back.setVisible(false);
    decrypt.setVisible(false);
    riddleCode = generateEncrypted(100);
    riddle.appendText(riddleCode);
    riddle.setWrapText(true);

    // Individual popup items.
    p.setVisible(false);
    p1.setVisible(false);
    p2.setVisible(false);
    comms.setVisible(false);
    comms1.setVisible(false);
    tear.setVisible(false);
    lastDigits.setVisible(false);
    drawer1.setVisible(false);
    riddle.setVisible(false);
    paper.setVisible(false);

    back.setOnAction(
        event -> {
          for (ImageView popup : visiblePopups) {
            popup.setVisible(false);
          }
          visiblePopups.clear();
          back.setVisible(false);
          popUpBackGround.setVisible(false);
          // Disables sliders & last digits.
          toggleSliders(false);
          toggleYear(false);
          openCabinet(false);
          riddle.setVisible(false);
          decrypt.setVisible(false);
        });

    decrypt.setOnAction(
        event -> {
          // send the encrypted message to GPT.
          System.out.println("TEST PRESS");
          String dialogue =
              "Sir, I found a piece of paper with the following characters, what does it say? "
                  + riddleCode;
          try {
            CommanderController.getInstance().onSendMessage(event, dialogue);

            // Create a Timeline to wait for 10 seconds
            Timeline timeline =
                new Timeline(
                    new KeyFrame(
                        Duration.millis(10000),
                        ae -> {
                          try {
                            CommanderController.getInstance().sendForUser(GptPromptEngineering.getRiddleWithGivenWord(GameState.getRandomWord()));
                          } catch (Exception e) {
                            e.printStackTrace();
                          }
                        }));

            // Play the Timeline
            timeline.play();

          } catch (Exception e) {
            e.printStackTrace();
          }
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

  private void setSliders() {
    toggleSliders(false);
    sliders = new ArrayList<>();
    sliders.add(s);
    sliders.add(s1);
    sliders.add(s2);
    sliders.add(s3);
    sliders.add(s4);
    sliders.add(s5);
    passcode = new ArrayList<>();
    passcode.add(x);
    passcode.add(x1);
    passcode.add(x2);
    passcode.add(x3);
    passcode.add(x4);
    passcode.add(x5);

    for (int i = 0; i < sliders.size(); i++) {
      Slider s = sliders.get(i);
      Label digit = passcode.get(i);

        // Set the major and minor tick unit values to 1
        s.setMajorTickUnit(1);
        s.setMinorTickCount(0);
        s.setBlockIncrement(1);

        // Enable snapping to tick marks
        s.setSnapToTicks(true);

        s.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Convert the value to an integer
                int intValue = newValue.intValue();
                s.setValue(intValue); // Update the slider value to the nearest integer

                // Get the respective character from slider map.
                String code = sliderMap.get(intValue);
                // Update respective label.
                digit.setText(code);
            }
        });

      // s.setSkin(new CustomSliderSkin(s));
    }
  }

  private void createSliderMap() {
    sliderMap = new HashMap<>();
    sliderMap.put(0,"!");
    sliderMap.put(1,"+");
    sliderMap.put(2,"-");
    sliderMap.put(3,"*");
    sliderMap.put(4,"&");
    sliderMap.put(5,"^");
    sliderMap.put(6,"%");
    sliderMap.put(7,"$");
    sliderMap.put(8,"#");
    sliderMap.put(9,"@");
    sliderMap.put(10,"?");
  }

  private void openCabinet(Boolean flag) {
    topDrawer.setVisible(flag);
    midDrawer.setVisible(flag);
    botDrawer.setVisible(flag);
  }

  @FXML
  public void onClick(MouseEvent event) {

    Shape clickedObject = (Shape) event.getSource();
    Object type = objects.get(clickedObject);

    // Add more items that can be clicked on.
    switch (type) {
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
        break;
      case DESK:
        showPopup(tear);
        toggleYear(true);
        break;
      case DRAWER:
        showPopup(drawer1);
        openCabinet(true);
        break;
      case MID:
        showPopup(paper);
        riddle.setVisible(true);
        decrypt.setVisible(true);
      default:
        break;
    }
  }

  private String generateEncrypted(int length) {

    String asciiChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    SecureRandom random = new SecureRandom();
    StringBuilder encryptedText = new StringBuilder();
    int count = 0;
    for (int i = 0; i < length; i++) {
      if (count > 0 && random.nextInt(10) < 2) { // 20% chance to insert a space
        encryptedText.append(' ');
        count = 0;
        continue;
      }
      int index = random.nextInt(asciiChars.length());
      encryptedText.append(asciiChars.charAt(index));
      count++;
    }
    return encryptedText.toString();
  }
}
