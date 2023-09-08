package nz.ac.auckland.se206.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Controller class for the room view. */
public class BlackBoardController extends Commander implements TimerObserver {

  @FXML private Label objective;
  @FXML private Text timer;
  @FXML private Label mon;
  @FXML private Label tues;
  @FXML private Label wed;
  @FXML private Label th;
  @FXML private Label fr;
  @FXML private Label sat;
  @FXML private Label sun;

  int monday;
  int tuesday;
  int wednesday;
  int thursday;
  int friday;
  int saturday;
  int sunday;
  int day;
  int ans;
  LocalDate currentDate;

  Map<Integer, Integer> hashmap = new HashMap<>();

  Random random = new Random();

  Font chalkFont =
      Font.loadFont(getClass().getResource("/fonts/Chalkduster.ttf").toExternalForm(), 12);

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws ApiProxyException
   */
  public void initialize() throws ApiProxyException {
    super.initialize();
    objective.setText("Hmm this seems pretty important");
    TimerClass.add(this);
    findDate();
  }

  @Override
  public void timerStart() {
    TimerClass timerText = TimerClass.getInstance();
    timer.setText(timerText.getTimerLeft());
  }

  /**
   * Find the local date from the user and update
   *
   * @param event
   */
  private void findDate() {

    currentDate = LocalDate.now();
    day = currentDate.getDayOfWeek().getValue();

    monday = 100 + random.nextInt(900);
    tuesday = 100 + random.nextInt(900);
    wednesday = 100 + random.nextInt(900);
    thursday = 100 + random.nextInt(900);
    friday = 100 + random.nextInt(900);
    saturday = 100 + random.nextInt(900);
    sunday = 100 + random.nextInt(900);

    hashmap.put(1, monday);
    hashmap.put(2, tuesday);
    hashmap.put(3, wednesday);
    hashmap.put(4, thursday);
    hashmap.put(5, friday);
    hashmap.put(6, saturday);
    hashmap.put(7, sunday);

    mon.setText(Integer.toString(monday));
    tues.setText(Integer.toString(tuesday));
    wed.setText(Integer.toString(wednesday));
    th.setText(Integer.toString(thursday));
    fr.setText(Integer.toString(friday));
    sat.setText(Integer.toString(saturday));
    sun.setText(Integer.toString(sunday));
  }

  /**
   * Returns the correct answer for keypad
   *
   * @return Correct answer for keypad
   */
  public int getKeypadAns() {
    return hashmap.get(day);
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
