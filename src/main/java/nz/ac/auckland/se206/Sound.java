package nz.ac.auckland.se206;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
  private Media phoneSound;
  private Media radioSound;
  private Media eggSound;
  private Media eggTwoSound;
  private Media completed;
  private MediaPlayer clickMajor;
  private MediaPlayer clickMinor;
  private MediaPlayer hover;
  private MediaPlayer rollout;
  private MediaPlayer phone;
  private MediaPlayer radio;
  private MediaPlayer eggPlayerOne;
  private MediaPlayer eggPlayerTwo;
  private MediaPlayer buzz;

  private Set<MediaPlayer> currentlyPlaying;
  private TextToSpeech tts;
  private Random random;

  private Sound() {

    currentlyPlaying = new HashSet<>();
    tts = TextToSpeech.getInstance();

    String clickMajorPath = getClass().getResource("/sounds/clickMenu.mp3").toString();
    String clickMinorPath = getClass().getResource("/sounds/clickMenu1.mp3").toString();
    String hoverPath = getClass().getResource("/sounds/hover.mp3").toString();
    String rollPath = getClass().getResource("/sounds/scroll.mp3").toString();
    String notePath = getClass().getResource("/sounds/phone.mp3").toString();
    String radioPath = getClass().getResource("/sounds/radio.mp3").toString();
    String eggOnePath = getClass().getResource("/sounds/egg1.mp3").toString();
    String eggTwoPath = getClass().getResource("/sounds/egg2.mp3").toString();
    String completedPath = getClass().getResource("/sounds/completed.mp3").toString();

    clickMajorSound = new Media(clickMajorPath);
    clickMinorSound = new Media(clickMinorPath);
    hoverOver = new Media(hoverPath);
    textRollout = new Media(rollPath);
    phoneSound = new Media(notePath);
    radioSound = new Media(radioPath);
    eggSound = new Media(eggOnePath);
    eggTwoSound = new Media(eggTwoPath);
    completed = new Media(completedPath);

    clickMajor = new MediaPlayer(clickMajorSound);
    clickMinor = new MediaPlayer(clickMinorSound);
    hover = new MediaPlayer(hoverOver);
    rollout = new MediaPlayer(textRollout);
    phone = new MediaPlayer(phoneSound);
    radio = new MediaPlayer(radioSound);
    eggPlayerOne = new MediaPlayer(eggSound);
    eggPlayerTwo = new MediaPlayer(eggTwoSound);
    buzz = new MediaPlayer(completed);

    random = new Random();

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

  public void playRadio() {
    playRadioSegment(radio);
  }

  public void stopRadio() {
    radio.stop();
  }

  public void playTextRollout() {
    playOnLoop(rollout);
  }

  public void stopBuzz() {
    buzz.stop();
  }

  public void stopRollout() {
    stopSound(rollout);
  }

  public void stopAllSound() {
    for (MediaPlayer player : currentlyPlaying) {
      player.stop();
    }
    currentlyPlaying.clear();
  }

  public void playCompleted() {
    playSound(buzz);
  }

  public void playSoundOne() {
    playSound(eggPlayerOne);
  }

  public void playSoundTwo() {
    playSound(eggPlayerTwo);
  }

  public void playRecieved() {
    playSound(phone);
  }

  public void transmitSound() {
    playOnLoop(phone);
  }

  public void stopTransmit() {
    stopSound(phone);
  }

  public void playTTS() {
    tts.speak("ENEMY DETECTED IN OUR BASE!! ENEMY DETECTED IN OUR BASE!!");
  }

  private void bindToMute() {
    GameState.isMuted.addListener(
        (observable, oldValue, newValue) -> {
          if (newValue) {
            stopAllSound();
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

  private void playRadioSegment(MediaPlayer player) {
    // Stop any currently running timelines and media player instances
    if (currentlyPlaying.contains(player)) {
      player.stop();
    }

    // Start playing from a new random point
    int point = random.nextInt(100);
    player.seek(Duration.seconds(point));
    player.play();

    // Set a new timeline to stop the player after 2 seconds
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10000), ae -> player.stop()));
    timeline.play();
  }

  private void playOnLoop(MediaPlayer player) {
    currentlyPlaying.add(player);
    player.stop();
    player.seek(Duration.ZERO);
    player.setCycleCount(MediaPlayer.INDEFINITE);
    player.play();
  }

  private void stopSound(MediaPlayer player) {
    player.stop();
  }
}
