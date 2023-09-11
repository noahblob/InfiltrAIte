package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javafx.beans.property.SimpleIntegerProperty;

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
  }

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key to the cabinet has been found. */
  public static boolean isKeyFound = true;

  /** Indicates whether the keypad has had the correct digits input */
  public static boolean isKeypadSolved = false;

  /** Indicates the difficulty level of the game, 1 for EASY, 2 for MEDIUM and 3 for HARD */
  public static int difficulty = 0;

  /** Indicates current country we are infiltrating */
  public static String country = null;

  /** Indicates amount of intelligence gathered */
  public static SimpleIntegerProperty numOfIntel = new SimpleIntegerProperty(0);


  /** Indeicates the number of hints allowed */
  public static String numHints = "0";

  /** Indicates whether the player has found intel in the cabinet */
  public static boolean cabinetIntelfound = false;

  public static String getRandomWord() {
    // Create an ArrayList to hold the keys (words)
    ArrayList<String> keys = new ArrayList<>(riddleAnswers);

    // Create a Random object
    Random random = new Random();

    // Get a random index from 0 to size-1
    int randomIndex = random.nextInt(keys.size());

    // Return a random word from the ArrayList of keys
    return keys.get(randomIndex);
  }
}
