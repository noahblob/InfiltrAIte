package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** Controller class for the chat view. */
public class CommanderController {

  private static CommanderController instance;

  public static CommanderController getInstance() throws ApiProxyException {
    if (instance == null) {
      instance = new CommanderController();
    }
    return instance;
  }
  
  private ChatCompletionRequest messages;
  private List<TextArea> phoneScreens;

  private CommanderController() throws ApiProxyException {

    phoneScreens = new ArrayList<>();
    String hintCount = "";
    if (GameState.difficulty == 1) {
      hintCount = "infinite";
    } else if (GameState.difficulty == 2) {
      hintCount = "5";
    } else {
      hintCount = "0";
    }
    messages = new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
    runGpt(new ChatMessage("user", GptPromptEngineering.initialiseCommander(hintCount)));
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {

    // Updates each of the screens in each room with updated message.
    for (TextArea screen : phoneScreens) {
      if (screen == null) {
        continue;
      }
      screen.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");
    }
  }


  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
   private ChatMessage runGpt(ChatMessage msg) {
    
    // Create new Task (Thread) to handle calling chatGPT.
    Task<ChatMessage> task = new Task<>() {
      @Override
      protected ChatMessage call() throws Exception {
        messages.addMessage(msg);
        try {
          ChatCompletionResult chatCompletionResult = messages.execute();
          Choice result = chatCompletionResult.getChoices().iterator().next();
          messages.addMessage(result.getChatMessage());
          return result.getChatMessage();
        } catch (ApiProxyException e) {
          e.printStackTrace();
          return null;
        }
      }
    };
    task.setOnSucceeded(
        workerStateEvent -> {
          ChatMessage result = task.getValue();
          appendChatMessage(result);
        });

    // Optional: catch any exceptions thrown during the task execution.
    task.setOnFailed(
        workerStateEvent -> {
          Throwable ex = task.getException();
          ex.printStackTrace();
        });

    new Thread(task).start();
    return msg;
  }

  public void onSendMessage(MouseEvent event, TextArea inputText) throws ApiProxyException, IOException {
      String message = inputText.getText();
      if (message.trim().isEmpty()) {
          return;
      }
      inputText.clear();
      ChatMessage msg = new ChatMessage("user", message);
      
      // Asynchronous task to get GPT response
      Task<ChatMessage> task = new Task<>() {
          @Override
          protected ChatMessage call() throws Exception {
              return runGpt(msg);
          }
      };
      task.setOnSucceeded(e -> {
          ChatMessage gptResponse = task.getValue();
          if (gptResponse != null) {
              javafx.application.Platform.runLater(() -> {
                  appendChatMessage(gptResponse);
              });
          }
      });
      task.setOnFailed(e -> {
          Throwable ex = task.getException();
          ex.printStackTrace();
      });
      new Thread(task).start();
  }

  public void addTextArea(TextArea textArea) {
    phoneScreens.add(textArea);
  }

}
