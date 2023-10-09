package nz.ac.auckland.se206;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import nz.ac.auckland.se206.gpt.ChatMessage;

/** Custom list cell for the chat. */
public class ChatCell extends ListCell<ChatMessage> {
  // Relevant FXML elements
  private HBox chatHorizontalBox;
  private ImageView portrait;
  private Text messageText;

  /** Constructor for the ChatCell. */
  public ChatCell() {

    super();

    // Set up the chat bubble (HBox for horizontal layout)
    chatHorizontalBox = new HBox(10);
    chatHorizontalBox.setMaxWidth(Region.USE_PREF_SIZE);
    chatHorizontalBox.setAlignment(Pos.CENTER);

    // Set up the portrait ImageView
    portrait = new ImageView();
    portrait.setFitHeight(25);
    portrait.setFitWidth(25);

    // Set up the message label
    messageText = new Text();
    messageText.setWrappingWidth(320);
    messageText.setStyle(" -fx-word-break: break-all;");

    messageText.setTextAlignment(TextAlignment.CENTER);
    portrait.setPreserveRatio(true);
    portrait.setSmooth(true);

    // Finally, set the chat HBox as the graphic for the list cell
    setGraphic(chatHorizontalBox);
    HBox.setHgrow(chatHorizontalBox, Priority.ALWAYS);
  }

  @Override
  protected void updateItem(ChatMessage item, boolean empty) {
    super.updateItem(item, empty);
    setPadding(new Insets(0, 0, 10, 0));

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
      setStyle("");

    } else {
      if (item.getRole().equals("user")) {
        updateChatBox("/images/user.png");
      } else if (item.getRole().equals("assistant")) {
        updateChatBox("/images/commander.png");
      } else {
        updateTransmitting();
      }

      // Scroll to the bottom
      if (getListView() != null) {
        getListView().scrollTo(getListView().getItems().size() - 1);
      }

      // Update message label text
      messageText.setText(item.getContent());

      // Display the chat HBox
      setGraphic(chatHorizontalBox);
    }
  }

  @Override
  public double computePrefHeight(double width) {
    double contentHeight = (messageText != null) ? messageText.getLayoutBounds().getHeight() : 0;

    double minHeight = 50;
    double maxHeight = 400;

    contentHeight += 50;

    return Math.min(maxHeight, Math.max(minHeight, contentHeight));
  }

  /**
   * Updates the chat box with the correct image and text based on what was in previous scene.
   *
   * @param imageSource The image source to be used
   */
  protected void updateChatBox(String imageSource) {
    portrait.setImage(new Image(getClass().getResource(imageSource).toString()));
    chatHorizontalBox.getChildren().clear();
    messageText.setStyle("-fx-fill: #000000");
    if (imageSource.equals("/images/user.png")) {
      chatHorizontalBox.getChildren().addAll(messageText, portrait);
      messageText.setTextAlignment(TextAlignment.RIGHT);
      this.setStyle(
          "-fx-background-color: rgba(112,141,129);"
              + " -fx-padding: 0px -10px 0px 0px;"
              + " -fx-border-color: #000; -fx-border-width: 0 0 1px 0;"
              + " -fx-border-radius: 15px;"
              + " -fx-background-radius: 15px;");
    } else {
      chatHorizontalBox.getChildren().addAll(portrait, messageText);
      messageText.setTextAlignment(TextAlignment.LEFT);
      this.setStyle(
          "-fx-background-color: rgba(238,225,179,0.7);"
              + " -fx-padding: 0px 10px 0px 10px;"
              + " -fx-border-color: #000; -fx-border-width: 0 0 1px 0;"
              + " -fx-border-radius: 15px;"
              + " -fx-background-radius: 15px;");
    }
  }

  protected void updateTransmitting() {
    chatHorizontalBox.getChildren().clear();
    messageText.setText("TRANSMITTING...");
    messageText.setTextAlignment(TextAlignment.CENTER);
    messageText.setStyle("-fx-fill: #FFFFFF");
    chatHorizontalBox.getChildren().addAll(messageText);
  }
}
