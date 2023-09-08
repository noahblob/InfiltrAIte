package nz.ac.auckland.se206.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Controller class for the room view. */
public class BookController extends Commander implements TimerObserver {

  @FXML private Label objective;
  @FXML private Text timer;

  @FXML private Rectangle book1;
  @FXML private Rectangle book2;
  @FXML private Rectangle book3;
  @FXML private Rectangle book4;
  @FXML private Rectangle book5;
  @FXML private ImageView actual1;
  @FXML private ImageView actual2;
  @FXML private ImageView actual3;
  @FXML private ImageView actual4;
  @FXML private ImageView actual5;
  @FXML private Button back;
  @FXML private Button goBack;
  @FXML private Label intel;

  @FXML private String currentBook;
  List<Rectangle> bookButtons;

  ImageView book;

  Map<String, ImageView> bookMap = new HashMap<>();

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws ApiProxyException
   */
  public void initialize() throws ApiProxyException {
    intel.textProperty().bind(Bindings.concat("x", GameState.numOfIntel.asString()));

    // Initialise phone.
    super.initialize();
    objective.setText("Wonder which book can help me?");
    TimerClass.add(this);

    bookMap.put("book1", actual1);
    bookMap.put("book2", actual2);
    bookMap.put("book3", actual3);
    bookMap.put("book4", actual4);
    bookMap.put("book5", actual5);
    bookButtons = Arrays.asList(book1, book2, book3, book4, book5);
  }

  @Override
  public void timerStart() {
    TimerClass timerText = TimerClass.getInstance();
    timer.setText(timerText.getTimerLeft());
  }

  /** Handles the popup of books. */
  @FXML
  private void showBook() {
    back.setVisible(true);
    back.setDisable(false);
    book.setVisible(true);

    for (Rectangle bookInShelf : bookButtons) {
      bookInShelf.setVisible(false);
    }
  }

  /**
   * Handles the return from popup of books.
   *
   * @param click
   */
  @FXML
  private void onGoBackShelf() {
    back.setVisible(false);
    back.setDisable(true);
    book.setVisible(false);

    for (Rectangle bookInShelf : bookButtons) {
      bookInShelf.setVisible(true);
    }
  }

  /**
   * Handles the clicking of book type
   *
   * @param click the mouse event
   */
  @FXML
  public void checkBook(MouseEvent click) {

    Rectangle selectedBook = (Rectangle) click.getSource();
    String bookID = selectedBook.getId();
    currentBook = bookID.toString();
    book = bookMap.get(bookID);
    showBook();
  }

  /**
   * Handles the return event
   *
   * @param event the mouse event
   */
  @FXML
  public void onGoBack(MouseEvent event) {
    Button rectangle = (Button) event.getSource();
    Scene currentScene = rectangle.getScene();
    // Update the scene to the right room
    currentScene.setRoot(SceneManager.getuserInterface(AppUI.RIGHT));
  }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("key " + event.getCode() + " released");
  }

  /**
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message the message content of the dialog box
   */
  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
