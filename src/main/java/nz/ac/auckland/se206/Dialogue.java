package nz.ac.auckland.se206;

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
  INITIAL("Agent, good job on infiltrating the base. Go get the intel and get out of there!"),
  CABINETLOCK("This drawer seems to be locked..."),
  EMPTY("There's nothing there..."),
  CABINETUNLOCK("I just heard a click somewhere..."),
  DRAWERUNLOCK("You unlocked this drawer!"),
  FOUNDENCRYPTED("Sir, I found a piece of paper with the following characters, what does it say? "),
  KEYNEEDED("Looks like you need a key to open this drawer"),
  KEYFOUND("You found a key! I wonder what its for..."),
  INTELFOUND("Nice! You found some intelligence!"),
  INTELALREADYFOUND("You already found the intel in this drawer!"),
  SOLVEKEYPAD("You need to solve the keypad first!"),
  INCORRECT("I don't think that was the right code..."),
  FOUNDALLINTEL("You have found all required intel, time to escape!");
  private final String msg;

  private Dialogue(final String msg) {
    this.msg = msg;
  }

  public String getMessage(final String... args) {
    String tmpMessage = msg;

    for (final String arg : args) {
      tmpMessage = tmpMessage.replaceFirst("%s", arg);
    }

    return tmpMessage;
  }

  public void printMessage(final String... args) {
    System.out.println(getMessage(args));
  }

  @Override
  public String toString() {
    return msg;
  }
}
