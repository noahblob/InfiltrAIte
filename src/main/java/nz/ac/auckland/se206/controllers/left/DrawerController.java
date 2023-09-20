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
    configurePuzzle();
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

  private void configurePuzzle() {
    riddlePane.setVisible(false);
    riddle.setWrapText(true);
    String easterEgg = MorseCode.convertToMorse(GameState.puzzleWord);
    riddle.appendText(easterEgg);
  }

  private void configureButtons() {

    back.setOnAction(
        event -> {
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

  private void toggleDrawers(Boolean flag) {
    topDrawer.setVisible(flag);
    midDrawer.setVisible(flag);
    botDrawer.setVisible(flag);
  }
}
