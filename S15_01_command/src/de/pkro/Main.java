package de.pkro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

interface Command {
  void call();

  void undo();
}

public class Main {

  public static void main(String[] args) {
    BankAccount ba = new BankAccount();
    System.out.println(ba);
    List<Command> commands =
        List.of(
            new BankAccountCommand(ba, BankAccountCommand.Action.DEPOSIT, 100),
            new BankAccountCommand(ba, BankAccountCommand.Action.WITHDRAW, 1000));

    for (Command c : commands) {
      c.call();
      System.out.println(ba);
    }

    // undo in reverse
    for (int i = commands.size() - 1; i >= 0; i--) {
      Command c = commands.get(i);
      c.undo();
      System.out.println(ba);
    }
  }
}

// just your normal bankaccount class
class BankAccount {
  private int balance;
  private int overdraftLimit = -500;

  public BankAccount() {
    this.balance = 0;
  }

  public boolean deposit(int amount) {
    balance += amount;
    System.out.println("Deposited " + amount + "; balance: " + balance);
    return true;
  }

  public boolean withdraw(int amount) {
    if (balance - amount >= overdraftLimit) {
      balance -= amount;
      System.out.println("Withdrew " + amount + "; balance: " + balance);
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return "BankAccount{" + "balance=" + balance + '}';
  }
}

class BankAccountCommand implements Command {
  private BankAccount account;
  private Action action;
  private int amount;
  private boolean succeeded;

  public BankAccountCommand(BankAccount account, Action action, int amount) {
    this.account = account;
    this.action = action;
    this.amount = amount;
  }

  @Override
  public void call() {
    switch (action) {
      case DEPOSIT:
        succeeded = account.deposit(amount);
        break;
      case WITHDRAW:
        succeeded = account.withdraw(amount);
        break;
    }
  }

  // WRONG (because withdraw might not succeed if overdrawn)
  @Override
  public void undo() {
    switch (action) {
      case DEPOSIT:
        if (succeeded) {
          account.withdraw(amount);
        }
        break;
      case WITHDRAW:
        if (succeeded) {
          account.deposit(amount);
        }
        break;
    }
  }

  public enum Action {
    DEPOSIT,
    WITHDRAW
  }
}
