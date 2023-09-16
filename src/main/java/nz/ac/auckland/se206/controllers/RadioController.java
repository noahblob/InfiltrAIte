package nz.ac.auckland.se206.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
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
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

/** Controller class for the room view. */
public class RadioController extends Commander implements TimerObserver {

  public static int year;

  @FXML private TextArea objective;
  @FXML private Button back;
  @FXML private ImageView comms;
  @FXML private ImageView comms1;
  @FXML private Pane sliderPane;
  @FXML private Slider slider;
  @FXML private Slider slider1;
  @FXML private Slider slider2;
  @FXML private Slider slider3;
  @FXML private Slider slider4;
  @FXML private Slider slider5;
  @FXML private Pane passcodePane;
  @FXML private Label label;
  @FXML private Label label1;
  @FXML private Label label2;
  @FXML private Label label3;
  @FXML private Label label4;
  @FXML private Label label5;

  /** The key in the inventory box. It is currently set to visible. */
  @FXML private ImageView key;

  private List<Slider> sliders;
  private List<Label> passcode;
  private char[] code;
  private char[] answer;
  private Map<Integer, Character> sliderMap;
  private boolean isDialogueUpdated;

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws Exception
   */
  public void initialize() throws Exception {

    System.out.println(GameState.getRandomWord());
    System.out.println(String.valueOf(GameState.setSliders()));

    super.initialize();
    objective.setText("Hmm I wonder what this does...");

    comms1.setVisible(false);
    answer = GameState.setSliders();
    isDialogueUpdated = false;
    setSliders();
    createSliderMap();
    TimerClass.add(this);
  }

  @Override
  public void timerStart() {
    TimerClass timerText = TimerClass.getInstance();
    timer.setText(timerText.getTimerLeft());
  }

  @FXML
  public void onReturn(MouseEvent event) {
    Button button = (Button) event.getSource();
    Scene currentScene = button.getScene();
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.LEFT));
    System.out.println("switched to left");
  }

  private void setSliders() {
    code = new char[6];
    sliders = List.of(slider, slider1, slider2, slider3, slider4, slider5);
    passcode = List.of(label, label1, label2, label3, label4, label5);

    IntStream.range(0, sliders.size()).forEach(i -> setSlider(sliders.get(i), passcode.get(i), i));
  }

  private void createSliderMap() {
    sliderMap = new HashMap<>();
    sliderMap.put(0, '!');
    sliderMap.put(1, '+');
    sliderMap.put(2, '-');
    sliderMap.put(3, '*');
    sliderMap.put(4, '&');
    sliderMap.put(5, '^');
    sliderMap.put(6, '%');
    sliderMap.put(7, '$');
    sliderMap.put(8, '#');
    sliderMap.put(9, '@');
    sliderMap.put(10, '?');
  }

  // Helper function for sliders.
  private void setSlider(Slider s, Label digit, int index) {
    s.setMajorTickUnit(1);
    s.setMinorTickCount(0);
    s.setBlockIncrement(1);
    s.setSnapToTicks(true);

    s.valueProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int intValue = newValue.intValue();
                s.setValue(intValue); // Update the slider value to the nearest integer

                // Get the respective character from slider map.
                char codeValue = sliderMap.get(intValue);

                // Update the actual code array.
                code[index] = codeValue;
                // Update respective label.
                digit.setText(String.valueOf(codeValue));

                try {
                  checkSlidersSolved();
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            });
  }

  private void checkSlidersSolved() throws Exception {
    if (Arrays.equals(code, answer) && !isDialogueUpdated) {
      isDialogueUpdated = true;
      // Update game state and show sine wave.
      GameState.isSlidersSolved = true;
      comms1.setVisible(true);
      // Disable slider game.
      for (Slider slider : sliders) {
        slider.setDisable(true);
      }

      CommanderController.getInstance().updateDialogueBox(Dialogue.CABINETUNLOCK.toString());
    }
  }

}
