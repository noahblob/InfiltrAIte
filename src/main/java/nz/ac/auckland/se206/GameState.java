package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.controllers.CommanderController;
import nz.ac.auckland.se206.controllers.left.RadioController;
import nz.ac.auckland.se206.controllers.main.ComputerController;
import nz.ac.auckland.se206.controllers.main.MainRoomController;
import nz.ac.auckland.se206.controllers.right.BlackBoardController;
import nz.ac.auckland.se206.controllers.right.BookController;
import nz.ac.auckland.se206.controllers.right.LockerController;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Represents the state of the game. */
public class GameState {

  private static final Set<String> riddleSetOne = new HashSet<>();
  private static final Set<String> riddleSetTwo = new HashSet<>();
  private static char[] sliderAnswer = null;

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key to the cabinet has been found. */
  public static BooleanProperty isKeyFound = new SimpleBooleanProperty(false);

  /** Indicates whether the key to the cabinet has been used. */
  public static BooleanProperty isKeyUsed = new SimpleBooleanProperty(false);

  /** Indicates whether the keypad has had the correct digits input. */
  public static BooleanProperty isKeypadSolved = new SimpleBooleanProperty(false);

  /** Indicates whether game has been won or not. */
  public static BooleanProperty isGameWon = new SimpleBooleanProperty(false);

  /** Indicates the difficulty level of the game, 1 for EASY, 2 for MEDIUM and 3 for HARD. */
  public static IntegerProperty difficulty = new SimpleIntegerProperty();

  /** Indicates amount of intelligence gathered */
  public static SimpleIntegerProperty numOfIntel = new SimpleIntegerProperty(0);

  /** Indeicates the number of hints allowed. */
  public static SimpleIntegerProperty numHints = new SimpleIntegerProperty(0);

  /** Indicates whether the player has found intel in the right room cabinet. */
  public static boolean cabinetRightIntelfound = false;

  /** Indicates whether the player has found intel in the middle room cabinet. */
  public static boolean cabinetMiddleIntelfound = false;

  /** Indicates whether the player has solved communication puzzle. */
  public static boolean isSlidersSolved = false;

  /**
   * Indicates whether the player has solved the password to the computer, giving them access to the
   * keypad.
   */
  public static boolean isPasswordSolved = false;

  /** Indicates the answer to the left room riddle for the current game. */
  public static String puzzleWord = "";

  /** Indicates the answer to the computer riddle for the current game. */
  public static String mainRiddleAnswer = "";

  /** Indicates the last numbers of the year for the current game. */
  public static SimpleIntegerProperty lastNumbers = new SimpleIntegerProperty(0);

  /** Indicates if player is on end Screen */
  public static BooleanProperty isEndScreen = new SimpleBooleanProperty(false);

  private static Random random = new Random();

  // Create riddle answers for drawer.
  static {
    // Decide what type of riddle answers we want later.
    riddleSetOne.add("apple");
    riddleSetOne.add("pear");
    riddleSetOne.add("banana");
    riddleSetOne.add("book");
    riddleSetOne.add("cake");
    riddleSetOne.add("riddlers");
    riddleSetOne.add("aiishere");

    // Create riddle set for computer.
    riddleSetTwo.add("clock");
    riddleSetTwo.add("potato");
    riddleSetTwo.add("cheese");
    // Add more words.

    // Update GameState:
    GameState.puzzleWord = getRandomWord(riddleSetOne);
    System.out.println(GameState.puzzleWord);

    GameState.mainRiddleAnswer = getRandomWord(riddleSetTwo);
    System.out.println(GameState.mainRiddleAnswer);

    // setUp listeners to check if game is won or not.
    setupWinListeners();
    // Generate random year for left room puzzle.
    generateYear();
  }

  /** Method to create random riddle for current game. */
  public static String getRandomWord(Set<String> set) {
    // Create an ArrayList to hold the keys (words)
    ArrayList<String> keys = new ArrayList<>(set);

    // Get a random index from 0 to size-1
    int randomIndex = random.nextInt(keys.size());

    // Update the answer to the riddle for current game, and return.
    String riddle = keys.get(randomIndex);
    return riddle;
  }

