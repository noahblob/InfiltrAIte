package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class Sound {

  private static Sound instance;

  private Media clickMajorSound;
  private Media clickMinorSound;
  private Media hoverOver;
  private Media textRollout;
  private MediaPlayer clickMajor;
  private MediaPlayer clickMinor;
  private MediaPlayer hover;
  private MediaPlayer rollout;
  private List<MediaPlayer> currentlyPlaying;
  private TextToSpeech tts;

  private Sound() {

    currentlyPlaying = new ArrayList<>();
    tts = TextToSpeech.getInstance();

    String clickMajorPath = getClass().getResource("/sounds/clickMenu.mp3").toString();
    String clickMinorPath = getClass().getResource("/sounds/clickMenu1.mp3").toString();
    String hoverPath = getClass().getResource("/sounds/hover.mp3").toString();
    String rollPath = getClass().getResource("/sounds/scroll.mp3").toString();

    clickMajorSound = new Media(clickMajorPath);
    clickMinorSound = new Media(clickMinorPath);
    hoverOver = new Media(hoverPath);
    textRollout = new Media(rollPath);

    clickMajor = new MediaPlayer(clickMajorSound);
    clickMinor = new MediaPlayer(clickMinorSound);
    hover = new MediaPlayer(hoverOver);
    rollout = new MediaPlayer(textRollout);

    bindToMute();
  }

  public static Sound getInstance() {
    if (instance == null) {
      instance = new Sound();
    }
    return instance;
  }

  public void playClickMajor() {
    playSound(clickMajor);
  }

  public void playClickMinor() {
    playSound(clickMinor);
  }

  public void playHover() {
    playSound(hover);
  }

  public void playTextRollout() {
    playOnLoop(rollout);
  }

  public void stopRollout() {
    stopSound(rollout);
  }

  public void playTextToSpeech() {
    tts.speak("ENEMY DETECTED IN OUR BASE!! ENEMY DETECTED IN OUR BASE!!");
  }

  private void bindToMute() {
    GameState.isMuted.addListener(
        (observable, oldValue, newValue) -> {
          if (newValue) {
            for (MediaPlayer player : currentlyPlaying) {
              player.stop();
            }
            currentlyPlaying.clear();
            tts.toggleMute(); // Toggle mute for tts based on the new value
          } else {
            tts.toggleMute(); // Toggle mute for tts based on the new value
          }
        });
  }

  private void playSound(MediaPlayer player) {

    if (!GameState.isMuted.get()) {
      currentlyPlaying.add(player);
      player.stop();
      player.seek(Duration.ZERO);
      player.play();
      player.setOnEndOfMedia(() -> currentlyPlaying.remove(player));
    }
  }

  private void playOnLoop(MediaPlayer player) {
    player.stop();
    player.seek(Duration.ZERO);
    player.setCycleCount(MediaPlayer.INDEFINITE);
    player.play();
  }

  private void stopSound(MediaPlayer player) {
    player.stop();
  }
}
