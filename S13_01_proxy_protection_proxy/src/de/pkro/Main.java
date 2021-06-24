package de.pkro;

interface Drivable {
  void drive();
}

public class Main {
  public static void main(String[] args) {
    Car c1 = new Car(new Driver(15));
    c1.drive();
    // Car can be replaced in existing code with CarProxy and nothing will change for the user
    Car c2 = new CarProxy(new Driver(15));
    c2.drive();
  }
}

class Car implements Drivable {
  protected Driver driver;

  public Car(Driver driver) {
    this.driver = driver;
  }

  @Override
  public void drive() {
    System.out.println("Car is being driven");
  }
}

class Driver {
  public int age;

  public Driver(int age) {
    this.age = age;
  }
}

// we want something that behaves like a car but verifies that the driver
// is old enough to drive

class CarProxy extends Car {
  public CarProxy(Driver driver) {
    super(driver);
  }

  @Override
  public void drive() {
    if (driver.age > 16) {
      super.drive();
    } else {
      System.out.println("Driver is too young to drive");
    }
  }
}
