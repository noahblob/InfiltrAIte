package nz.ac.auckland.se206.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;

public class EscapeController {

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
      // update the dialogue area with final message from commander. (UPDATE LATER BASED ON IF ALL 3 Intel COLLECTED)
      Platform.runLater(() -> {textRollout(Dialogue.WINDIALOGUE.toString());});
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

   // Method to generate backstory.
  private void textRollout(String message) {

    char[] chars = message.toCharArray();
    Timeline timeline = new Timeline();
    Duration timepoint = Duration.ZERO;

    for (char ch : chars) {
      timepoint = timepoint.add(Duration.millis(12));
      final char finalChar = ch; // Make a final local copy of the character
      KeyFrame keyFrame =
          new KeyFrame(
              timepoint,
              e -> {
                dialogue.appendText(String.valueOf(finalChar));
              });
      timeline.getKeyFrames().add(keyFrame);
    }
    timeline.play();
  }
}
