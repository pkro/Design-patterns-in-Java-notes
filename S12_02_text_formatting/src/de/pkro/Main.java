package de.pkro;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    FormattedText text = new FormattedText("Hello There");
    text.capitalize(7, 10);
    System.out.println(text);

    BetterFormattedText text2 = new BetterFormattedText("Hello There");
    text2.getRange(7, 10).capitalize = true;
    System.out.println(text2);
  }
}

// "Naive" implementation, storing uppercase information
// for every character in the string

class FormattedText {
  private String plainText;
  private boolean[] capitalize;

  public FormattedText(String plainText) {
    this.plainText = plainText;
    capitalize = new boolean[plainText.length()];
  }

  public void capitalize(int start, int end) {
    for (int i = start; i <= end; i++) {
      capitalize[i] = true;
    }
  }

  @Override
  public String toString() {
    char[] out = new char[plainText.length()];
    for (int i = 0; i < plainText.length(); i++) {
      if (capitalize[i]) {
        out[i] = Character.toUpperCase(plainText.charAt(i));
      } else {
        out[i] = plainText.charAt(i);
      }
    }
    return new String(out);
  }
}

class BetterFormattedText  {
  private String plainText;
  private List<TextRange> formatting = new ArrayList<>();

  public BetterFormattedText(String plainText) {
    this.plainText = plainText;
  }

  public TextRange getRange(int start, int end) {
    TextRange range = new TextRange(start, end);
    formatting.add(range);
    return range;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < plainText.length(); i++) {
     char c = plainText.charAt(i);
     for(TextRange range : formatting) {
       if(range.covers(i) && range.capitalize) {
         c = Character.toUpperCase(c);
       }
        sb.append(c);
     }

    }
    return sb.toString();
  }

  // this is the actual flyweight
  public class TextRange {
    public int start, end;
    boolean capitalize, bold, italic;

    public TextRange(int start, int end) {
      this.start = start;
      this.end = end;
    }

    public boolean covers(int position) {
      return position >= start && position <= end;
    }

  }
}