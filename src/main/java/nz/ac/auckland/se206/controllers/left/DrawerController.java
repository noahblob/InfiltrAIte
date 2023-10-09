package nz.ac.auckland.se206.controllers.left;

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
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.MorseCode;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.controllers.CommanderController;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

/** Controller class for the room view. */
public class DrawerController extends Commander {

  @FXML private Button back;
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

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws Exception if there is an error loading the chat view
   */
  public void initialize() throws Exception {

    super.initialize();
    objective.setText("I wonder whats inside...");
    configureButtons();
    setHoverEvents();
    configurePuzzle();
  }

  /** Indicates when each drawer is being hovered over. */
  private void setHoverEvents() {
    setOpacityOnHover(topDrawer);
    setOpacityOnHover(midDrawer);
    setOpacityOnHover(botDrawer);
  }

  /**
   * Sets the opacity of specified shape that has been hovered.
   *
   * @param shape The shape that has been hovered.
   */
  private void setOpacityOnHover(Shape shape) {
    shape.setOnMouseEntered(event -> shape.setOpacity(0.5));
    shape.setOnMouseExited(event -> shape.setOpacity(0));
  }

  /** Configures riddle and morse code puzzles in drawers. */
  private void configurePuzzle() {
    riddlePane.setVisible(false);
    riddle.setWrapText(true);
    String easterEgg = MorseCode.convertToMorse(GameState.puzzleWord);
    riddle.appendText(easterEgg);
  }

  /** Configure functionality of check and return buttons in drawer puzzles. */
  private void configureButtons() {

    back.setOnAction(
        event -> {
          Sound.getInstance().playClickMinor();
          if (riddlePane.isVisible()) {
            riddlePane.setVisible(false);
          } else if (keyDrawer.isVisible()) {
            keyDrawer.setVisible(false);
          } else {
            Scene currentScene = keyDrawer.getScene();
            currentScene.setRoot(SceneManager.getuserInterface(AppUi.LEFT));
          }
          toggleDrawers(true);
        });

    check.setOnAction(
        event -> {
          String attempt = riddleBox.getText();
          String message;

          // If the user inputs the correct answer, then unlock the drawer.
          if (attempt.toLowerCase().contains(GameState.puzzleWord)) {
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

  /**
   * Handles the click event for different drawers.
   *
   * @param event the mouse event
   * @throws Exception if there is an error loading the chat view
   */
  @FXML
  public void onClick(MouseEvent event) throws Exception {
    Sound.getInstance().playClickMinor();
    updateDialogue(Dialogue.EMPTY);
  }

  private void toggleDrawers(Boolean flag) {
    topDrawer.setVisible(flag);
    midDrawer.setVisible(flag);
    botDrawer.setVisible(flag);
  }
}
