package de.pkro;

import java.util.Arrays;

public class Main {

  public static void main(String[] args) throws CloneNotSupportedException{
    Person john = new Person(new String[] {"John", "Smith"}, new Address("London Road", 123));

    // make another person who lives at same address
    // Person jane = john; // obviously assigns the reference the same object, so this doesn't work
    Person jane = (Person) john.clone();
    jane.names[0] = "Jane";
    jane.address.houseNumber = 124;
    System.out.println(john);
    System.out.println(jane);
  }
}

class Address implements Cloneable {
  public String streetName;
  public int houseNumber;

  public Address(String streetName, int houseNumber) {
    this.streetName = streetName;
    this.houseNumber = houseNumber;
  }

  @Override
  public String toString() {
    return "Address{" + "streetName='" + streetName + '\'' + ", houseNumber=" + houseNumber + '}';
  }

  // deep copy; Strings are immutable and ints are literals so we don't just pass a reference
  // when creating a new address
  @Override
  public Object clone() throws CloneNotSupportedException {
    return new Address(streetName, houseNumber);
  }
}

class Person implements Cloneable {
  public String[] names;
  public Address address;

  public Person(String[] names, Address address) {
    this.names = names;
    this.address = address;
  }

  @Override
  public String toString() {
    return "Person{" + "names=" + Arrays.toString(names) + ", address=" + address + '}';
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    //return new Person(names, address); // problem as names and address are just references to the same object
    return new Person(names.clone(), (Address) address.clone());
  }
}
