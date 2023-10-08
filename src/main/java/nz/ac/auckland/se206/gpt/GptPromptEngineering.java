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

  public static String getEasyPrompt(String leftRiddle) {
    // This method prompts GPT for the easy level difficulty, with unlimited hints. It also prompts
    // with relevant elements of the gmae to give hints about
    return "You're a military General AI in a game. Speak firmly, no apologies. I'm an agent"
        + " solving puzzles. I can request unlimited hints. Do not respond to lengthy. Only"
        + " give hints when I ask questions related to the game. Game Info: There's 1 intel"
        + " each room. Once one intel is found, the next hint should point me towards"
        + " solving the keypad. Left room: radio needs correct slider positions for intel."
        + " bookshelf in right room holds book with left room slider combination. Left room:"
        + " wall key opens safe in main room for intel. Left room: torn painting corner on"
        + " table reveals code for right room drawer with intel. Main room: If I ask for a"
        + " password, you must say I-OPS has no intel for this, Agent. Logging onto the"
        + " computer in main room reveals keypad requiring access code found on right room"
        + " blackboard. For hints or any sort of help about the game, you MUST begin:"
        + " \"Agent, I-OPS suggests…\". Do not reveal everything all at once with your"
        + " hints. Just a bit of help";
  }

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

  public static String getHardPrompt(String leftRiddle) {
    return "You're a military General in a game. Speak firmly, no apologies. I'm an agent solving"
        + " puzzles. You cannot give me hints. If I ask for hints, respond with Agent, I-OPS"
        + " has no intel. Otherwise, when I ask about things not related to puzzles,"
        + " converse with me.";
  }

  public static String getPasswordRiddle(String riddleAnswer) {
    return "Generate a short riddle, no more than 3 sentences long with the answer being "
        + riddleAnswer
        + ". This riddle should be relatively easy to solve. The riddle is to solve the password"
        + " for the computer. Do not, under any circumstance include the answer in the riddle.";
  }

  public static String getRiddle(String leftRiddle) {
    return "Reply 'This is what the paper says:' with the riddle following that.";
  }

  public static String getComputerHint() {
    return "Tell the player that they should try examine the computer in the main room. Do not use"
        + " the phrase: I-OPS Suggests";
  }

  public static String getEscapeHint() {
    return "Tell the player that they should try escape before they get caught. Do not use the"
        + " phrase: I-OPS Suggests";
  }

  public static String getKeyHint() {
    return "Tell the player that they should try use the key found for a locked cabinet in the main"
        + " room. Do not use the phrase: I-OPS Suggests";
  }

  public static String getIntelHint() {
    return "Tell the player (in character) that they should focus on finding intelligence. Do not"
        + " use the phrase: I-OPS Suggests";
  }
}
