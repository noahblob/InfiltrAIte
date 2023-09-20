package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.TextRollout;

public class EscapeController extends TextRollout {

  @FXML private TextArea dialogue;
  @FXML private Button exit;
  @FXML private Button won;
  @FXML private Pane winPane;
  @FXML private Pane losePane;

  public void initialize() {

    System.out.println(GameState.isGameWon.get());
    
    if (GameState.isGameWon.get()) {
      winPane.setVisible(true);
      losePane.setVisible(false);
      // update the dialogue area with final message from commander. (UPDATE LATER BASED ON IF ALL 3
      // Intel COLLECTED)
      Platform.runLater(
          () -> {
            if (GameState.numOfIntel.get() == 1) {
              textRollout(Dialogue.WINDIALOGUE1.toString());
            } else if (GameState.numOfIntel.get() == 2) {
              textRollout(Dialogue.WINDIALOGUE2.toString());
            } else if (GameState.numOfIntel.get() == 3) {
              textRollout(Dialogue.WINDIALOGUE3.toString());
            }
          });
    } else {
      losePane.setVisible(true);
    }
  }

  @FXML
  public void onClick(MouseEvent event) {
    System.exit(0);
  }
}
