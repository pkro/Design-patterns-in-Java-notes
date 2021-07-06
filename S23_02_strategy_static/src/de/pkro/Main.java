package de.pkro;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

enum OutputFormat {
  MARKDOWN,
  HTML
}

/*
Lists in HTML:
<ul><li>item1</li><li>item2</li></ul>

Lists in MARKDOWN:
* item1
* item2
*/
interface ListStrategy {
  default void start(StringBuilder sb) {}
  ; // not applicable for markdown type list

  void addListItem(StringBuilder sb, String item);

  default void end(StringBuilder sb) {}
  ; // not applicable for markdown type list
}

class MarkdownListStrategy implements ListStrategy {
  @Override
  public void addListItem(StringBuilder sb, String item) {
    sb.append("* ").append(item).append(System.lineSeparator());
  }
}

class HtmlListStrategy implements ListStrategy {
  @Override
  public void start(StringBuilder sb) {
    sb.append("<ul>");
  }

  @Override
  public void addListItem(StringBuilder sb, String item) {
    sb.append("<li>").append(item).append("</li>");
  }

  @Override
  public void end(StringBuilder sb) {
    sb.append("</ul>");
  }
}

class TextProcessor<LS extends ListStrategy> {
  private StringBuilder sb = new StringBuilder();
  private LS listStrategy;

  public TextProcessor(Supplier<? extends LS> ctor) {
    this.listStrategy = ctor.get();
  }

  public void addItem(String bulletPoint) {
    listStrategy.addListItem(sb, bulletPoint);
  }

  public void addList(List<String> items) {
    listStrategy.start(sb);
    for (String item : items) {
      addItem(item);
    }
    listStrategy.end(sb);
  }

  public void clear() {
    sb.setLength(0);
  }

  @Override
  public String toString() {
    return sb.toString();
  }
}

public class Main {

  public static void main(String[] args) {
    TextProcessor<MarkdownListStrategy> tp = new TextProcessor<>(MarkdownListStrategy::new);
    tp.addList(List.of("alpha", "beta", "gama"));
    System.out.println(tp);
  }
}
