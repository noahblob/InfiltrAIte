package nz.ac.auckland.se206;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public abstract class Sound {

  protected void playSound(MediaPlayer player) {
    player.stop();
    player.seek(Duration.ZERO);
    player.play();
  }
}
