package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javafx.beans.property.SimpleIntegerProperty;

/** Represents the state of the game. */
public class GameState {

  private static final HashMap<String, String> dictionary = new HashMap<>();

  // Create riddle answers
  static {
    // Decide what type of riddle answers we want later.
    dictionary.put("apple", "A round fruit with red or green skin and crisp flesh.");
    dictionary.put("banana", "A yellow fruit that is long and curved.");
    dictionary.put("cherry", "A small, round fruit that is typically red or black.");
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

  /** Indicates whether the player has found intel in the cabinet */
  public static boolean cabinetIntelfound = false;

  public static String getRandomWord() {
    // Create an ArrayList to hold the keys (words)
    ArrayList<String> keys = new ArrayList<>(dictionary.keySet());

    // Create a Random object
    Random random = new Random();

    // Get a random index from 0 to size-1
    int randomIndex = random.nextInt(keys.size());

    // Return a random word from the ArrayList of keys
    return keys.get(randomIndex);
  }
}
