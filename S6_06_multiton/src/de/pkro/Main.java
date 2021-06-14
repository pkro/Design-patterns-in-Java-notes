package de.pkro;

import java.util.HashMap;

public class Main {
  public static void main(String[] args) {
    Printer primary = Printer.get(Subsystem.PRIMARY);
    Printer primary2 = Printer.get(Subsystem.PRIMARY);
    System.out.println(primary==primary2); // true
    Printer fallback = Printer.get(Subsystem.FALLBACK);

  }
}

enum Subsystem {
  PRIMARY,
  AUXILIARY,
  FALLBACK
}

class Printer {
  private static int instanceCount = 0;
  private Printer() {
    instanceCount++;
    System.out.println(instanceCount);
  }

  private static HashMap<Subsystem, Printer> instances = new HashMap<>();

  public static Printer get(Subsystem ss) {
    if(instances.containsKey(ss)) {
      return instances.get(ss);
    }
    Printer instance = new Printer();
    instances.put(ss, instance);
    return instance;
  }
}