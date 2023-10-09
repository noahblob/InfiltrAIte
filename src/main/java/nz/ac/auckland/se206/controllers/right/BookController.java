package nz.ac.auckland.se206.controllers.right;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

/** Controller class for the room view. */
public class BookController extends Commander {

  private static BookController instance;

  public static BookController getInstance() {
    return instance;
  }

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
  @FXML private Text title1;
  @FXML private Text title2;
  @FXML private Text title3;
  @FXML private Text title4;
  @FXML private Text title5;

  @FXML private Text description1;
  @FXML private Text description2;
  @FXML private Text description3;
  @FXML private Text description4;
  @FXML private Text description5;

  @FXML private String currentBook;

  private List<Rectangle> bookButtons;

  private String bookCode;

  private ImageView book;

  private Map<String, ImageView> bookMap = new HashMap<>();
  private Map<String, Boolean> content = new HashMap<>();
  private Map<String, Text> titleMap = new HashMap<>();
  private Map<String, Text> descriptionMap = new HashMap<>();
  private String goodTitle = "[IMPORTANT] INTELLIGENCE";

  private String pattern;
  private String goodDesc;

  private Map<String, String> titleToDescription =
      new HashMap<>() {
        {
          put("How to bake a Cake", "Simple Vanilla Cake Recipe\r\nYou will need eggs,flour...");
          put(
              "The rain is never ending",
              "The small town of Riverview had seen rain before, but this was different.");
          put(
              "Data Structures\r\n5th Edition",
              "A heap is a specialized tree-based data structure that satisfies the heap"
                  + " property.");
          put(
              "3 Programming tips even your grandma knows",
              "1- Always Save Your Work\r\n"
                  + "\r\n"
                  + "2- Read the Error Messages\r\n"
                  + "\r\n"
                  + "3- Google is Your Friend\r\n");
        }
      };

  public BookController() {
    instance = this;
  }

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws Exception
   */
  public void initialize() throws Exception {
    // bind the intel to gamestate number of intel so it updates every time user finds intel
    intel.textProperty().bind(Bindings.concat("x", GameState.numOfIntel.asString()));

    // Initialise phone.
    super.initialize();
    objective.setText("Wonder which book can help me?");

    // Initialise the book map and put all contents into each book
    bookMap.put("book1", actual1);
    bookMap.put("book2", actual2);
    bookMap.put("book3", actual3);
    bookMap.put("book4", actual4);
    bookMap.put("book5", actual5);
    titleMap.put("book1", title1);
    titleMap.put("book2", title2);
    titleMap.put("book3", title3);
    titleMap.put("book4", title4);
    titleMap.put("book5", title5);
    descriptionMap.put("book1", description1);
    descriptionMap.put("book2", description2);
    descriptionMap.put("book3", description3);
    descriptionMap.put("book4", description4);
    descriptionMap.put("book5", description5);

    // setup content of books and add each button to a list to be used later
    setupContent();
    bookButtons = Arrays.asList(book1, book2, book3, book4, book5);
  }

  public void setupContent() {
    // Select a random book to be the good book
    int randomNumber = new Random().nextInt(5) + 1;
    String randomBookId = "book" + randomNumber;
    content.put(randomBookId, true);

    List<String> availableBadTitles = new ArrayList<>(titleToDescription.keySet());

    List<String> allBooks =
        new ArrayList<>(Arrays.asList("book1", "book2", "book3", "book4", "book5"));
    allBooks.remove(randomBookId);

    Random random = new Random();
    for (String bookId : allBooks) {
      int randomIndex = random.nextInt(availableBadTitles.size());

      String selectedBadTitle = availableBadTitles.get(randomIndex);
      content.put(bookId, false);

      titleMap.get(bookId).setText(selectedBadTitle);
      descriptionMap.get(bookId).setText(titleToDescription.get(selectedBadTitle));

      availableBadTitles.remove(randomIndex);
    }

    // Update the passcode for left room puzzle.
    pattern = String.valueOf(GameState.sliderAnswer);
    goodDesc = "Stored in\r\nLEFT ROOM\r\nRadio sliders\r\n" + "\r\n " + pattern;

    titleMap.get(randomBookId).setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

    descriptionMap.get(randomBookId).setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
  }

  /**
   * Handles the popup of books.
   *
   * @throws Exception
   */
  @FXML
  private void showBook() throws Exception {
    Sound.getInstance().playClickMinor();
    back.setVisible(true);
    back.setDisable(false);
    book.setVisible(true);

    // Allow the user to click on the books on the shelf.
    for (Rectangle bookInShelf : bookButtons) {
      bookInShelf.setVisible(false);
    }

    if (content.get(bookCode)) {

      titleMap.get(bookCode).setText(goodTitle);
      descriptionMap.get(bookCode).setText(goodDesc);

      titleMap.get(bookCode).setVisible(true);
      descriptionMap.get(bookCode).setVisible(true);

      // Prompt the user that this book may important for the game.
      updateDialogue(Dialogue.CORRECTBOOK);
    } else {
      // Display a random text that is not the slider code.
      titleMap.get(bookCode).setVisible(true);
      descriptionMap.get(bookCode).setVisible(true);
    }
  }

  /**
   * Resets the font of the book titles and descriptions.
   */
  public void resetFont() {
    if (titleMap.containsKey(bookCode)) {
      titleMap.get(bookCode).setStyle("");
    }
    if (book != null) {
      onGoBackShelf();
    }
    if (descriptionMap.containsKey(bookCode)) {
      descriptionMap.get(bookCode).setStyle("");
    }
  }

  /**
   * Handles the return from popup of books.
   *
   * @param click
   */
  @FXML
  private void onGoBackShelf() {
    Sound.getInstance().playClickMinor();
    // disable the back button and hide the book
    back.setVisible(false);
    back.setDisable(true);
    book.setVisible(false);

    // Make each book appear on the shelf
    for (Rectangle bookInShelf : bookButtons) {
      bookInShelf.setVisible(true);
    }
    // Hide all the book titles and descriptions
    for (int i = 1; i <= 5; i++) {
      titleMap.get("book" + i).setVisible(false);
      descriptionMap.get("book" + i).setVisible(false);
    }
  }

  /**
   * Handles the clicking of book type
   *
   * @param click the mouse event
   * @throws Exception
   */
  @FXML
  public void checkBook(MouseEvent click) throws Exception {

    Rectangle selectedBook = (Rectangle) click.getSource();
    bookCode = selectedBook.getId();
    currentBook = bookCode.toString();
    book = bookMap.get(bookCode);

    showBook();
  }

  /**
   * Handles the player clicking the return button.
   *
   * @param event the mouse event
   */
  @FXML
  public void onGoBack(MouseEvent event) {
    Button rectangle = (Button) event.getSource();
    Scene currentScene = rectangle.getScene();
    Sound.getInstance().playClickMinor();
    // Update the scene to the right room
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.RIGHT));
  }

  /**
   * Handles the hovering of rectangles, and highlights them upon hovering.
   *
   * @param event the mouse event
   */
  @FXML
  public void onHover(MouseEvent event) {
    Rectangle rectangle = (Rectangle) event.getSource();
    rectangle.setOpacity(1);
  }

  /**
   * Handles the un-hovering of rectangles
   *
   * @param event the mouse
   */
  @FXML
  public void onHoverExit(MouseEvent event) {
    Rectangle rectangle = (Rectangle) event.getSource();
    rectangle.setOpacity(0);
  }
}
