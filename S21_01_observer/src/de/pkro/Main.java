package de.pkro;

import java.util.ArrayList;
import java.util.List;

interface Observer<T> {
  void handle(PropertyChangedEventArgs<T> args);
}

public class Main implements Observer<Person>{

  public static void main(String[] args) {
    new Main();
  }

  public Main() {
    Person person = new Person();
    person.subscribe(this);
    for (int i = 20; i < 25; i++) {
      person.setAge(i);
    }
  }

  @Override
  public void handle(PropertyChangedEventArgs<Person> args) {
    System.out.println(args.propertyName + " changed to " + args.newValue + " by " + args.source);
  }
}

class PropertyChangedEventArgs<T> {
  public T source;
  public String propertyName;
  public Object newValue;

  public PropertyChangedEventArgs(T source, String propertyName, Object newValue) {
    this.source = source;
    this.propertyName = propertyName;
    this.newValue = newValue;
  }
}

class Observable<T> {
  private List<Observer<T>> observers = new ArrayList<>();

  public void subscribe(Observer<T> observer) {
    observers.add(observer);
  }

  protected void propertyChanged(T source, String propertyName, Object newValue) {
    for (Observer<T> o : observers) {
      o.handle(new PropertyChangedEventArgs<>(source, propertyName, newValue));
    }
  }
}

class Person extends Observable<Person>{

  // prnoperty age = field "age" + getter + setter
  private int age;

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    if(this.age == age) return;
    this.age = age;
    propertyChanged(this, "age", age);
  }
}
