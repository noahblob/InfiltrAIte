package nz.ac.auckland.se206.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;

/** Controller class for the room view. */
public class BlackBoardController extends Commander implements TimerObserver {

  @FXML private TextArea objective;
  @FXML private Text timer;
  @FXML private Label mon;
  @FXML private Label tues;
  @FXML private Label wed;
  @FXML private Label th;
  @FXML private Label fr;
  @FXML private Label sat;
  @FXML private Label sun;
  @FXML private Label intel;

  private int monday;
  private int tuesday;
  private int wednesday;
  private int thursday;
  private int friday;
  private int saturday;
  private int sunday;
  private int day;
  private static int ans;
  private LocalDate currentDate;

  Map<Integer, Integer> hashmap = new HashMap<>();

  Random random = new Random();

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws Exception
   */
  public void initialize() throws Exception {
    Font.loadFont(getClass().getResourceAsStream("/fonts/Chalkduster.ttf"), 12);

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

    mon.setStyle("-fx-font-family: 'Chalkduster'; -fx-font-size: 12px;");
    tues.setStyle("-fx-font-family: 'Chalkduster'; -fx-font-size: 12px;");
    wed.setStyle("-fx-font-family: 'Chalkduster'; -fx-font-size: 12px;");
    th.setStyle("-fx-font-family: 'Chalkduster'; -fx-font-size: 12px;");
    fr.setStyle("-fx-font-family: 'Chalkduster'; -fx-font-size: 12px;");
    sat.setStyle("-fx-font-family: 'Chalkduster'; -fx-font-size: 12px;");
    sun.setStyle("-fx-font-family: 'Chalkduster'; -fx-font-size: 12px;");

    mon.setText(Integer.toString(monday));
    tues.setText(Integer.toString(tuesday));
    wed.setText(Integer.toString(wednesday));
    th.setText(Integer.toString(thursday));
    fr.setText(Integer.toString(friday));
    sat.setText(Integer.toString(saturday));
    sun.setText(Integer.toString(sunday));

    ans = hashmap.get(day);
  }

  /**
   * Returns the correct answer for keypad
   *
   * @return Correct answer for keypad
   */
  public static int getKeypadAns() {
    return ans;
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
