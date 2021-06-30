package de.pkro;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

interface Element {
  int eval();
}

public class Main {

  public static void main(String[] args) {
    String input = "(13+4)-(12+1)";
    List<Token> tokens = lex(input);

    System.out.println(tokens.stream().map(t -> t.toString()).collect(Collectors.joining("\t")));

    Element parsed = parse(tokens);
    System.out.println(input + " = " + parsed.eval());
  }

  // Lexing
  static List<Token> lex(String input) {
    ArrayList<Token> result = new ArrayList<>();
    for (int i = 0; i < input.length(); i++) {
      switch (input.charAt(i)) {
        case '+':
          result.add(new Token(Token.Type.PLUS, "+"));
          break;
        case '-':
          result.add(new Token(Token.Type.MINUS, "-"));
          break;
        case '(':
          result.add(new Token(Token.Type.LPAREN, "("));
          break;
        case ')':
          result.add(new Token(Token.Type.RPAREN, ")"));
          break;
        default: // it's a number
          StringBuilder sb = new StringBuilder("" + input.charAt(i));
          for (int j = i + 1; i < input.length(); j++) {
            if (Character.isDigit(input.charAt(j))) {
              sb.append(input.charAt(j));
              i++; // increment outer counter too
            } else {
              result.add(new Token(Token.Type.INTEGER, sb.toString()));
              break;
            }
          }
          break;
      }
    }
    return result;
  }

  // Parsing
  static Element parse(List<Token> tokens) {
    BinaryOperation result = new BinaryOperation();
    boolean haveLeftHandSide = false;

    for (int i = 0; i < tokens.size(); i++) {
      Token token = tokens.get(i);
      switch (token.type) {
          // no case RPARENthesis as it's handled by the opening LPAREN
        case INTEGER:
          Integer integer = new Integer(java.lang.Integer.parseInt(token.text));
          if (!haveLeftHandSide) {
            result.left = integer;
            haveLeftHandSide = true;
          } else {
            result.right = integer;
          }
          break;
        case PLUS:
          result.type = BinaryOperation.Type.ADDITION;
          break;
        case MINUS:
          result.type = BinaryOperation.Type.SUBSTRACTION;
          break;
        case LPAREN:
          int j = i; // location of right parenthesis
          for (; j < tokens.size(); j++) {
            if (tokens.get(j).type == Token.Type.RPAREN) {
              break;
            }
          }
          List<Token> subexpression =
              tokens.stream()
                      .skip(i + 1)
                      .limit(j - i - 1)
                      .collect(Collectors.toList());
          Element element = parse(subexpression); // recursively parse
          if (!haveLeftHandSide) {
            result.left = element;
            haveLeftHandSide = true;
          } else {
            result.right = element;
          }
          i = j; // continue after the subexpression
          break;
      }
    }

    return result;
  }
}

class Token {
  public Type type;
  public String text;

  public Token(Type type, String text) {
    this.type = type;
    this.text = text;
  }

  @Override
  public String toString() {
    return "`" + text + "`";
  }

  public enum Type {
    INTEGER,
    PLUS,
    MINUS,
    LPAREN,
    RPAREN
  }
}

class Integer implements Element {
  private int value;

  public Integer(int value) {
    this.value = value;
  }

  @Override
  public int eval() {
    return value;
  }
}

class BinaryOperation implements Element {
  public Type type;
  public Element left, right; // elements on the left and right of operator

  @Override
  public int eval() {
    switch (type) {
      case ADDITION:
        return left.eval() + right.eval();
      case SUBSTRACTION:
        return left.eval() - right.eval();
      default:
        return 0;
    }
  }

  public enum Type {
    ADDITION,
    SUBSTRACTION
  }
}
