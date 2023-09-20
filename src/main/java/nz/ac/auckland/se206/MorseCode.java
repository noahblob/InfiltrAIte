package nz.ac.auckland.se206;

import java.util.HashMap;
import java.util.Map;

public class MorseCode {

  private static final Map<Character, String> MORSE_CODE_MAP;

  static {
    MORSE_CODE_MAP = new HashMap<>();
    MORSE_CODE_MAP.put('a', ".-");
    MORSE_CODE_MAP.put('b', "-...");
    MORSE_CODE_MAP.put('c', "-.-.");
    MORSE_CODE_MAP.put('d', "-..");
    MORSE_CODE_MAP.put('e', ".");
    MORSE_CODE_MAP.put('f', "..-.");
    MORSE_CODE_MAP.put('g', "--.");
    MORSE_CODE_MAP.put('h', "....");
    MORSE_CODE_MAP.put('i', "..");
    MORSE_CODE_MAP.put('j', ".---");
    MORSE_CODE_MAP.put('k', "-.-");
    MORSE_CODE_MAP.put('l', ".-..");
    MORSE_CODE_MAP.put('m', "--");
    MORSE_CODE_MAP.put('n', "-.");
    MORSE_CODE_MAP.put('o', "---");
    MORSE_CODE_MAP.put('p', ".--.");
    MORSE_CODE_MAP.put('q', "--.-");
    MORSE_CODE_MAP.put('r', ".-.");
    MORSE_CODE_MAP.put('s', "...");
    MORSE_CODE_MAP.put('t', "-");
    MORSE_CODE_MAP.put('u', "..-");
    MORSE_CODE_MAP.put('v', "...-");
    MORSE_CODE_MAP.put('w', ".--");
    MORSE_CODE_MAP.put('x', "-..-");
    MORSE_CODE_MAP.put('y', "-.--");
    MORSE_CODE_MAP.put('z', "--..");
  }

  // Public method to turn a word into morsecode.
  public static String convertToMorse(String word) {

    StringBuilder morseCode = new StringBuilder();
    // Loop through the word and convert the word to morse code
    for (char c : word.toLowerCase().toCharArray()) {
      if (MORSE_CODE_MAP.containsKey(c)) {
        morseCode.append(MORSE_CODE_MAP.get(c)).append(" ");
      } else {
        // Handle space or unknown characters
        if (c == ' ') {
          morseCode.append("  ");
        }
      }
    }
    return morseCode.toString().trim();
  }
}
