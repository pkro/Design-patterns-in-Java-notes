package de.pkro;

enum Trigger {
  DIGIT_ENTERED
}

enum State {
  OPEN,
  ERROR,
  LOCKED
}

public class Main {

  public static void main(String[] args) {
    CombinationLock cl = new CombinationLock(new int[] {1, 2, 3, 4});
    System.out.println(cl.status); // LOCKED
    cl.enterDigit(1);
    System.out.println(cl.status); // 1

    cl.enterDigit(2);
    System.out.println(cl.status); // 12

    cl.enterDigit(3);
    System.out.println(cl.status); // 123

    cl.enterDigit(4);
    System.out.println(cl.status); // OPEN

    CombinationLock cl2 = new CombinationLock(new int[] {1, 2, 3, 4});
    cl2.enterDigit(5);
    System.out.println(cl2.status); // ERROR
  }
}

class CombinationLock {
  public String status;
  private int[] combination;

  public CombinationLock(int[] combination) {
    this.combination = combination;
    this.status = State.LOCKED.name();
  }

  public void enterDigit(int digit) {

    if (!status.equals(State.ERROR.name())) {
      if(status.equals(State.LOCKED.name())) {
        status = "";
      }
      int currentDigitIdx = status.length();
      if (digit == combination[currentDigitIdx]) {
        status = status + "" + digit;
        if (combination.length == status.length()) {
          status = State.OPEN.name();
        }
      } else {
        status = State.ERROR.name();
      }
    }
  }
}
