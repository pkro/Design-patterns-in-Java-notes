package de.pkro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) {
    // write your code here
  }
}


class Token {
  public int value = 0;

  public Token(int value) {
    this.value = value;
  }
}

class Memento {
  public List<Token> tokens = new ArrayList<>();
}

class TokenMachine {
  public List<Token> tokens = new ArrayList<>();

  public Memento addToken(int value) {
    return addToken(new Token(value));

  }

  public Memento addToken(Token token) {
    tokens.add(token);
    Memento m =  new Memento();
    for(Token t: tokens) {
      m.tokens.add(new Token(t.value));
    }
    return m;
  }

  public void revert(Memento m) {
    tokens.clear();
    tokens.addAll(m.tokens);
  }
}