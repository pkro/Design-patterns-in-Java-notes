package de.pkro;

public class Main {

  public static void main(String[] args) {
    BankAccount bankAccount = new BankAccount();
    Memento state1 = bankAccount.deposit(300);
    Memento state2 = bankAccount.deposit(250);
    System.out.println(bankAccount);
    bankAccount.restore(state1);
    System.out.println(bankAccount);
  }
}

class Memento { // or BankAccountToken
  private int balance; // immutable, no setter

  public Memento(int balance) {
    this.balance = balance;
  }

    public int getBalance() {
        return balance;
    }
}

class BankAccount {
  private int balance;

  public BankAccount() {
    this.balance = 0;
  }

  public Memento deposit(int amount) {
    balance += amount;
    return new Memento(balance);
  }

  public void restore(Memento m) {
    this.balance = m.getBalance();
  }


  @Override
  public String toString() {
    return "BankAccount{" + "balance=" + balance + '}';
  }
}
