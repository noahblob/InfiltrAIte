package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** Class to store all sound effects for the game. */
public class Sound {

  private static Sound instance;

  /**
   * Gets the singleton instance of the Sound class.
   *
   * @return The singleton instance of the Sound class.
   */
  public static Sound getInstance() {
    if (instance == null) {
      instance = new Sound();
    }
    return instance;
  }

  private Media clickMajorSound;
  private Media clickMinorSound;
  private Media hoverOver;
  private Media textRollout;
  private Media phoneSound;
  private MediaPlayer clickMajor;
  private MediaPlayer clickMinor;
  private MediaPlayer hover;
  private MediaPlayer rollout;
  private MediaPlayer phone;
  private List<MediaPlayer> currentlyPlaying;
  private TextToSpeech tts;

  /** Constructor for sound class. */
  private Sound() {

    currentlyPlaying = new ArrayList<>();
    tts = TextToSpeech.getInstance();

    String clickMajorPath = getClass().getResource("/sounds/clickMenu.mp3").toString();
    String clickMinorPath = getClass().getResource("/sounds/clickMenu1.mp3").toString();
    String hoverPath = getClass().getResource("/sounds/hover.mp3").toString();
    String rollPath = getClass().getResource("/sounds/scroll.mp3").toString();
    String notePath = getClass().getResource("/sounds/phone.mp3").toString();

    clickMajorSound = new Media(clickMajorPath);
    clickMinorSound = new Media(clickMinorPath);
    hoverOver = new Media(hoverPath);
    textRollout = new Media(rollPath);
    phoneSound = new Media(notePath);

    clickMajor = new MediaPlayer(clickMajorSound);
    clickMinor = new MediaPlayer(clickMinorSound);
    hover = new MediaPlayer(hoverOver);
    rollout = new MediaPlayer(textRollout);
    phone = new MediaPlayer(phoneSound);

    bindToMute();
  }

  /** Plays sound for major click. */
  public void playClickMajor() {
    playSound(clickMajor);
  }

  /** Plays sound for minor click. */
  public void playClickMinor() {
    playSound(clickMinor);
  }

  /** Plays sound for user hovering object. */
  public void playHover() {
    playSound(hover);
  }

  /** Plays sound for text rollout. */
  public void playTextRollout() {
    playOnLoop(rollout);
  }

  /** Stops playing sound for text rollout. */
  public void stopRollout() {
    stopSound(rollout);
  }

  /** Plays sound for phone when message is received from commander. */
  public void playRecieved() {
    playSound(phone);
  }

  /** Plays transmission sound when message is sending from commander. */
  public void transmitSound() {
    playOnLoop(phone);
  }

  /** Stops playing transmission sound when message has finished sending. */
  public void stopTransmit() {
    stopSound(phone);
  }

  /** Plays text to speech warning when user has 30 seconds remaining. */
  public void playTextToSpeech() {
    tts.speak("ENEMY DETECTED IN OUR BASE!! ENEMY DETECTED IN OUR BASE!!");
  }

  /** Toggles mute for all sounds in the game. */
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

  /**
   * Plays sound if game is not muted.
   *
   * @param player The media player to play
   */
  private void playSound(MediaPlayer player) {

    if (!GameState.isMuted.get()) {
      currentlyPlaying.add(player);
      player.stop();
      player.seek(Duration.ZERO);
      player.play();
      player.setOnEndOfMedia(() -> currentlyPlaying.remove(player));
    }
  }

  /**
   * Plays sound on loop if game is not muted.
   *
   * @param player The media player to play
   */
  private void playOnLoop(MediaPlayer player) {
    player.stop();
    player.seek(Duration.ZERO);
    player.setCycleCount(MediaPlayer.INDEFINITE);
    player.play();
  }

  /**
   * Stops sound that is playing on loop.
   *
   * @param player The media player to stop
   */
  private void stopSound(MediaPlayer player) {
    player.stop();
  }
}
