package de.pkro;

public class Main {
  public static void main(String[] args) {
    EmployeeBuilder person = new EmployeeBuilder();
    Person d = person.withName("Dimitri").worksAt("blah company").build();
  }
}

class Person {
  String name;
  String position;

  @Override
  public String toString() {
    return "Person{" + "name='" + name + '\'' + ", position='" + position + '\'' + '}';
  }
}

class PersonBuilder<SELF extends PersonBuilder<SELF>> {
  protected Person person = new Person();

  public SELF withName(String name) {
    person.name = name;
    return self();
  }

  public Person build() {
    return person;
  }

  protected SELF self() {
    return (SELF) this; // warning underline is a limitation of IDE
  }
}

class EmployeeBuilder extends PersonBuilder<EmployeeBuilder> {
  public EmployeeBuilder worksAt(String position) {
    person.position = position;
    return self(); // Problem: returns Employeebuilder
  }

  @Override
  protected EmployeeBuilder self() {
    return this;
  }
}