package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
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
  
  // Instance fields
  private ChatCompletionRequest messages;
  private List<TextArea> phoneScreens;
  private List<TextArea> dialogues;
  private boolean isFirstMessage;

  private CommanderController() throws ApiProxyException {

    phoneScreens = new ArrayList<>();
    dialogues = new ArrayList<>();
    isFirstMessage = true;
    // Determine number of hints that commander is able to give.
    String hintCount = "";
    hintCount = (GameState.difficulty == 1) ? "infinite" : (GameState.difficulty == 2) ? "5" : "0";
    messages = new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
    runGpt(new ChatMessage("user", GptPromptEngineering.initialiseCommander(hintCount)));
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg, boolean showUserMessage) {

    if (isFirstMessage) {
      isFirstMessage = false;
      return;
    }

    String role;
    role = msg.getRole().equals("user") ? "You" : "Commander";

    // Only append the user message if showUserMessage is true
    if (showUserMessage || !msg.getRole().equals("user")) {
      for (TextArea screen : phoneScreens) {
        if (screen == null) {
          continue;
        }
        screen.appendText(role + ": " + msg.getContent() + "\n\n");
      }
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
          appendChatMessage(result, true);
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

  // For TextArea input
    public void onSendMessage(MouseEvent event, TextArea inputText) throws Exception {
      String message = inputText.getText();
      handleSendMessage(message);
      inputText.clear();
    }

    // For String input
    public void onSendMessage(ActionEvent event, String message) throws Exception {
      handleSendMessage(message);
    }

    // Method to update GPT with the gamestate.
    public void sendHiddenMessageToGpt(String messageContent) throws Exception {
      ChatMessage msg = new ChatMessage("user", messageContent);

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
            appendChatMessage(gptResponse, false);
          });
        }
      });
      task.setOnFailed(e -> {
        Throwable ex = task.getException();
        ex.printStackTrace();
      });
      new Thread(task).start();
    }



    // Common code to handle sending message
    private void handleSendMessage(String message) throws Exception {
      if (message.trim().isEmpty()) {
          return;
      }
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
                  appendChatMessage(gptResponse, true);
              });
          }
      });
      task.setOnFailed(e -> {
          Throwable ex = task.getException();
          ex.printStackTrace();
      });
      new Thread(task).start();
    }

  // Helper method to add text areas from different scenes to the controller.
  public void addTextArea(TextArea textArea) {
    phoneScreens.add(textArea);
  }
  // Helper method to add text areas from different scenes to the controller.
  public void addDialogueBox(TextArea textArea) {
    dialogues.add(textArea);
  }

  // Method to update commander's dialogue.
  public void updateDialogueBox(String textToRollOut) {
    for (TextArea dialogue : dialogues) {
      textRollout(textToRollOut, dialogue);
    }
  }

  // Method to generate commander text roll out on each screen.
  public void textRollout(String message, TextArea dialogue) {

      String[] words = message.split(" ");
      Timeline timeline = new Timeline();
      Duration timepoint = Duration.ZERO;

      for (String word : words) {
          timepoint = timepoint.add(Duration.millis(100));
          final String finalWord = word;  // Make a final local copy of the word
          KeyFrame keyFrame = new KeyFrame(timepoint, e -> {
            dialogue.appendText(finalWord + " ");
          });
          timeline.getKeyFrames().add(keyFrame);
      }

      // Clear the text after the dialogue
      KeyFrame clearKeyFrame = new KeyFrame(timepoint.add(Duration.millis(2000)), e -> {
              if (dialogue != null) {
                  dialogue.clear();
              }
      });
      timeline.getKeyFrames().add(clearKeyFrame);
      timeline.play();
  }

}
