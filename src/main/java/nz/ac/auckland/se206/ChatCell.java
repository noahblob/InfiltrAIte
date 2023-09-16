package nz.ac.auckland.se206;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import nz.ac.auckland.se206.gpt.ChatMessage;

/** Custom list cell for the chat. */
public class ChatCell extends ListCell<ChatMessage> {
  // Relevant FXML elements
  private HBox chatHBox;
  private ImageView portrait;
  private Text messageText;

  /** Constructor for the ChatCell. */
  public ChatCell() {

    super();

    // Set up the chat bubble (HBox for horizontal layout)
    chatHBox = new HBox(10);
    chatHBox.setMaxWidth(200);

    // Set up the portrait ImageView
    portrait = new ImageView();
    portrait.setFitHeight(25);
    portrait.setFitWidth(25);

    // Set up the message label
    messageText = new Text();
    messageText.setWrappingWidth(250);
    messageText.setStyle(" -fx-word-break: break-all;");

    // Finally, set the chat HBox as the graphic for the list cell
    setGraphic(chatHBox);
    HBox.setHgrow(chatHBox, Priority.ALWAYS);
  }

  @Override
  protected void updateItem(ChatMessage item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    } else {
      if (item.getRole().equals("user")) {
        updateChatBox("/images/user.png");
      } else {
        updateChatBox("/images/commander.png");
      }

      // Update message label text
      messageText.setText(item.getContent());

      // Display the chat HBox
      setGraphic(chatHBox);
    }
  }

  /**
   * Updates the chat box with the correct image and text based on what was in previous scene.
   *
   * @param imageSource The image source to be used
   */
  protected void updateChatBox(String imageSource) {
    // Update portrait image, all chat cells and text alignment
    portrait.setImage(new Image(getClass().getResource(imageSource).toString()));
    chatHBox.getChildren().clear();
    if (imageSource.equals("/images/user.png")) {
      chatHBox.getChildren().addAll(messageText, portrait);
      messageText.setTextAlignment(TextAlignment.RIGHT);
    } else {
      chatHBox.getChildren().addAll(portrait, messageText);
      messageText.setTextAlignment(TextAlignment.LEFT);
    }
  }
}
