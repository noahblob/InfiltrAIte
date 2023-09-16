package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/** Represents the state of the game. */
public class GameState {

  private static final Set<String> riddleAnswers = new HashSet<>();

  // Create riddle answers
  static {
    // Decide what type of riddle answers we want later.
    riddleAnswers.add("apple");
    riddleAnswers.add("pear");
    riddleAnswers.add("banana");
    riddleAnswers.add("book");
    // Add more words and definitions here

    // setUp listeners to check if game is won or not.
    setupWinListeners();
  }

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether game has been won or not. */
  public static BooleanProperty isGameWon = new SimpleBooleanProperty(false);

  /** Indicates whether the key to the cabinet has been found. */
  public static BooleanProperty isKeyFound = new SimpleBooleanProperty(false);

  /** Indicates whether the keypad has had the correct digits input. */
  public static boolean isKeypadSolved = false;

  /** Indicates the difficulty level of the game, 1 for EASY, 2 for MEDIUM and 3 for HARD. */
  public static int difficulty = 0;

  /** Indicates current country we are infiltrating */
  public static String country = null;

  /** Indicates amount of intelligence gathered */
  public static SimpleIntegerProperty numOfIntel = new SimpleIntegerProperty(0);

  /** Indeicates the number of hints allowed. */
  public static String numHints = "0";

  /** Indicates whether the player has found intel in the cabinet. */
  public static boolean cabinetIntelfound = false;

  /** Indicates whether the player has solved communication puzzle. */
  public static boolean isSlidersSolved = false;

  /** Indicates the answer to the riddle for the current game.*/ 
  public static String riddleAnswer = "";
  
  /** Method to create random riddle for current game. */ 
  public static String getRandomWord() {
    // Create an ArrayList to hold the keys (words)
    ArrayList<String> keys = new ArrayList<>(riddleAnswers);

    // Create a Random object
    Random random = new Random();

    // Get a random index from 0 to size-1
    int randomIndex = random.nextInt(keys.size());

    // Update the answer to the riddle for current game, and return.
    GameState.riddleAnswer = keys.get(randomIndex);
    return GameState.riddleAnswer;
  }

  private static void setupWinListeners() {
      if (numOfIntel == null) {
          numOfIntel = new SimpleIntegerProperty(0);
      }
      // Listen for changes to numOfIntel
      numOfIntel.addListener(new ChangeListener<Number>() {
          @Override
          public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
              checkIsGameWon();
          }
      });
  }

  // Method to update if game has been won
  public static void setKeypadSolved(boolean solved) {
      isKeypadSolved = solved;
      checkIsGameWon();
  }

  private static void checkIsGameWon() {
    isGameWon.set(isKeypadSolved && numOfIntel.get() == 3);
  }
}
