package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;

/** Controller class for the room view. */
public class TitleController {

  @FXML private TextArea difficulty;
  @FXML private Rectangle elbonia;
  @FXML private Rectangle genovia;
  @FXML private Rectangle sanescobar;
  @FXML private Rectangle zubrowka;

  // private Timeline timeline;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    initialiseCountries(); 
  }

  // private void updateTimer() {
  //   TimerClass.initialize(20);
  //   TimerClass timerText = TimerClass.getInstance();
  //   timer.setText(timerText.getTimeLeft());
  //   timerText.start();

  //   timeline =
  //       new Timeline(
  //           new KeyFrame(
  //               Duration.seconds(1),
  //               event -> {
  //                 if (Integer.parseInt(timerText.getTimeLeft()) > 0) {
  //                   timer.setText(timerText.getTimeLeft());
  //                 } else {
  //                   timeline.stop();
  //                 }
  //               }));

  //   timeline.setCycleCount(Timeline.INDEFINITE);
  //   timeline.play();
  // }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("key " + event.getCode() + " released");
  }

  private void setRectangleHover(Rectangle rectangle, String info) {
      rectangle.setOnMouseEntered(event -> {
          difficulty.setText(info);
      });

      rectangle.setOnMouseExited(event -> {
          difficulty.setText("");
      });
  }

  @FXML
  private void onClick(MouseEvent event) {
    // Update in the future with different difficulties but for now just click to next screen.
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene currentScene = rectangle.getScene();
    // Update the scene to the watch.
    currentScene.setRoot(SceneManager.getuserInterface(AppUI.WATCH));

  }

  private void initialiseCountries() {
    setRectangleHover(elbonia, "DIFFICULTY: \nEASY");
    setRectangleHover(genovia, "DIFFICULTY: \nMEDIUM");
    setRectangleHover(sanescobar, "DIFFICULTY: \nHARD");
    setRectangleHover(zubrowka, "???");
    difficulty.setEditable(false);
  }
 
}
