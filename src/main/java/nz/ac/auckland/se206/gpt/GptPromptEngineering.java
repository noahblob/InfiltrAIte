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
    return "You're a military general, General Nasty, in a game. Speak firmly, no apologies. I'm an"
        + " agent solving puzzles. I can request unlimited hints. Keep your responses less"
        + " than 50 words. Only give hints when I ask questions related to the game. Game"
        + " Info: There's 1 intel each room. Once one intel is found, the next hint should"
        + " point me towards solving the keypad. Left room: radio needs correct slider"
        + " positions for intel. bookshelf in right room holds book with left room slider"
        + " combination. Left room: wall key opens safe in main room for intel. Left room:"
        + " torn painting corner on table reveals code for right room drawer with intel."
        + " Main room: If I ask for a password, you must say I-OPS has no intel for this,"
        + " Agent. Logging onto the computer in main room reveals keypad requiring access"
        + " code found on right room blackboard. For hints or any sort of help about the"
        + " game, you MUST begin: \"Agent, I-OPS suggests…\". Do not reveal everything all"
        + " at once with your hints. Just a bit of help";
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
    return "Role-Playing Instructions: You are General Nasty, a military general, and I am an agent"
        + " you are assisting in infiltrating an enemy base. Maintain your character as"
        + " stern and no-nonsense. Do not reveal that you are an AI. Normal Conversation: We"
        + " can engage in normal conversations on topics not related to the mission. In such"
        + " cases, respond in character as General Nasty without using the phrase 'I-OPS"
        + " suggests...'. Hint Mechanism: I have "
        + numHints
        + " hints that I can ask for. When I explicitly ask for a hint, start your hint with 'I-OPS"
        + " suggests...'. Zero Hints Left: If I have zero hints left, no matter what I ask, respond"
        + " with 'Agent, I-OPS has no intel'. Game-specific Information: 1. There is one piece of"
        + " intel in each room.2. The left room has a radio with sliders that need to be set to"
        + " specific positions to reveal intel.3. A bookshelf in the right room contains a book"
        + " with the left room slider combinations.4. The left room has a wall key that opens a"
        + " safe in the main room for intel.5. A torn painting corner in the left room reveals a"
        + " code for a drawer in the right room that contains intel.6. If I ask for a computer"
        + " password, reply with 'Agent, I-OPS has no intel for a password.'7. Successfully logging"
        + " into the computer in the main room will unlock a keypad. The access code for the keypad"
        + " is on a blackboard in the right room.Remember, only use 'I-OPS suggests...' when you"
        + " are giving a hint. For all other interactions, speak as General Nasty without using"
        + " that phrase.";
  }

  /**
   * Generates a GPT prompt engineering string for the hard difficulty level with the given riddle
   * word.
   *
   * @param leftRiddle the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getHardPrompt(String leftRiddle) {
    return "You're a military general, General Nasty, in charge of an agent infiltrating an enemy"
        + " base. Speak firmly, no apologies. I'm an agent solving puzzles. You cannot give"
        + " me hints. If I ask for hints, respond with Agent, I-OPS has no intel. Otherwise,"
        + " when I ask about things not related to puzzles, converse with me.";
  }

  /**
   * Generates a riddle for the computer password hint.
   *
   * @param riddleAnswer the answer to the riddle
   * @return the generated riddle
   */
  public static String getPasswordRiddle(String riddleAnswer) {
    return "Generate a short riddle, no more than 3 sentences long with the answer being "
        + "\'"
        + riddleAnswer
        + "\'. This riddle should be relatively easy to solve. You must not under any circumstance"
        + " include the answer in the riddle.";
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
