package nz.ac.auckland.se206.controllers;

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
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class TimeController {

  @FXML private ImageView watch;
  @FXML private Label time;
  @FXML private Button increase;
  @FXML private Button decrease;
  @FXML private Button set;
  private int gameTime;

  public void initialize() {

    Font.loadFont(getClass().getResourceAsStream("/fonts/DS-DIGI.TTF"), 20);
    time.setStyle("-fx-font-family: 'DS-Digital'; -fx-font-size: 50px; -fx-text-fill: black;");
    gameTime = 4;
    updateTime();
    initialiseButtons();
  }

  private void initialiseButtons() {
    increase.setOnAction(
        event -> {
          gameTime += 2; // Increment by 2 minutes
          if (gameTime > 6) {
            gameTime = 6; // Ensure time doesn't go above 6 minutes.
          }
          updateTime();
        });

    decrease.setOnAction(
        event -> {
          gameTime -= 2; // Decrement by 2 minutes
          if (gameTime < 2) {
            gameTime = 2; // Ensure time doesn't go below 2 minutes
          }
          updateTime();
        });
  }

  @FXML
  private void onClick(MouseEvent event) throws ApiProxyException {
    Button rectangle = (Button) event.getSource();
    Scene currentScene = rectangle.getScene();

    // Update game master with number of hints I have.
    try {
      CommanderController.getInstance()
          .updateGPT(
              GptPromptEngineering.giveUpdateInfo(GameState.numHints, GameState.getRandomWord()));
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Sets the timer time and starts it
    TimerClass.initialize(gameTime);
    TimerClass timer = TimerClass.getInstance();
    timer.start();

    // Update the scene to the main game.
    currentScene.setRoot(SceneManager.getuserInterface(AppUI.MAIN));

    // TO SHOW HOW TEXT ROLL OUT WORKS, CAN DELETE LATER
    CommanderController.getInstance().updateDialogueBox(Dialogue.INITIAL.toString());
  }

  private void updateTime() {
    time.setText(String.format("%d:00", gameTime));
  }
}
