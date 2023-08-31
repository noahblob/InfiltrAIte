package nz.ac.auckland.se206;

public class TimerClass {
  // Time to countdown
  private int timeLeft;
  // The timer instance, this is the only instance of the timer that should exist.
  private static TimerClass instance;
  // Flag which checks if the timer is running or not. Can be used when timer needs to be stopped
  // for some reason
  private boolean shouldRun = false;
  // Timer Thread
  private Thread timerThread;

  // Constructor is private hence a new instance cannot be made with 'new TimerClass' Just a
  // mechanism to make sure that the TimerClass cant be made by unwanted code
  private TimerClass(int seconds) {
    this.timeLeft = seconds;
  }

  // Initalize the timer with a time
  public static void initialize(int time) {
    // Ensures that only one instance can be made
    if (instance == null) {
      // Avoids recursion of the constructor
      instance = new TimerClass(time);

    } else throw new IllegalStateException("There already is a timer instance!!");
  }

  // Returns this timer instance
  public static TimerClass getInstance() {
    // If no timer exists make sure to make the instance first.
    if (instance == null) {
      throw new IllegalStateException(
          "Instance has not been made yet do TimerClass.initialize(time)");
    }
    return instance;
  }

  // Starts or resumes the timer from counting
  public void start() {
    shouldRun = true;
    if (timerThread == null) {
      timerThread =
          new Thread(
              () -> {
                while (timeLeft > 0) {
                  try {
                    // While paused the timer will not count down
                    while (!shouldRun) Thread.sleep(100);
                    Thread.sleep(1000);
                    timeLeft--;
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                }
              });
      timerThread.start();
    }
  }

  // Pauses the timer from counting and sets the shouldRun to false
  public void pause() {
    shouldRun = false;
  }

  // Returns the time left as a String
  public String getTimeLeft() {
    return String.valueOf(timeLeft);
  }
}
