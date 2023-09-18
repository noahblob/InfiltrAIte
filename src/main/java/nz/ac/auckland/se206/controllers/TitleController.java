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
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

/** Controller class for the room view. */
public class TitleController {

  @FXML private Label difficulty;
  @FXML private TextArea description;
  @FXML private Label country;

  @FXML private Circle ring;
  @FXML private ImageView elbonia;
  @FXML private ImageView genovia;
  @FXML private ImageView sanescobar;
  @FXML private ImageView zubrowka;

  private final Map<String, String> countryImageMap = new HashMap<>();

  public void initialize() {
    ring.getStyleClass().add("titlerings");
    intialiseFonts();
    initialiseCountries();
    initialiseImageMap();
  }

  private void initialiseImageMap() {
    countryImageMap.put("E1", "images/countries/elbonia.png");
    countryImageMap.put("E2", "images/countries/1.png");
    countryImageMap.put("G1", "images/countries/genovia.png");
    countryImageMap.put("G2", "images/countries/2.png");
    countryImageMap.put("S1", "images/countries/z1.png");
    countryImageMap.put("S2", "images/countries/z2.png");
    countryImageMap.put("Z1", "images/countries/zubrowka.png");
    countryImageMap.put("Z2", "images/countries/4.png");
  }

  private void initialiseCountries() {
    setImageHover(elbonia, "EASY", "\u221E hints provided", "Elbonia");
    setImageHover(genovia, "MEDIUM", "Five hints provided", "Genovia");
    setImageHover(sanescobar, "HARD", "No Hints provided", "Sanescobar");
    setImageHover(zubrowka, "???", "???", "Zubrowka");
  }

  private void setImageHover(ImageView image, String info, String about, String country) {

    image.setOnMouseEntered(
        event -> {
          changeImage(event, country, true);
          difficulty.setText(info);
          difficulty.setTextAlignment(TextAlignment.JUSTIFY);
          description.setText(about);
          description.setStyle("-fx-text-alignment: center;");
          this.country.setText(country);
        });

    image.setOnMouseExited(
        event -> {
          changeImage(event, country, false);
          difficulty.setText("DIFFICULTY");
          difficulty.setTextAlignment(TextAlignment.JUSTIFY);
          description.setText("");
          this.country.setText("");
          description.setStyle("-fx-text-alignment: center;");
        });
  }

  @FXML
  private void onClick(MouseEvent event) {
    // Update in the future with different difficulties but for now just click to next screen.
    ImageView image = (ImageView) event.getSource();
    System.out.println(image.getId());
    // Set difficulty of game
    if (image.equals(elbonia)) {
      GameState.difficulty = 1;
      GameState.country = "Elbonia";
      GameState.numHints.set(100);
    } else if (image.equals(genovia)) {
      GameState.difficulty = 2;
      GameState.country = "Genovia";
      GameState.numHints.set(5);
    } else if (image.equals(sanescobar)) {
      GameState.difficulty = 3;
      GameState.country = "Sanescobar";
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
