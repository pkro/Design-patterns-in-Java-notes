package de.pkro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class HtmlElement {
  private final int indentSize = 2;
  private final String newLine = System.lineSeparator();
  public String name, text;
  public List<HtmlElement> elements = new ArrayList<>();

  public HtmlElement() {}

  public HtmlElement(String name, String text) {
    this.name = name;
    this.text = text;
  }

  private String toStringImpl(int indent) {
    StringBuilder sb = new StringBuilder();
    String i = repeatSpace(indent);
    sb.append(String.format("%s<%s>%s", i, name, newLine));

    if (text != null && !text.isEmpty()) {
      sb.append(String.join("", repeatSpace(indent + 1))).append(text).append(newLine);
    }

    for (HtmlElement e : elements) {
      sb.append(e.toStringImpl(indent + 1));
    }

    sb.append(String.format("%s</%s>%s", i, name, newLine));

    return sb.toString();
  }

  private String repeatSpace(int indent) {
    return String.join("", Collections.nCopies(indentSize * (indent + 1), " "));
  }

  @Override
  public String toString() {
    return toStringImpl(indentSize);
  }
}

class HtmlBuilder {
  private String rootName;
  private HtmlElement root = new HtmlElement();

  public HtmlBuilder(String rootName) {
    this.rootName = rootName;
    root.name = rootName;
  }

  public HtmlBuilder addChild(String childName, String childText) {
    HtmlElement e = new HtmlElement(childName, childText);
    root.elements.add(e);
    return this;
  }

  public void clear() {
    root = new HtmlElement();
    root.name = rootName;
  }

  // the method that builds and returns the final element
  @Override
  public String toString() {
    return root.toString();
  }
}

public class Main {
  public static void main(String[] args) {
    /*String hello = "hello";
    System.out.println("<p>" + hello + "</p>");

    String[] words = {"hello", "world"};

    StringBuilder sb = new StringBuilder();
    sb.append("<ul>\n");
    for(String word : words) {
      sb.append(String.format(" <li>%s</li>\n", word));
    }
    sb.append("</ul>");

    System.out.println(sb);*/

    HtmlBuilder builder = new HtmlBuilder("ul").addChild("li", "hello").addChild("li", "there");
    System.out.println(builder);
  }
}
