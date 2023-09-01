package nz.ac.auckland.se206;

public enum Dialogue {

  // Add more dialogue and use as game progresses. Will try to tie this to GPT in the future.
  INITIAL("Solider, good job on infiltrating the base. Go get the intel and get out of there!");


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
