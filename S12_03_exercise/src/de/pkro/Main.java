package de.pkro;

import java.util.*;

public class Main {

  public static void main(String[] args) {
    Sentence sentence = new Sentence("Hello World");
    sentence.getWord(1).capitalize = true;
    System.out.println(sentence);
  }
}

class Sentence {
  private Map<Integer, WordToken> tokens;
  private String sentence;
  public Sentence(String plainText) {
    tokens = new HashMap<>();
    sentence = plainText;
  }

  public WordToken getWord(int index) {
    WordToken token = new WordToken();
    tokens.put(index, token);
    return token;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    String[] words = sentence.split(" ");
    for(int i=0; i<words.length; i++) {
      if(tokens.containsKey(i) && tokens.get(i).capitalize) {
        sb.append(words[i].toUpperCase());
      } else {
        sb.append(words[i]);
      }
      if(i<words.length-1) {
        sb.append(" ");
      }
    }
    return sb.toString();
  }

  class WordToken {
    public boolean capitalize;
  }
}
