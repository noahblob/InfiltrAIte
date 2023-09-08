package nz.ac.auckland.se206;

import javafx.beans.property.SimpleIntegerProperty;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKeyFound = false;

  /** Indicates the difficulty level of the game, 1 for EASY, 2 for MEDIUM and 3 for HARD */
  public static int difficulty = 0;

  /** Indicates current country we are infiltrating */
  public static String country = null;

  /** Indicates amount of intelligence gathered */
  public static SimpleIntegerProperty numOfIntel = new SimpleIntegerProperty(0);
}
