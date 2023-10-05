package nz.ac.auckland.se206;

/** enum to store relevant dialogue to gameplay. */
public enum Dialogue {

  // Add more dialogue and use as game progresses. Will try to tie this to GPT in the future.
  BACKSTORY(
      "Greetings, Agent.\n\nYour mission, should you choose to accept it, involves infiltrating"
          + " hostile territory.\n"
          + "You must exfiltrate with vital enemy intelligence critical to our national security.\n"
          + "We have equipped you with a notepad and a phone, the phone is connected to me.\n"
          + "Should you be caught, the country will disavow any knowledge of"
          + " your actions.\nGood luck, Agent."),
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
  KEYUSED("Look's like that key was useful!"),
  INTELFOUND("Nice! You found some intelligence! Try to get out now agent..."),
  INTELALREADYFOUND("You already found the intel in this drawer!"),
  SOLVEKEYPAD("You need to solve the keypad first!"),
  INCORRECT("I don't think that was the right code..."),
  NOINTELFOUND("Agent, you are yet to find any intel, keep looking!"),
  CORRECTBOOK("This book seems like it has important information"),
  PAPERSEEN("That encrypted piece of paper might be for this drawer..."),
  DOORLOCKED("How in the world do you open the door..."),
  WRONGCODE("That's not the right code, try again!"),
  NOCODE("You haven't entered anything!"),
  COMPUTERHINT("Maybe you can open the door with this computer..."),
  DOORUNLOCK("Nice work, you cracked the code to the door!"),
  WIN1(
      "Good work, Agent. You've done your country proud. You successfully escaped with 1 intel from"
          + " the base. We now have critical information confirming the enemy's plans to launch an"
          + " attack on our southern sea front. Your next mission is crucial. Meet for extraction"
          + " at the rendezvous point."),
  WIN2(
      "Superb work, Agent. You've done your country proud. You successfully escaped with 2 intel"
          + " from the base. We now have critical information confirming the enemy's plans to"
          + " launch an attack on our southern sea front. They plan to do so in approximately in"
          + " T-minus 10 days. Your next mission is crucial. Meet for extraction at the rendezvous"
          + " point."),
  WIN3(
      "Outstanding, Agent! You've elevated the stakes and your nation salutes you. With 3 intel"
          + " pieces gripped tightly in your hand, you've pierced the veil of the enemy's"
          + " plans.We've confirmed that they're plotting a devastating strike on our southern"
          + " maritime stronghold in T-minus 10 days. They're also deploying a secret agent to"
          + " infiltrate us. \n\n"
          + "Your mission, should you choose to accept it, is nothing less than the assassination"
          + " of this covert operative. Pack your gear and meet for extraction at the rendezvous"
          + " point."),
  CORRECTYEAR("Good work agent, you found one of the enemy's intelligence, try to escape now!"),
  SLIDERHINT("I wonder if moving the sliders around does anything?"),
  SLIDERSOLVED("Whats this now?"),
  DEADEND("Dang, theres nothing in here but cake..."),
  TORNHINT("This portait is torn... I wonder what year the war was..."),
  NOTHINGTHERE("Theres nothing there..."),
  WRONGYEAR("That doesnt seem to be the correct passcode"),
  WRONGPASSCODE("That's not the right password, try again!"),
  CORRECTPASSCODE("You cracked the passcode! Something else in the room may have been revealed..."),
  ALREADYGOTLOCKER("You have already collected the intelligence"),
  INTRUDERDETECED(
      "Oh no it seems like they found out you're here! Quickly finish up and get out of there!!"),
  ALREADYSOLVED("You already solved this puzzle!"),
  KEYPADLOCKED("Seems like this is locked...");
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
