package de.pkro;

import java.io.File;
import java.io.IOException;

public class Main {

  public static void main(String[] args) {
    // write your code here
  }
}

class StaticBlockSingleton {
  private static StaticBlockSingleton instance;

  // catches exception from
  static {
    try {
      instance = new StaticBlockSingleton();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private StaticBlockSingleton() throws IOException {
    System.out.println("Singleton is initializing");
    File.createTempFile(".", "."); // invalid, throws exception
  }
}
