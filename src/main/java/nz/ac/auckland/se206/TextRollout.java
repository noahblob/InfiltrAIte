package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

/** Class that handles text rollout animation */
public abstract class TextRollout {

  @FXML private TextArea dialogue;

  /**
   * Method to generate text rollout animation
   *
   * @param message String to be animated
   * @param dialogue TextArea to be animated
   */
  public void textRollout(Dialogue text) {

    // Play the text rollout sound effect.
    Sound.getInstance().playTextRollout();

    String message = text.toString();
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
    // Stop the sound effect once finished.
    timeline.setOnFinished(e -> Sound.getInstance().stopRollout());
    timeline.play();
  }
}