  /** Method to create random slider combination for the current game */
  public static char[] setSliders() {
    if (sliderAnswer != null) {
      return sliderAnswer;
    }
    // Create an array of chars to hold the slider answer
    char[] answer = new char[6];
    Random random = new Random();
    // Possible char[] list of answer;
    char[] symbols = {'+', '-', '*', '^', '%', '$', '#', '@', '?'};

    for (int i = 0; i < 6; i++) {
      int index = random.nextInt(symbols.length);
      answer[i] = symbols[index];
    }
    sliderAnswer = answer;
    return answer;
  }

  public static void resetGame() throws Exception {
    resetGameState();
    resetTimer();
    resetCommander();
    resetMainRoom();
    resetLeftRoom();
    resetRightRoom();
  }

  // Add listeners to isKeyPadSolved and numOfIntel to update isGameWon.
  private static void setupWinListeners() {
    // Listen for changes to isKeyPadSolved
    isKeypadSolved.addListener((observable, oldValue, newValue) -> checkIsGameWon());

    // Listen for changes to numOfIntel
    numOfIntel.addListener((observable, oldValue, newValue) -> checkIsGameWon());
  }

  // Method to update if game has been won
  private static void checkIsGameWon() {
    isGameWon.set(isKeypadSolved.get() && numOfIntel.get() >= 1);
  }

  // Method to generate a new Year number for the current game.
  private static void generateYear() {
    lastNumbers.set(random.nextInt(41) + 20); // Generates number between 20 and 60
  }

  private static void resetGameState() {
    // Reset all game state variables to default values for when the player restarts the game.

    isRiddleResolved = false;
    isKeyFound.set(false);
    isKeyUsed.set(false);
    isKeypadSolved.set(false);
    isGameWon.set(false);

    difficulty.set(0);
    numOfIntel.set(0);
    numHints.set(0);

    cabinetRightIntelfound = false;
    cabinetMiddleIntelfound = false;
    isSlidersSolved = false;
    isPasswordSolved = false;

    puzzleWord = getRandomWord(riddleSetOne);
    mainRiddleAnswer = getRandomWord(riddleSetTwo);
    sliderAnswer = setSliders();
    lastNumbers.set(random.nextInt(41) + 20);
    isEndScreen.set(false);
  }

  private static void resetTimer() {
    // get the list of timers before resetting.
    List<Text> timers = TimerClass.getTimers();
    // Reset the timer.
    TimerClass.resetInstance();
    // Create a new Timer.
    TimerClass.initialize();
    // Update new Timer with room labels.
    TimerClass.setTimers(timers);
  }

  private static void resetCommander() throws Exception {

    CommanderController instance = CommanderController.getInstance();

    // Clear the notes and phones of previous game.
    instance.clearNotes();
    instance.clearPhones();

    List<TextArea> dialogues = instance.getDialogues();
    List<ListView<ChatMessage>> phoneScreens = instance.getPhoneScreens();
    // Reset the game master.
    CommanderController.resetInstance();

    // Update the new Commander controller with the list of dialogues and phonescreens.
    CommanderController.getInstance().setDialogues(dialogues);
    CommanderController.getInstance().setPhoneScreens(phoneScreens);
  }

  private static void resetMainRoom() throws ApiProxyException {
    // change computer riddle for next user playthrough
    ComputerController.getInstance()
        .runGpt(
            new ChatMessage(
                "system", GptPromptEngineering.getPasswordRiddle(GameState.mainRiddleAnswer)));

    // Reset main room back to original imageview
    MainRoomController.getInstance().resetRoom();
  }

  private static void resetLeftRoom() {
    RadioController.getInstance().setSliders();
  }

  private static void resetRightRoom() {
    // Reset BlackBoard Numbers
    BlackBoardController.getInstance().refreshBoard();

    // Reset Book order in bookshelf
    BookController.getInstance().resetFont();
    BookController.getInstance().setupContent();

    // Reset Right Room Locker
    LockerController.getInstance().resetRoom();
  }
}
