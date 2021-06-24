package de.pkro;

public class Main {

  public static void main(String[] args) {
    Account a = new Account();
    Command command = new Command(Command.Action.DEPOSIT, 100);
    a.process(command);
  }
}

class Command {
  public Action action;
  public int amount;
  public boolean success;

  public Command(Action action, int amount) {
    this.action = action;
    this.amount = amount;
  }

  enum Action {
    DEPOSIT,
    WITHDRAW
  }
}

class Account {
  public int balance;

  public void process(Command c) {
    switch (c.action) {
      case DEPOSIT:
        balance += c.amount;
        c.success = true;
        break;
      case WITHDRAW:
        if (balance - c.amount >= 0) {
          balance -= c.amount;
          c.success = true;
        } else {
          c.success = false;
        }
        break;
    }
  }
}
