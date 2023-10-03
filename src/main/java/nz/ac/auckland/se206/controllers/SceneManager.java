package nz.ac.auckland.se206.controllers;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Parent;

public class SceneManager {

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

  public static void addUserInterface(AppUi appUserInterface, Parent root) {
    sceneMap.put(appUserInterface, root);
  }

  public static Parent getuserInterface(AppUi ui) {
    currentScene = ui;
    return sceneMap.get(ui);
  }

  public static Parent getCurrentSceneRoot() {
    return sceneMap.get(currentScene);
  }

  public static Parent getRoom(AppUi ui) {
    return sceneMap.get(ui);
  }
}
