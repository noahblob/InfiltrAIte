package nz.ac.auckland.se206;

/** enum to store relevant dialogue to gameplay. */
public enum Dialogue {

  // Add more dialogue and use as game progresses. Will try to tie this to GPT in the future.
  BACKSTORY(
      "Greetings, Agent. Your mission, should you choose to accept it, involves infiltrating"
          + " hostile territory.\n\n"
          + "The objective: retrieve vital intelligence critical to national"
          + " security in a high-stakes espionage mission. You'll need to utilize your skillset in"
          + " problem solving to complete the mission.\n"
          + "Remember, should you be caught or killed, the agency will disavow any knowledge of"
          + " your actions. Good luck, Agent."),
  INITIAL(
      "Agent, good job on infiltrating the base. Go get the intel and get out of there! Remember,"
          + " if you need any help, you can contact me any time."),
  CABINETLOCK("This drawer seems to be locked..."),
  EMPTY("There's nothing there..."),
  CABINETUNLOCK("I just heard the cabinet click..."),
  WRONGKEY("Don't think the key is for this drawer..."),
  DRAWERUNLOCK("You unlocked this drawer!"),
  FOUNDENCRYPTED("Sir, I found a piece of paper with the following characters, what does it say? "),
  KEYNEEDED("Looks like you need a key to open this drawer"),
  KEYFOUND("You found a key! I wonder what its for..."),
  INTELFOUND("Nice! You found some intelligence!"),
  INTELALREADYFOUND("You already found the intel in this drawer!"),
  SOLVEKEYPAD("You need to solve the keypad first!"),
  INCORRECT("I don't think that was the right code..."),
  FOUNDALLINTEL("You have found all required intel, time to escape!"),
  CORRECTBOOK("This book seems like it has important information"),
  WINDIALOGUE1(
      "Good work, Agent. You've done your country proud. You successfully escaped with 1 intel."
          + " Meet for extraction at the rendezvous point."),
  WINDIALOGUE2(
      "Good work, Agent. You've done your country proud. You successfully escaped with 2 intel."
          + " Meet for extraction at the rendezvous point."),
  WINDIALOGUE3(
      "Good work, Agent. You've done your country proud. You successfully escaped with 3 intel."
          + " Meet for extraction at the rendezvous point."),
  CORRECTYEAR("Well done soldier you found one of the enemies intelligence"),
  SLIDERHINT("I wonder if moving the sliders around does anything?"),
  TORNHINT("This portait is torn... I wonder what year the war was..."),
  WRONGYEAR("That doesnt seem to be the correct passcode");
  private final String msg;

  /**
   * Constructor for Dialogue.
   *
   * @param msg The message to be stored
   */
  private Dialogue(final String msg) {
    this.msg = msg;
  }

  /**
   * Returns the message with the arguments inserted.
   *
   * @param args The arguments to be inserted
   * @return The message with the arguments inserted
   */
  public String getMessage(final String... args) {
    String tmpMessage = msg;

    // Replace all %s with the arguments
    for (final String arg : args) {
      tmpMessage = tmpMessage.replaceFirst("%s", arg);
    }

    return tmpMessage;
  }

  /**
   * Prints the message to the console with the arguments inserted.
   *
   * @param args The arguments to be inserted
   */
  public void printMessage(final String... args) {
    System.out.println(getMessage(args));
  }

  @Override
  public String toString() {
    return msg;
  }
}
