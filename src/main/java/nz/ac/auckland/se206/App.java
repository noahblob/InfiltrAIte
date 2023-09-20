package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  private static Scene scene;

  public static void main(final String[] args) {
    launch();
  }

  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  public static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    // Initialise all scenes using SceneManager so upon game start, they can all be accessed without
    // having to load new elements.
    SceneManager.addUserInterface(AppUi.START, loadFxml("start"));
    SceneManager.addUserInterface(AppUi.TITLE, loadFxml("title"));
    SceneManager.addUserInterface(AppUi.WATCH, loadFxml("time"));
    SceneManager.addUserInterface(AppUi.MAIN, loadFxml("mainroom"));
    SceneManager.addUserInterface(AppUi.RIGHT, loadFxml("rightroom"));
    SceneManager.addUserInterface(AppUi.LEFT, loadFxml("leftroom"));
    SceneManager.addUserInterface(AppUi.DRAWER, loadFxml("drawer"));
    SceneManager.addUserInterface(AppUi.RADIO, loadFxml("radio"));
    SceneManager.addUserInterface(AppUi.LOCKER, loadFxml("rightlocker"));
    SceneManager.addUserInterface(AppUi.KEYPAD, loadFxml("keypad"));
    SceneManager.addUserInterface(AppUi.BOOKSHELF, loadFxml("bookshelf"));
    SceneManager.addUserInterface(AppUi.BLACKBOARD, loadFxml("blackboard"));
    SceneManager.addUserInterface(AppUi.COMPUTER, loadFxml("computer"));
    SceneManager.addUserInterface(AppUi.END, loadFxml("escape"));

    // Set the scene for start screen and show it
    scene = new Scene(SceneManager.getuserInterface(AppUi.START), 800, 600);
    scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
    stage.setOnCloseRequest(
        event -> {
          System.exit(0);
        });
  }
  
}
