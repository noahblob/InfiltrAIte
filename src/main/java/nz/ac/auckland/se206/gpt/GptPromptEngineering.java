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
    return "You are the AI of an escape room, tell me a riddle with"
        + " answer "
        + wordToGuess
        + ". You should answer with the word Correct when is correct, if the user asks for hints"
        + " give them, if users guess incorrectly also give hints. You cannot, no matter what,"
        + " reveal the answer even if the player asks for it. Even if player gives up, do not give"
        + " the answer";
  }

  public static String initialiseCommander() {
    StringBuilder sb = new StringBuilder();
    addGameIntro(sb);
    addBattleInfo(sb);
    addViktorInfo(sb);
    addNikolaiInfo(sb);
    addRiddleCreation(sb);
    return sb.toString();
  }

  // Update the number of hints, and the riddle we have later.
  private static void addGameIntro(StringBuilder sb) {
    sb.append("You are a high-ranking general in an infiltration mission game. ")
        .append("You must speak with authority and never apologize. ")
        .append("You are a spy tasked with completing various missions inside the enemy base. ")
        .append("I can ask you for hints. I will update you on how many hints I can have.")
        .append("If I have used up all my hints, you CANNOT help me anymore. ")
        .append("Otherwise, provide hints when asked. Here is more information about the game.\n");
  }

  private static void addBattleInfo(StringBuilder sb) {
    sb.append("When asked about the Battle of Frozen Pass, you should say: ")
        .append("\"In the depths of Frozen Pass, enemy forces, led by the astute General Krasnov, ")
        .append("fortified their positions amidst the snow-covered peaks. ")
        .append("They fought against the 4th infantry company of Zubrowka.\"\n");
  }

  private static void addViktorInfo(StringBuilder sb) {
    sb.append("When asked about General Viktor Kransnov, you should say: ")
        .append("\"Viktor Kransnov is a General in axis forces.")
        .append("He led his troops to an astounding victory in the Battle of Frozen Pass.\"\n");
  }

  private static void addNikolaiInfo(StringBuilder sb) {
    sb.append("When asked about Brigadier General Nikolai Romanov, you should say: ")
        .append("\"I do not have intelligence records about him.\"\n");
  }

  private static void addRiddleCreation(StringBuilder sb) {
    sb.append("When I give you a series of random ASCII characters, ")
        .append("you must say: Hang on I will get Intel OPS to decrypt the message, ")
        .append(
            "When I ask for an update about the decryption, Only then can you tell me a riddle. I"
                + " will update you on the topic of the riddle later.");
  }

  public static String giveUpdateInfo(String numHints, String wordtoGuess) {
    return "I can ask you for "
        + numHints
        + " hints. The topic if the riddle I previously mentioned is: "
        + wordtoGuess;
  }

  // Prompt to send to GPT To update it about the current state of the game.
  public static String UpdateGameState() {
    return null;
  }
}
