package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;

/** Controller class for the room view. */
public class TitleController {

  @FXML private Label difficulty;
  @FXML private TextArea description;
  @FXML private Rectangle elbonia;
  @FXML private Rectangle genovia;
  @FXML private Rectangle sanescobar;
  @FXML private Rectangle zubrowka;

  public void initialize() {
      intialiseFonts();
      initialiseCountries();
  }


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

  private void setRectangleHover(Rectangle rectangle, String info, String about) {
    rectangle.setOnMouseEntered(
        event -> {
          difficulty.setText(info);
          description.setText(about);
        });

    rectangle.setOnMouseExited(
        event -> {
          difficulty.setText("DIFFICULTY");
          description.setText("");
        });
  }

  @FXML
  private void onClick(MouseEvent event) {
    // Update in the future with different difficulties but for now just click to next screen.
    Rectangle rectangle = (Rectangle) event.getSource();
    // Set difficulty of game
    if (rectangle == elbonia) {
      GameState.difficulty = 1;
    } else if (rectangle == genovia) {
      GameState.difficulty = 2;
    } else if (rectangle == sanescobar) {
      GameState.difficulty = 3;
    }
    Scene currentScene = rectangle.getScene();
    // Update the scene to the watch.
    currentScene.setRoot(SceneManager.getuserInterface(AppUI.WATCH));
  }

  private void initialiseCountries() {
    setRectangleHover(elbonia, "EASY", "Unlimited hints provided");
    setRectangleHover(genovia, "MEDIUM","Five hints provided");
    setRectangleHover(sanescobar, "HARD", "No Hints provided");
    setRectangleHover(zubrowka, "???", "???");
  }

  private void intialiseFonts() {
      
      difficulty.setText("DIFFICULTY");
      difficulty.setAlignment(Pos.CENTER_RIGHT);
      description.setEditable(false);
      description.setWrapText(true);
      Font level = Font.font("Anonymous Pro", 18); 
      Font font = Font.font("Anonymous Pro", 14); 
      difficulty.setFont(level);
      description.setFont(font);
  }
}
