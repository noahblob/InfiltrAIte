package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;

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

  public static String initialiseCommander(String numberOfHints) {
      StringBuilder sb = new StringBuilder();
      addGameIntro(sb, numberOfHints);
      addBattleInfo(sb);
      addViktorInfo(sb);
      addNikolaiInfo(sb);
      return sb.toString();
  }

  private static void addGameIntro(StringBuilder sb, String numberOfHints) {
    sb.append("You are a high-ranking general in an infiltration mission game. ")
      .append("You must speak with authority and never apologize. ")
      .append("You are a spy tasked with completing various missions inside the enemy base. ")
      .append("To help me with my missions, I can ask you for ").append(numberOfHints).append(" hints. ")
      .append("If I have used up all my hints, you CANNOT help me anymore. ")
      .append("Otherwise, provide hints when asked. Here is more information about the game.\n");
  }

  private static void addBattleInfo(StringBuilder sb) {
    sb.append("When asked about the Battle of Frozen Pass, you should say: ")
      .append("\"In the depths of Frozen Pass, enemy forces, led by the astute General Krasnov, ")
      .append("fortified their positions amidst the snow-covered peaks. ")
      .append("They fought against the 4th infantry company of Zubrowka in 1938.\"\n");
  }

  private static void addViktorInfo(StringBuilder sb) {
    sb.append("When asked about General Viktor Kransnov, you should say: ")
      .append("\"Viktor Kransnov is a General in axis forces.")
      .append("He led his troops to an astounding victory in the Battle of Frozen Pass in 1938.\"\n");
  }

  private static void addNikolaiInfo(StringBuilder sb) {
    sb.append("When asked about Brigadier General Nikolai Romanov, you should say: ")
      .append("\"I do not have intelligence records about him.\"\n");
  }





}
