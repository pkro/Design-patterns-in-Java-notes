package de.pkro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) throws FileNotFoundException {
    Journal j = new Journal();
    j.addEntry("blah d");
    j.addEntry("fd fdsf sd");
    System.out.println(j);

    Persistence p = new Persistence();
    String filename = "journal.txt";
    p.save(j, filename, true);

    try {
      Runtime.getRuntime().exec("code " + filename);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

class Journal {
  private static int count = 0;
  private final List<String> entries = new ArrayList<>();

  public void addEntry(String text) {
    entries.add("" + (++count) + ": " + text);
  }

  public void removeEntry(int index) {
    entries.remove(index);
  }

  @Override
  public String toString() {
    return String.join(System.lineSeparator(), entries);
  }

  // breaks single responsibility principle
  // concern: persistence
  /*public void save(String filename) throws FileNotFoundException {
    try(PrintStream out = new PrintStream(filename)) {
      out.println(toString());
    }
  }
  public void load(String filename) {}
   */
}

// good separation for persistence
class Persistence {
  public void save(Journal journal, String filename, boolean overwrite)
      throws FileNotFoundException {
    if (overwrite || new File(filename).exists()) {
      try (PrintStream out = new PrintStream(filename)) {
        out.println(journal.toString());
      }
    }
  }

  public void load(String filename) {}

  public void load(URL url) {}
  // etc
}
