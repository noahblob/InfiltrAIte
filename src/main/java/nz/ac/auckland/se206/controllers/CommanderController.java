package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import nz.ac.auckland.se206.ChatCell;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** Controller class for the chat view. */
public class CommanderController {

  private static CommanderController instance;

  public static CommanderController getInstance() throws Exception {
    if (instance == null) {
      instance = new CommanderController();
    }
    return instance;
  }

  // Instance fields
  private ChatCompletionRequest messages;
  private List<ListView<ChatMessage>> phoneScreens;
  private List<TextArea> dialogues;
  private StringProperty lastInputTextProperty = new SimpleStringProperty("");
  private boolean scroll = false;

  private CommanderController() throws Exception {

    phoneScreens = new ArrayList<>();
    dialogues = new ArrayList<>();
    messages =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
    updateGPT(new ChatMessage("user", GptPromptEngineering.initialiseCommander()));
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    for (ListView<ChatMessage> screen : phoneScreens) {
      if (screen != null) {
        screen.getItems().add(msg);
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
    Task<ChatMessage> task =
        new Task<>() {
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

  // For TextArea input
  public void onSendMessage(MouseEvent event, TextArea inputText) throws Exception {
    String message = inputText.getText();
    handleSendMessage(message);
    inputText.clear();
  }

  // For when user presses enter
  public void onSendMessage(KeyEvent event, TextArea inputText) throws Exception {
    String message = inputText.getText();
    handleSendMessage(message);
    inputText.clear();
  }

  // For String input
  public void onSendMessage(ActionEvent event, String message) throws Exception {
    handleSendMessage(message);
  }

  // Method to talk to GPT without typing.
  public void sendForUser(String messageContent) throws Exception {
    ChatMessage msg = new ChatMessage("user", messageContent);

    Task<ChatMessage> task =
        new Task<>() {
          @Override
          protected ChatMessage call() throws Exception {
            return runGpt(msg);
          }
        };
    new Thread(task).start();
  }

  public void updateGPT(String messageContent) throws Exception {
    ChatMessage msg = new ChatMessage("user", messageContent);
    updateGPT(msg);
  }

  // Method to update GPT's information without any output.
  public void updateGPT(ChatMessage msg) throws Exception {

    // Create new Task (Thread) to handle calling chatGPT.
    Task<ChatMessage> task =
        new Task<>() {
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
          System.out.println("Updated GPT without printing to phone");
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
    Task<ChatMessage> task =
        new Task<>() {
          @Override
          protected ChatMessage call() throws Exception {
            return runGpt(msg);
          }
        };
    task.setOnSucceeded(
        e -> {
          ChatMessage gptResponse = task.getValue();
          if (gptResponse != null) {
            javafx.application.Platform.runLater(
                () -> {
                  appendChatMessage(gptResponse);
                });
          }
        });
    task.setOnFailed(
        e -> {
          Throwable ex = task.getException();
          ex.printStackTrace();
        });
    new Thread(task).start();
  }

  // Helper method to add text areas from different scenes to the controller.
  public void addListView(ListView<ChatMessage> textArea) {
    textArea.setCellFactory(param -> new ChatCell());
    phoneScreens.add(textArea);

    // Ensure the scrollbar is at the bottom initially.
    scrollToBottom(textArea);

    // Add a change listener to keep the scrollbar at the bottom when new items are added.
    textArea
        .getItems()
        .addListener(
            (ListChangeListener<ChatMessage>)
                c -> {
                  if (scroll) return;
                  while (c.next()) {
                    if (c.wasAdded()) {
                      scrollToBottom(textArea);
                    }
                  }
                });
  }

  private void scrollToBottom(ListView<ChatMessage> textArea) {
    scroll = true;
    // Add a non-visible empty item and scroll to it.
    textArea.getItems().add(new ChatMessage("", ""));
    textArea.scrollTo(textArea.getItems().size() - 1);

    // Then remove the added empty item.
    textArea.getItems().remove(textArea.getItems().size() - 1);
    scroll = false;
  }

  // Helper method which keeps track of what is written in the users text are so that it can be kept
  // through different scenes
  public String getLastInputText() {
    return lastInputTextProperty.get();
  }

  public void setLastInputText(String text) {
    this.lastInputTextProperty.set(text);
  }

  public StringProperty lastInputTextProperty() {
    return lastInputTextProperty;
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

    char[] chars = message.toCharArray();
    Timeline timeline = new Timeline();
    Duration timepoint = Duration.ZERO;

    for (char ch : chars) {
      timepoint = timepoint.add(Duration.millis(20));
      final char finalChar = ch; // Make a final local copy of the character
      KeyFrame keyFrame =
          new KeyFrame(
              timepoint,
              e -> {
                dialogue.appendText(String.valueOf(finalChar));
              });
      timeline.getKeyFrames().add(keyFrame);
    }

    // Clear the text after the dialogue
    KeyFrame clearKeyFrame =
        new KeyFrame(
            timepoint.add(Duration.millis(2000)),
            e -> {
              if (dialogue != null) {
                dialogue.clear();
              }
            });
    timeline.getKeyFrames().add(clearKeyFrame);
    timeline.play();
  }
}
