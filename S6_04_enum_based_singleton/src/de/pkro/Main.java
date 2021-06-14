package de.pkro;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

enum EnumBasedSingleton {
  INSTANCE;

  private int value;

  // always private, no public constructor for enum
  EnumBasedSingleton() {
    value = 42;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }
}

public class Main {

  public static void main(String[] args) throws Exception {
    EnumBasedSingleton singleton = EnumBasedSingleton.INSTANCE;
    //singleton.setValue(111);
    System.out.println(singleton.getValue());
    //saveToFile(singleton, "enum.bin");
    EnumBasedSingleton singleton1 = readFromFile("enum.bin");
    System.out.println(singleton1.getValue());
  }

  static void saveToFile(EnumBasedSingleton singleton, String filename) throws Exception {
    try (FileOutputStream fileOut = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
      out.writeObject(singleton);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static EnumBasedSingleton readFromFile(String filename) throws Exception {
    try (FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fileIn)) {
      return (EnumBasedSingleton) in.readObject();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
