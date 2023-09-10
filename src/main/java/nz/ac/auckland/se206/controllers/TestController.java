package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class TestController {
  
  @FXML ImageView test;



  @FXML
  private void onEnter(MouseEvent event) {
    System.out.println("Hover detected!");
    handleHover(event,true);
  }

  @FXML
  private void onExit(MouseEvent event) {
    System.out.println("Unhover detected!");
    handleHover(event,false);
  }

  // Need to fix IDK WHY BROKEN !!!!!!
  private void handleHover(MouseEvent event, Boolean flag) {
    
    System.out.println("Hover detected!");
    
    ImageView imageView = (ImageView) event.getSource();
    
    if (flag) {
      imageView.setImage(new Image("/images/countries/1.png"));
    } else {
      imageView.setImage(new Image("/images/countries/elbonia.png"));
    }
    



  }

  
  
}
