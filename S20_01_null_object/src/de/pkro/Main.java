package de.pkro;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

interface Log {
  void info(String msg);

  void warn(String msg);
}

public class Main {

  public static void main(String[] args) {
    ConsoleLog c = new ConsoleLog();
    BankAccount account = new BankAccount(c);
    account.deposit(500);

    // Problem: if we don't want logging, we can't just pass null as
    // the logger to BankAccount as this would cause exceptions
    // (without checking for null on every call)
    // Solution: NullLog class that has empty function bodies
    // (but what if the methods return something? I guess then we should
    // make sure then that we don't pass null or nullobject...)

    NullLog nl = new NullLog();
    BankAccount notlogged = new BankAccount(nl);
    notlogged.deposit(500);

    // Dynamic null object using dynamic proxy (noOp method below)
    Log noOp = noOp(Log.class);
    BankAccount accountUsingNoOpProxy = new BankAccount(noOp);
    accountUsingNoOpProxy.deposit(500);

  }

  @SuppressWarnings("unchecked")
  public static <T> T noOp(Class<T> itf) {
    // build fake object that conforms to given interface
    return (T)
        Proxy.newProxyInstance(
            itf.getClassLoader(),
            new Class<?>[] {itf},
            (proxy, method, args) -> {
              if (method.getReturnType().equals(Void.TYPE)) {
                return null;
              } else {
                return method.getReturnType().getConstructor().newInstance();
              }
            });
  }
}

// Null object
final class NullLog implements Log {
  @Override
  public void info(String msg) {
    // just leave blank
  }

  @Override
  public void warn(String msg) {}
}

class ConsoleLog implements Log {
  @Override
  public void info(String msg) {
    System.out.println(msg);
  }

  @Override
  public void warn(String msg) {
    System.out.println("Warn: " + msg);
  }
}

class BankAccount {
  private int balance = 0;
  private Log log;

  public BankAccount(Log log) {
    this.log = log;
  }

  public void deposit(int amount) {
    balance += amount;
    log.info("Deposited " + amount);
  }
}
