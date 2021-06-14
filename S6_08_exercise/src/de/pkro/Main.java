package de.pkro;

import java.util.function.Supplier;

public class Main {

  public static void main(String[] args) {
    // write your code here
  }
}

class SingletonTester
{
  public static boolean isSingleton(Supplier<Object> func)
  {
    Object maybeSingleton = func.get();
    Object thisShouldBeTheSame = func.get();
    return maybeSingleton == thisShouldBeTheSame;
  }
}