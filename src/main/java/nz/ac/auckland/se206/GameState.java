package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

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

  /** Indicates current country we are infiltrating */
  public static String country = null;

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
  }

  /** Method to create random riddle for current game. */
  public static String getRandomWord(Set<String> set) {
    // Create an ArrayList to hold the keys (words)
    ArrayList<String> keys = new ArrayList<>(set);

    // Create a Random object
    Random random = new Random();

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

  public static void resetGameState() {
  // Reset all game state variables to default values for when the player restarts the game.
  
  isRiddleResolved = false;
  isKeyFound.set(false);
  isKeyUsed.set(false);
  isKeypadSolved.set(false);
  isGameWon.set(false);

  difficulty.set(0); 
  numOfIntel.set(0);
  numHints.set(0);

  country = null;
  cabinetRightIntelfound = false;
  cabinetMiddleIntelfound = false;
  isSlidersSolved = false;
  isPasswordSolved = false;

  puzzleWord = getRandomWord(riddleSetOne);
  mainRiddleAnswer = getRandomWord(riddleSetTwo);
  sliderAnswer = setSliders(); 
}

}
