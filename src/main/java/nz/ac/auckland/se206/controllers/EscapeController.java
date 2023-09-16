package nz.ac.auckland.se206.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.GameState;

public class EscapeController {

  @FXML private TextArea dialogue;
  @FXML private Button exit;
  @FXML private Button playAgain;
  @FXML private Pane winPane;
  @FXML private Pane losePane;

  public void initialize() {
    GameState.isGameWon.addListener(new ChangeListener<Boolean>() {

      @Override 
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
          winPane.setVisible(true);
          losePane.setVisible(false);
        } else {
          winPane.setVisible(false);
          losePane.setVisible(true);
        }
      }
    });
  }


  @FXML
  public void onClick(MouseEvent event) {
    System.out.println("boop");
  }
}
