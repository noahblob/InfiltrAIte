package nz.ac.auckland.se206.controllers;

import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextAlignment;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

/** Controller class for the room view. */
public class TitleController {

  @FXML private Label difficulty;
  @FXML private TextArea description;
  @FXML private Label country;

  @FXML private ImageView elbonia;
  @FXML private ImageView genovia;
  @FXML private ImageView sanescobar;
  @FXML private ImageView zubrowka;

  private final Map<String, String> countryImageMap = new HashMap<>();
  private String infinity = "\u221E";

  /** Initialise the controller by setting fonts, countries and map image. */
  public void initialize() {
    intialiseFonts();
    initialiseCountries();
    initialiseImageMap();
  }

  private void initialiseImageMap() {
    // Put respective countrys' image in a hashmap with its respective key.
    // E represents Elbonia
    countryImageMap.put("E1", "images/countries/elbonia.png");
    countryImageMap.put("E2", "images/countries/1.png");
    // G represents Genovia
    countryImageMap.put("G1", "images/countries/genovia.png");
    countryImageMap.put("G2", "images/countries/2.png");
    // S represents Sanescobar
    countryImageMap.put("S1", "images/countries/z1.png");
    countryImageMap.put("S2", "images/countries/z2.png");
    // Z represents Zubrowka
    countryImageMap.put("Z1", "images/countries/zubrowka.png");
    countryImageMap.put("Z2", "images/countries/4.png");
  }

  private void initialiseCountries() {
    setImageHover(elbonia, "EASY", infinity + " hints provided", "Elbonia");
    setImageHover(genovia, "MEDIUM", "Five hints provided", "Genovia");
    setImageHover(sanescobar, "HARD", "No Hints provided", "Sanescobar");
  }

  private void setImageHover(ImageView image, String info, String about, String country) {
    // When each difficulty country is hovered over, display relevant information
    image.setOnMouseEntered(
        event -> {
          // play hover sound effect.
          Sound.getInstance().playHover();
          // ensure text formatting is correct when displaying information
          changeImage(event, country, true);
          difficulty.setText(info);
          difficulty.setTextAlignment(TextAlignment.JUSTIFY);
          description.setText(about);
          description.setStyle("-fx-text-alignment: center;");
          this.country.setText(country);
        });

    image.setOnMouseExited(
        event -> {
          // when user is not hovering over a country, prompt them to select a country
          changeImage(event, country, false);
          difficulty.setText("DIFFICULTY");
          difficulty.setTextAlignment(TextAlignment.JUSTIFY);
          description.setText("");
          this.country.setText("SELECT COUNTRY");
          description.setStyle("-fx-text-alignment: center;");
        });
  }

  @FXML
  private void onClick(MouseEvent event) {

    // Play sound effect.
    Sound.getInstance().playClickMajor();

    // Update in the future with different difficulties but for now just click to next screen.
    ImageView image = (ImageView) event.getSource();
    System.out.println(image.getId());
    // Set difficulty of game
    if (image.equals(elbonia)) {
      GameState.difficulty.set(1);
      GameState.numHints.set(100);
    } else if (image.equals(genovia)) {
      GameState.difficulty.set(2);
      GameState.numHints.set(5);
    } else if (image.equals(sanescobar)) {
      GameState.difficulty.set(3);
      GameState.numHints.set(0);
    }

    System.out.println(GameState.numHints);

    Scene currentScene = image.getScene();
    // Update the scene to the watch.
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.WATCH));
  }

  private void changeImage(MouseEvent event, String country, Boolean flag) {
    ImageView imageView = (ImageView) event.getSource();
    String suffix = flag ? "2" : "1";
    String prefix = country.substring(0, 1).toUpperCase();

    String key = prefix + suffix;
    String path = countryImageMap.getOrDefault(key, "default-image-path.png");

    imageView.setImage(new Image(path));
  }

  private void intialiseFonts() {
    difficulty.setText("DIFFICULTY");
    description.setEditable(false);
    description.setWrapText(true);
  }
}
