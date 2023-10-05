package nz.ac.auckland.se206.controllers.right;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

/** Controller class for the room view. */
public class BlackBoardController extends Commander {

  private static int ans;
  public static BlackBoardController instance;

  public static BlackBoardController getInstance() {
    return instance;
  }

  /**
   * Returns the correct answer for keypad
   *
   * @return Correct answer for keypad
   */
  public static int getKeypadAns() {
    return ans;
  }

  public static Map<Integer, Integer> randomizeNumbers() {
    // Create a new random variable that is used to calculate random keypad codes to put on
    // blackboard
    Random random = new Random();

    Map<Integer, Integer> resultMap = new HashMap<>();

    // put all random values into a hashmap to be accessed later
    resultMap.put(1, 100 + random.nextInt(900));
    resultMap.put(2, 100 + random.nextInt(900));
    resultMap.put(3, 100 + random.nextInt(900));
    resultMap.put(4, 100 + random.nextInt(900));
    resultMap.put(5, 100 + random.nextInt(900));
    resultMap.put(6, 100 + random.nextInt(900));
    resultMap.put(7, 100 + random.nextInt(900));

    return resultMap;
  }

  @FXML private Label mon;
  @FXML private Label tues;
  @FXML private Label wed;
  @FXML private Label th;
  @FXML private Label fr;
  @FXML private Label sat;
  @FXML private Label sun;

  private int monday;
  private int tuesday;
  private int wednesday;
  private int thursday;
  private int friday;
  private int saturday;
  private int sunday;
  private int day;
  private LocalDate currentDate;

  private Map<Integer, Integer> hashmap = new HashMap<>();

  public BlackBoardController() {
    instance = this;
  }

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws Exception
   */
  public void initialize() throws Exception {
    super.initialize();
    objective.setText("Hmm this seems pretty important");

    findDate();
  }

  private void findDate() {

    // Get the system date, then get the current day of the week.
    currentDate = LocalDate.now();
    day = currentDate.getDayOfWeek().getValue();

    hashmap = randomizeNumbers();

    monday = hashmap.get(1);
    tuesday = hashmap.get(2);
    wednesday = hashmap.get(3);
    thursday = hashmap.get(4);
    friday = hashmap.get(5);
    saturday = hashmap.get(6);
    sunday = hashmap.get(7);

    // Update the blackboard.
    mon.setText(Integer.toString(monday));
    tues.setText(Integer.toString(tuesday));
    wed.setText(Integer.toString(wednesday));
    th.setText(Integer.toString(thursday));
    fr.setText(Integer.toString(friday));
    sat.setText(Integer.toString(saturday));
    sun.setText(Integer.toString(sunday));

    ans = hashmap.get(day);
  }

  public void refreshBoard() {
    randomizeNumbers();
    findDate();
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
    Sound.getInstance().playClickMinor();
    // Update the scene to the right room
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.RIGHT));
  }
}
