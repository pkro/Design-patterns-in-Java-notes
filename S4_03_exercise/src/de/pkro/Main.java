package de.pkro;

public class Main {

  public static void main(String[] args) {
    // write your code here
  }
}

class Person
{
  public int id;
  public String name;

  public Person(int id, String name)
  {
    this.id = id;
    this.name = name;
  }
}

class PersonFactory
{
  private int index = 0;
  public Person createPerson(String name)
  {
    return new Person(index++, name);
  }
}
