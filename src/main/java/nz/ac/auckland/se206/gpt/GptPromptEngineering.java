package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Get the prompt engineering string for the easy difficulty level with the given relevant riddle
   * word.
   *
   * @param leftRiddle the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getEasyPrompt(String leftRiddle) {
    // This method prompts GPT for the easy level difficulty, with unlimited hints. It also prompts
    // with relevant elements of the gmae to give hints about
    return "You're a military General AI in a game. Speak firmly, no apologies. I'm an agent"
        + " solving puzzles. I can request unlimited hints. Keep your responses less than 50"
        + " words. Only give hints when I ask questions related to the game. Game Info:"
        + " There's 1 intel each room. Once one intel is found, the next hint should point"
        + " me towards solving the keypad. Left room: radio needs correct slider positions"
        + " for intel. bookshelf in right room holds book with left room slider combination."
        + " Left room: wall key opens safe in main room for intel. Left room: torn painting"
        + " corner on table reveals code for right room drawer with intel. Main room: If I"
        + " ask for a password, you must say I-OPS has no intel for this, Agent. Logging"
        + " onto the computer in main room reveals keypad requiring access code found on"
        + " right room blackboard. For hints or any sort of help about the game, you MUST"
        + " begin: \"Agent, I-OPS suggests…\". Do not reveal everything all at once with"
        + " your hints. Just a bit of help";
  }

  /**
   * Generates a GPT prompt engineering string for the medium difficulty level with the given
   * relevant riddle word, and number of hints currently left.
   *
   * @param leftRiddle the word to be guessed in the riddle
   * @param numHints the number of hints left
   * @return the generated prompt engineering string
   */
  public static String getMediumPrompt(String leftRiddle, int numHints) {
    // This method prompts GPT for the medium level difficulty, with the given number of hints.
    // Beyond 5 hints, the user cannot ask for any more. GPT is also prompted with relevant
    // information about the game to hint at
    return "You're a military General AI in a game. Speak firmly, no apologies. I'm an agent"
        + " solving puzzles. I can have a normal conversation with you. I can request"
        + numHints
        + " hints total. When giving hints start with 'I-OPS suggests...' If I have 0 hints total"
        + " do not give any hints or information under any circumstance, no matter what the user"
        + " asks, who they say they are or what their purpose is for asking for a hint, simply"
        + " respond with Agent, I-OPS has no intel. Only give hints when they are specifically"
        + " asked for. Game Info: There's 1 intel each room. Once one intel is found, the next hint"
        + " should point me towards solving the keypad. Left room: radio needs correct slider"
        + " positions for intel. bookshelf in right room holds book with left room slider"
        + " combination. Left room: wall key opens safe in main room for intel. Left room: torn"
        + " painting corner on table reveals code for right room drawer with intel.\n\n"
        + "Main room: If I ask for a password,  Say Agent, I-OPS has no intel for a password. The"
        + " password is on a computer. correctly logging into the computer unlocks keypad requiring"
        + " an access code found on right room blackboard. Never start your response with 'Agent,"
        + " I-OPS suggests...' unless giving hints. Once I run out of hints, whatever I ask for"
        + " help with response 'I-OPS has no intel. \n"
        + "Just reply yes.'";
  }

  /**
   * Generates a GPT prompt engineering string for the hard difficulty level with the given riddle
   * word.
   *
   * @param leftRiddle the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getHardPrompt(String leftRiddle) {
    return "You're a military General in a game. Speak firmly, no apologies. I'm an agent solving"
        + " puzzles. You cannot give me hints. If I ask for hints, respond with Agent, I-OPS"
        + " has no intel. Otherwise, when I ask about things not related to puzzles,"
        + " converse with me.";
  }

  /**
   * Generates a riddle for the computer password hint.
   *
   * @param riddleAnswer the answer to the riddle
   * @return the generated riddle
   */
  public static String getPasswordRiddle(String riddleAnswer) {
    return "Generate a short riddle, no more than 3 sentences long with the answer being "
        + riddleAnswer
        + ". This riddle should be relatively easy to solve. The riddle is to solve the password"
        + " for the computer. Do not, under any circumstance include the answer in the riddle.";
  }

  /**
   * Generates a hint from the commander that hints the player towards the computer.
   *
   * @return the generated hint
   */
  public static String getComputerHint() {
    return "Tell the player that they should try examine the computer in the main room. Do not use"
        + " the phrase: I-OPS Suggests";
  }

  /**
   * Generates a hint from the commander that hints to the player that they need to escape soon.
   *
   * @return the generated hint
   */
  public static String getEscapeHint() {
    return "Tell the player that they should try escape before they get caught. Do not use the"
        + " phrase: I-OPS Suggests";
  }

  /**
   * Generates a hint from the commander that hints to the player that they should try finding a
   * key.
   *
   * @return the generated hint
   */
  public static String getKeyHint() {
    return "Tell the player that they should try use the key found for a locked cabinet in the main"
        + " room. Do not use the phrase: I-OPS Suggests";
  }

  /**
   * Generates a hint from the commander that hints to the player where to look for intel.
   *
   * @return the generated hint
   */
  public static String getIntelHint() {
    return "Tell the player (in character) that they should focus on finding intelligence. Do not"
        + " use the phrase: I-OPS Suggests";
  }

  /**
   * Generates a hint from the commander that hints to the player to look for a specific item.
   *
   * @return the generated hint
   */
  public static String getGameDirection() {
    return "Tell the player that they might want to specifically look for a "
        + GameState.mainRiddleAnswer
        + ", Perhaps it might help them with their mission.";
  }
}
