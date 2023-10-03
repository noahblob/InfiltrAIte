package nz.ac.auckland.se206.controllers.main;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class ComputerController extends Commander {

  public static ComputerController instance;


  /**
   * Method to store and return the current instance of computercontroller.
   * 
   * @return the current instance of computer controller
   */
  public static ComputerController getInstance() {
    return instance;
  }

  @FXML private Label passwordHint;
  @FXML private TextField computerPassword;
  @FXML private Button back;
  @FXML private Button submitButton;
  private ChatCompletionRequest chatCompletionRequest;

  /**
   * A constructor to initialize the instance of computer controller.
   */
  public ComputerController() {
    instance = this;
  }

  public void initialize() throws Exception {
    super.initialize();
    objective.setText("I wonder what could be on the computer...");

    // Generate the riddle for current game.
    generateRiddle();
  }

  /**
   * Programatically updates the dialogue box with the given dialogue.
   * 
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  public void generateRiddle() throws ApiProxyException {
    // Create a chat completion request and run gpt to generate the password hint
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
    runGpt(
        new ChatMessage(
            "system", GptPromptEngineering.getPasswordRiddle(GameState.mainRiddleAnswer)));
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  public void runGpt(ChatMessage msg) throws ApiProxyException {

    // Create a task for chat gpt to generate password hint asynchronously
    Task<ChatMessage> taskGpt =
        new Task<>() {
          @Override
          protected ChatMessage call() {
            // Add the message to the chat completion request, and generate a response from chatGPT
            chatCompletionRequest.addMessage(msg);
            try {
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              return result.getChatMessage();
            } catch (ApiProxyException e) {
              System.out.println("API KEY MISSING");
              return null;
            }
          }
        };
    // if task succeeds, set the password hint label to generated password hint
    taskGpt.setOnSucceeded(
        e -> {
          ChatMessage result = taskGpt.getValue();
          chatCompletionRequest.addMessage(result);
          passwordHint.setText("password hint: " + result.getContent());
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
        // empty text field in case user has left password sitting there
        computerPassword.setText("");
        currentScene.setRoot(SceneManager.getuserInterface(AppUi.MAIN));
        break;
      case ("submitButton"):
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
    // if the password has not already been solved, check if the users input answer is correct and
    // update commander dialogue and gamestate accordingly
    if (!GameState.isPasswordSolved) {
      if (!currentGuess.equals(GameState.mainRiddleAnswer)) {
        updateDialogue(Dialogue.WRONGPASSCODE);
        computerPassword.clear();
      } else {
        updateDialogue(Dialogue.CORRECTPASSCODE);
        GameState.isPasswordSolved = true;
        computerPassword.clear();
      }
    } else if (GameState.isPasswordSolved) {
      // in the case user has already solved the passcode, remind them that it has already been
      // solved
      updateDialogue(Dialogue.ALREADYSOLVED);
    }
  }
}
