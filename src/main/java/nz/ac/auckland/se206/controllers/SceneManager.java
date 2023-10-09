package nz.ac.auckland.se206.controllers;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Parent;

/** Class to manage the scenes in the application. */
public class SceneManager {

  /** Enum to store all relevant Ui elements in application. */
  public enum AppUi {
    UI,
    START,
    TITLE,
    WATCH,
    MAIN,
    LEFT,
    RADIO,
    DRAWER,
    RIGHT,
    LOCKER,
    KEYPAD,
    COMPUTER,
    BOOKSHELF,
    POPUP,
    BLACKBOARD,
    END
  }

  private static AppUi currentScene = null;

  public static Map<AppUi, Parent> sceneMap = new HashMap<>();

  /**
   * Add a user interface to the scene map.
   *
   * @param appUserInterface The user interface to add
   * @param root The root of the user interface
   */
  public static void addUserInterface(AppUi appUserInterface, Parent root) {
    sceneMap.put(appUserInterface, root);
  }

  /**
   * Get the user interface from the scene map.
   *
   * @param ui The user interface to get
   * @return The root of the user interface
   */
  public static Parent getuserInterface(AppUi ui) {
    currentScene = ui;
    return sceneMap.get(ui);
  }

  /**
   * Get the current scene root.
   *
   * @return The current scene root
   */
  public static Parent getCurrentSceneRoot() {
    return sceneMap.get(currentScene);
  }

  /**
   * Get the relevant room from the scene map.
   *
   * @param ui The room to get
   * @return The root of the room
   */
  public static Parent getRoom(AppUi ui) {
    return sceneMap.get(ui);
  }
}
