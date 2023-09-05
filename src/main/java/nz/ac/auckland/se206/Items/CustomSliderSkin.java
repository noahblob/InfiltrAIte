package nz.ac.auckland.se206.Items;

import javafx.scene.control.SkinBase;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class CustomSliderSkin extends SkinBase<Slider> {
    private ImageView thumbImage;

    public CustomSliderSkin(Slider slider) {
        super(slider);

        thumbImage = new ImageView(new Image(getClass().getResource("/images/slider.png").toExternalForm()));
        thumbImage.setFitWidth(20);
        thumbImage.setFitHeight(20);

        StackPane thumbPane = new StackPane(thumbImage);
        getChildren().addAll(thumbPane);

        // Initialize slider and thumb position
        slider.setMin(0);
        slider.setMax(10);
        slider.setValue(0);
        updateThumbPosition(slider.getValue(), slider.getHeight(), slider);

        thumbPane.setOnMousePressed(event -> {
            // capture the initial position when mouse is pressed
        });

        thumbPane.setOnMouseDragged(event -> {
            double mousePosition = event.getY();
            double newValue = (mousePosition / slider.getHeight()) * (slider.getMax() - slider.getMin());
            // Ensure newValue is within bounds
            newValue = Math.min(Math.max(newValue, slider.getMin()), slider.getMax());
            slider.setValue(newValue);
        });

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateThumbPosition(newValue.doubleValue(), slider.getHeight(), slider);
        });
    }

    private void updateThumbPosition(double value, double height, Slider slider) {
        double newPosition = ((value - slider.getMin()) / (slider.getMax() - slider.getMin())) * height;
        // Make sure the thumb stays within the bounds
        newPosition = Math.min(Math.max(0, newPosition), height);
        thumbImage.setTranslateY(newPosition);
    }
}

