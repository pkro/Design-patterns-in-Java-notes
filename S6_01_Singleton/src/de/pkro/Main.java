package de.pkro;

import java.io.*;

public class Main {

  public static void main(String[] args) throws Exception {
    BasicSingleton b = BasicSingleton.getInstance();
    b.setValue(111);
    System.out.println(b.getValue());
    saveToFile(b, "singleton.bin");
    BasicSingleton c = readFromFile("singleton.bin");
    System.out.println(c.getValue());
  }

  static void saveToFile(BasicSingleton singleton, String filename) throws Exception {
    try (FileOutputStream fileOut = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
      out.writeObject(singleton);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static BasicSingleton readFromFile(String filename) throws Exception {
    try (FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fileIn)) {
      return (BasicSingleton) in.readObject();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}

class BasicSingleton implements Serializable {
  private static BasicSingleton instance;
  private int value = 0;

  private BasicSingleton() {}

  public static BasicSingleton getInstance() {
    if (instance == null) {
      instance = new BasicSingleton();
    }

    return instance;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  protected Object readResolve() {
    return instance;
  }
}
