package de.pkro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

  public static void main(String[] args) {
    ExpressionProcessor ep = new ExpressionProcessor();
    ep.variables.put('x', 5);
    System.out.println(ep.calculate("1"));
    System.out.println(ep.calculate("1+2")); // 3
    System.out.println(ep.calculate("1+x")); // 6
    System.out.println(ep.calculate("11+2+4")); // 6
    System.out.println(ep.calculate("1+xy")); // 0
  }
}

class ExpressionProcessor {
  public Map<Character, Integer> variables = new HashMap<>();

  // parse
  public int calculate(String expression) {
    List<Token> tokens = lex(expression);
    if (tokens.size() == 1) {
      return Integer.parseInt(tokens.get(0).value);
    }
    int result = 0;
    for (int i = 1; i < tokens.size() - 1; i++) {
      switch (tokens.get(i).type) {
        case PLUS:
          result =
              result
                  + Integer.parseInt(tokens.get(i - 1).value)
                  + Integer.parseInt(tokens.get(i + 1).value);
          i++;
          break;
        case MINUS:
          result =
              result
                  + Integer.parseInt(tokens.get(i - 1).value)
                  - Integer.parseInt(tokens.get(i + 1).value);
          i++;
          break;
        case INTEGER:
          break;
      }
    }
    return result;
  }

  private List<Token> lex(String expression) {
    List<Token> tokens = new ArrayList<>();
    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);
      if (c == '+') {
        tokens.add(new Token(Token.Type.PLUS, "+"));
      } else if (c == '-') {
        tokens.add(new Token(Token.Type.MINUS, "-"));
      } else if (!Character.isDigit(c)) {
        /*        if (Character.isLetter(expression.charAt(i + 1))) {
          // 2 characters in a row are invalid as per exercise text
          return new ArrayList<Token>();
        }*/
        if (variables.containsKey(c)) {
          tokens.add(new Token(Token.Type.INTEGER, "" + variables.get(c)));
        } else {
          // variable does not exist in lookup, return empty list
          return new ArrayList<Token>();
        }
      } else if (Character.isDigit(c)) {
        StringBuilder sb = new StringBuilder();
        for (int j = i; j < expression.length(); j++) {
          if (Character.isDigit(expression.charAt(j))) {
            sb.append(expression.charAt(j));
            i = j;
          } else {
            break;
          }
        }
        tokens.add(new Token(Token.Type.INTEGER, sb.toString()));
      }
    }

    return tokens;
  }
}

class Token {
  public Type type;
  public String value;

  public Token(Type type, String value) {
    this.type = type;
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }

  public enum Type {
    PLUS,
    MINUS,
    INTEGER
  }
}
