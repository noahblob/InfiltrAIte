package nz.ac.auckland.se206.controllers;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Parent;

public class SceneManager {

  public enum AppUi {
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
    BOOKSHELF,
    POPUP,
    BLACKBOARD,
    END
  }

  private static Map<AppUi, Parent> sceneMap = new HashMap<>();

  public static void addUserInterface(AppUi appUserInterface, Parent root) {
    sceneMap.put(appUserInterface, root);
  }

  public static Parent getuserInterface(AppUi ui) {
    return sceneMap.get(ui);
  }
}
