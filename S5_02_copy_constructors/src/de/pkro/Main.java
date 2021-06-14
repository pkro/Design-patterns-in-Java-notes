package de.pkro;

public class Main {

  public static void main(String[] args) {
    Employee employee = new Employee("Peer", new Address("abcstr. 5", "offebach", "DE"));
    Employee chris = new Employee(employee);
    chris.name = "Chris";

    System.out.println(employee);
    System.out.println(chris);
  }
}

class Address {
  public String streetAddress, city, country;

  public Address(String streetAddress, String city, String country) {
    this.streetAddress = streetAddress;
    this.city = city;
    this.country = country;
  }

  // copy constructor
  public Address(Address other) {
    this(other.streetAddress, other.city, other.country);
  }

  @Override
  public String toString() {
    return "Address{"
        + "streetAddress='"
        + streetAddress
        + '\''
        + ", city='"
        + city
        + '\''
        + ", country='"
        + country
        + '\''
        + '}';
  }
}

class Employee {
  public String name;
  public Address address;

  public Employee(String name, Address address) {
    this.name = name;
    this.address = address;
  }

  public Employee(Employee other) {
    name = other.name;
    address = new Address(other.address);
  }

  @Override
  public String toString() {
    return "Employee{" + "name='" + name + '\'' + ", address=" + address + '}';
  }
}