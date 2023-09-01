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

  public static String initialiseCommander(String numberOfHints) {
    return "You are a high ranking general in an infiltration mission game. "
        + "I am a spy tasked with completing various missions inside the enemy base. "
        + "To help me with my missions, I can ask you for " + numberOfHints 
        + "hints. If I have used up all my hints, you CANNOT help me anymore. "
        + "Otherwise, give hints when asked.";
  }

}
