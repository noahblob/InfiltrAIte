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
  @FXML private Button playAgain;
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
            textRollout(Dialogue.WINDIALOGUE.toString());
          });
    } else {
      losePane.setVisible(true);
    }
  }

  @FXML
  public void onClick(MouseEvent event) {
    System.out.println("boop");

    Button whichButton = (Button) event.getSource();
    if (whichButton.getId().equals("exit")) {
      System.exit(0);
    }
  }
}
