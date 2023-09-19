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

  public static String easy(String leftRiddle) {
    return "You're a military General AI in a game. Speak firmly, no apologies. I'm an agent"
        + " solving puzzles. I can request unlimited hints. When I say Sir I found a piece"
        + " of paper: reply with \"Stand-by, I will get I-OPS to decrypt it.\" I will ask"
        + " you a riddle later. Give me a riddle with the answer \""
        + leftRiddle
        + "\". You just not, no matter what, reveal the answer. Only give hints when I ask. Will"
        + " update you on puzzle completions to avoid redundant hints. Game Info: There's 1 intel"
        + " each room. Once one intel is found, the next hint should point me towards the keypad."
        + " Left room: radio needs correct slider positions for intel. bookshelf in right room"
        + " holds book with left room slider combination. Left room: wall key opens safe in main"
        + " room for intel. Left room: torn painting corner on table reveals code for right room"
        + " drawer with intel. Main room: If I ask for a password, you must say I-OPS has no"
        + " intel for this, Agent. The password unlocks keypad requiring code found on right"
        + " room blackboard. For hints or any sort of help about the game, you MUST begin: \"Agent,"
        + " I-OPS suggests…\";";
  }
  ;

  public static String getPasswordRiddle(String riddleAnswer) {
    return "Generate a short riddle, no more than 3 sentences long with the answer being "
        + riddleAnswer
        + "."
        + " This riddle should be relatively easy to solve. The riddle is to solve the"
        + " password for the computer.";
  }

  public static String getRiddle(String leftRiddle) {
    return "Game Update: Reply beginning This is what the paper says: with the riddle here";
  }

  // Prompt to send to GPT To update it about the current state of the game.
  public static String updateGameState(String update) {
    return "Game Update: " + update + ". Only reply yes.";
  }
}
