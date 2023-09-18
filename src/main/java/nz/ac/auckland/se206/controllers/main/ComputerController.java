package nz.ac.auckland.se206.controllers.main;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class ComputerController extends Commander implements TimerObserver {

  @FXML private Label passwordHint;
  @FXML private TextArea computerPassword;
  @FXML private Button back;
  @FXML private Button submitButton;
  private ChatCompletionRequest chatCompletionRequest;

  public void initialize() throws Exception {
    super.initialize();
    objective.setText("Figure out the combination!");
    TimerClass.add(this);

    // Create a chat completion request and run gpt to generate the password hint
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
    runGpt(new ChatMessage("user", GptPromptEngineering.getPasswordRiddle()));
  }

  @Override
  public void timerStart() {
    TimerClass timerText = TimerClass.getInstance();
    timer.setText(timerText.getTimerLeft());
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private void runGpt(ChatMessage msg) throws ApiProxyException {

    // Create a task for chat gpt to generate password hint asynchronously
    Task<ChatMessage> taskGpt =
        new Task<>() {
          @Override
          protected ChatMessage call() {
            chatCompletionRequest.addMessage(msg);
            try {
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              return result.getChatMessage();
            } catch (ApiProxyException e) {
              e.printStackTrace();
              return null;
            }
          }
        };
    // if task succeeds, set the password hint label to generated password hint
    taskGpt.setOnSucceeded(
        e -> {
          ChatMessage result = taskGpt.getValue();
          chatCompletionRequest.addMessage(result);
          passwordHint.setText("paswsword hint: " + result.getContent());
        });

    // Optional: catch any exceptions thrown during the task execution.
    taskGpt.setOnFailed(
        workerStateEvent -> {
          Throwable ex = taskGpt.getException();
          ex.printStackTrace();
        });

    // Run the task in a background thread
    Thread thread = new Thread(taskGpt);
    thread.setDaemon(true);
    thread.start();
  }

  /**
   * Handles the event of user pressing the either button on the computer screen.
   *
   * @param event the mouse event
   * @throws Exception
   */
  @FXML
  private void onClick(MouseEvent event) throws Exception {
    Button button = (Button) event.getSource();
    Scene currentScene = button.getScene();
    // switch case for different buttons, including back and submit button
    switch (button.getId()) {
      case ("back"):
        currentScene.setRoot(SceneManager.getuserInterface(AppUi.MAIN));
        break;
      case ("submit"):
        String currentGuess = computerPassword.getText().toLowerCase();
        checkPassword(currentGuess);
        break;
      default:
        break;
    }
  }

  /**
   * Handles the event of user pressing ENTER key to submit password rather than pressing submit
   * button.
   *
   * @param event the key event
   * @throws Exception if there is an error communicating with the API proxy
   */
  @FXML
  private void onKeyPressed(KeyEvent event) throws Exception {
    if (event.getCode() == KeyCode.ENTER) {
      checkPassword(computerPassword.getText().toLowerCase());
    }
  }

  /**
   * Checks if the password guess is correct or not.
   *
   * @param currentGuess the password guess
   * @throws Exception if there is an error communicating with the API proxy
   */
  public void checkPassword(String currentGuess) throws Exception {
    if (!GameState.isPasswordSolved) {
      if (!currentGuess.equals("potato")) {
        updateDialogue(Dialogue.WRONGPASSCODE);
        computerPassword.clear();
      } else {
        updateDialogue(Dialogue.CORRECTPASSCODE);
        GameState.isPasswordSolved = true;
      }
    } else {
      updateDialogue(Dialogue.ALREADYSOLVED);
    }
  }
}
