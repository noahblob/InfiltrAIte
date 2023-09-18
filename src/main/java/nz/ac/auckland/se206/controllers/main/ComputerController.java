package nz.ac.auckland.se206.controllers.main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;

public class ComputerController extends Commander implements TimerObserver {

  @FXML private Label passwordHint;
  @FXML private TextArea computerPassword;

  public void initialize() throws Exception {
    super.initialize();
    objective.setText("Figure out the combination!");
    TimerClass.add(this);
  }

  @Override
  public void timerStart() {
    TimerClass timerText = TimerClass.getInstance();
    timer.setText(timerText.getTimerLeft());
  }
}
