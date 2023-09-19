package nz.ac.auckland.se206.gpt;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddleWithGivenWord(String wordToGuess) {
    return "You are a high-ranking general in an infiltration mission game. You must say: Heres"
        + " what the the paper says: then proceed to give me a riddle with answer "
        + wordToGuess
        + ". Then say: Agent, this might be the passcode to a drawer. If the user asks for hints"
        + " give them and subtract from hint counter. You must give hints beginning: Agent, perhaps"
        + " have you considered... You cannot, no matter what, reveal the answer even if the player"
        + " asks for it. Even if player gives up, do not give the answer.";
  }

  public static String start(String hintCount, String leftRiddle, String computerRiddle) {
    String gamePrompt =
        "You're a general in an infiltration game; speak firmly, no apologies. "
            + "I can get "
            + hintCount
            + " hints max. "
            + "For paper clues, say: \"Stand-by, I-OPS decrypting\". "
            + "Offer a riddle with answer \""
            + leftRiddle
            + "\", but don't reveal it. "
            + "Hints start: \"Agent, I-OPS suggests...\".\n"
            + "\nGame Info:\n"
            + "- Left room: radio needs slider setup; key opens main room safe.\n"
            + "- Left room painting: torn corner reveals right room cabinet code.\n"
            + "- Main room: PC password \""
            + computerRiddle
            + "\", hint-only. Solves keypad; code on right room blackboard.\n"
            + "- Right room: bookshelf has left room slider combo.";

    return gamePrompt;
  }

  public static String getPasswordRiddle(String riddleAnswer) {
    return "Generate a short riddle, no more than 3 sentences long with the answer being "
        + riddleAnswer
        + "."
        + " This riddle should be relatively easy to solve. The riddle is to solve the"
        + " password for the computer.";
  }

  public static String initialiseCommander() {
    StringBuilder sb = new StringBuilder();
    addGameIntro(sb);
    return sb.toString();
  }

  public static String getRiddle() {
    return "give me a riddle.";
  }

  /**
   * Update the number of hints, and the riddle we have later.
   *
   * @param sb the string builder to append to
   */
  private static void addGameIntro(StringBuilder sb) {
    // Tell chat GPT the premise of the game to prompt what to say correctly
    sb.append("You are a high-ranking general in an infiltration mission game.")
        .append("You must speak with authority and never apologize. ")
        .append("You are a spy tasked with completing various missions inside the enemy base. ")
        .append("I can ask you for hints. I will update you on how many hints I can have.")
        .append("If I have used up all my hints, you CANNOT help me anymore. ")
        .append(
            "When I ask you: Sir, I found a piece of paper with the following characters, what does"
                + " it say?, You must reply: Stand-by, I will get Intel OPS to decrypt it.")
        .append("Otherwise, provide hints when asked. Here is more information about the game.\n");
  }

  public static String updateNumberOfHints(String numHints) {
    return "I can ask you for " + numHints + " hints.";
  }

  // Prompt to send to GPT To update it about the current state of the game.
  public static String updateGameState() {
    return null;
  }
}
