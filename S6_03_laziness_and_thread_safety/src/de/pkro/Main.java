package de.pkro;

public class Main {

  public static void main(String[] args) {
    LazySingleton lazySingleton = LazySingleton.getInstance();
    System.out.println(lazySingleton);
  }
}

class LazySingleton {
  private static LazySingleton instance;

  private LazySingleton() {
    System.out.println("initializing a lazy singleton");
  }

  public static LazySingleton getInstance() {
    if (instance == null) {
      System.out.println("Instantiating a lazy singleton");
      instance = new LazySingleton();
    }
    return instance;
  }
}

class InnerStaticSingleton {
  private InnerStaticSingleton() {}

  private static class Impl {
    public static final InnerStaticSingleton INSTANCE = new InnerStaticSingleton();
  }

  public InnerStaticSingleton getInstance() {
    return Impl.INSTANCE;
  }
}
