package nz.ac.auckland.se206.controllers.left;

import java.security.SecureRandom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.controllers.CommanderController;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

/** Controller class for the room view. */
public class DrawerController extends Commander {

  @FXML private Button back;
  @FXML private Button decrypt;
  @FXML private Rectangle topDrawer;
  @FXML private Rectangle midDrawer;
  @FXML private Rectangle botDrawer;
  @FXML private Pane riddlePane;
  @FXML private Pane keyDrawer;
  @FXML private Label lastDigits;
  @FXML private ImageView paper;
  @FXML private TextArea riddle;
  @FXML private TextArea riddleBox;
  @FXML private Button check;

  /** The key in the inventory box. It is currently set to visible. */
  private String riddleCode;

  private boolean isPaperSeen;

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws Exception
   */
  public void initialize() throws Exception {

    super.initialize();
    objective.setText("I wonder whats inside...");
    isPaperSeen = false;
    configureButtons();
    setHoverEvents();
    configureRiddle();
  }

  private void setHoverEvents() {
    setOpacityOnHover(topDrawer);
    setOpacityOnHover(midDrawer);
    setOpacityOnHover(botDrawer);
  }

  private void setOpacityOnHover(Shape shape) {
    shape.setOnMouseEntered(event -> shape.setOpacity(0.5));
    shape.setOnMouseExited(event -> shape.setOpacity(0));
  }

  private void configureRiddle() {
    riddlePane.setVisible(false);
    riddleCode = generateEncrypted(60);
    riddle.setWrapText(true);
    riddle.appendText(riddleCode);
  }

  private void configureButtons() {

    decrypt.setVisible(false);

    back.setOnAction(
        event -> {
          if (riddlePane.isVisible()) {
            riddlePane.setVisible(false);
            decrypt.setVisible(false);
          } else if (keyDrawer.isVisible()) {
            keyDrawer.setVisible(false);
          } else {
            Scene currentScene = keyDrawer.getScene();
            currentScene.setRoot(SceneManager.getuserInterface(AppUi.LEFT));
          }
          toggleDrawers(true);
        });

    decrypt.setOnAction(
        event -> {
          // send the encrypted message to GPT.
          String dialogue = Dialogue.FOUNDENCRYPTED.toString() + riddleCode;
          try {
            CommanderController.getInstance().onSendMessage(event, dialogue);

            // Create a Timeline to wait for 3 seconds
            Timeline timeline =
                new Timeline(
                    new KeyFrame(
                        Duration.millis(3000),
                        ae -> {
                          try {
                            CommanderController.getInstance()
                                .sendForUser(
                                    GptPromptEngineering.getRiddle(GameState.leftRiddleAnswer));
                          } catch (Exception e) {
                            e.printStackTrace();
                          }
                        }));
            // Play the Timeline
            timeline.play();

            // Disable the button.
            decrypt.setDisable(true);

          } catch (Exception e) {
            e.printStackTrace();
          }
        });

    check.setOnAction(
        event -> {
          String attempt = riddleBox.getText();
          String message;

          // If the user inputs the correct answer, then unlock the drawer.
          if (attempt.toLowerCase().contains(GameState.leftRiddleAnswer)) {
            keyDrawer.setVisible(false);
            // Update game state.
            GameState.isRiddleResolved = true;
            message = Dialogue.DRAWERUNLOCK.toString();
            toggleDrawers(true);
          } else {
            message = Dialogue.INCORRECT.toString();
          }
          // Update the dialogue Correctly.
          try {
            CommanderController.getInstance().updateDialogueBox(message);
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }

  @FXML
  public void onClick(MouseEvent event) throws Exception {

    Rectangle clickedObject = (Rectangle) event.getSource();
    String drawer = clickedObject.getId();

    switch (drawer) {
      case "topDrawer":
        updateDialogue(Dialogue.EMPTY);
        break;
      case "midDrawer":
        if (!isPaperSeen) {
          isPaperSeen = true;
        }
        riddlePane.setVisible(true);
        decrypt.setVisible(true);
        toggleDrawers(false);

        break;
      case "botDrawer":
        if (GameState.isRiddleResolved) {
          updateDialogue(Dialogue.DEADEND);
          objective.setText("At least I get a snack...");
        } else {

          if (isPaperSeen) {
            updateDialogue(Dialogue.PAPERSEEN);
          }

          keyDrawer.setVisible(true);
          toggleDrawers(false);
        }
        break;
      default:
        break;
    }
  }

  private String generateEncrypted(int length) {

    String asciiChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    SecureRandom random = new SecureRandom();
    StringBuilder encryptedText = new StringBuilder();
    int count = 0;
    for (int i = 0; i < length; i++) {
      if (count > 0 && random.nextInt(10) < 2) { // 20% chance to insert a space
        encryptedText.append(' ');
        count = 0;
        continue;
      }
      int index = random.nextInt(asciiChars.length());
      encryptedText.append(asciiChars.charAt(index));
      count++;
    }
    return encryptedText.toString();
  }

  private void toggleDrawers(Boolean flag) {
    topDrawer.setVisible(flag);
    midDrawer.setVisible(flag);
    botDrawer.setVisible(flag);
  }
}
