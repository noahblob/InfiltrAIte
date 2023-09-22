package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

public class TimeController {

  @FXML private ImageView watch;
  @FXML private Label time;
  @FXML private Button increase;
  @FXML private Button decrease;
  @FXML private Button set;
  @FXML private Button back;
  private int gameTime;

  public void initialize() {

    Font.loadFont(getClass().getResourceAsStream("/fonts/DS-DIGI.TTF"), 20);
    time.setStyle("-fx-font-family: 'DS-Digital'; -fx-font-size: 50px; -fx-text-fill: black;");
    gameTime = 1;
    updateTime();
    initialiseButtons();
  }

  private void initialiseButtons() {
    increase.setOnAction(
        event -> {
          decrease.setDisable(false);
          gameTime += 2; // Increment by 2 minutes
          if (gameTime > 4) {
            gameTime = 6; // Ensure time doesn't go above 6 minutes.
            increase.setDisable(true);
          }
          updateTime();
        });

    decrease.setOnAction(
        event -> {
          increase.setDisable(false);
          gameTime -= 2; // Decrement by 2 minutes
          if (gameTime < 4) {
            gameTime = 2; // Ensure time doesn't go below 2 minutes
            decrease.setDisable(true);
          }
          updateTime();
        });

    back.setOnAction(
        event -> {
          Scene currentScene = back.getScene();
          currentScene.setRoot(SceneManager.getuserInterface(AppUi.TITLE));
        });
  }

  @FXML
  private void onClick(MouseEvent event) throws Exception {
    Button rectangle = (Button) event.getSource();
    Scene currentScene = rectangle.getScene();

    // Check if Easy, medium or hard and update prompt accordingly.
    if (GameState.difficulty.get() == 1) {
      CommanderController.getInstance()
          .updateGpt(GptPromptEngineering.getEasyPrompt(GameState.puzzleWord));
    } else if (GameState.difficulty.get() == 2) {
      // Update to medium later
      CommanderController.getInstance()
          .updateGpt(GptPromptEngineering.getMediumPrompt(GameState.puzzleWord, 5));
    } else {
      CommanderController.getInstance()
          .updateGpt(GptPromptEngineering.getHardPrompt(GameState.puzzleWord));
    }

    TimerClass timer = TimerClass.getInstance();
    timer.start(gameTime);

    // Update the scene to the main game.
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.MAIN));

    // Reset the scene.
    resetTime();

    Platform.runLater(
        () -> {
          try {
            CommanderController.getInstance().updateDialogueBox(Dialogue.INITIAL.toString());
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }

  private void updateTime() {
    time.setText(String.format("%d:00", gameTime));
  }

  // Reset the scene to default state for replaying.
  private void resetTime() {
    gameTime = 4;
    updateTime();
    increase.setDisable(false);
    decrease.setDisable(false);
  }
}
