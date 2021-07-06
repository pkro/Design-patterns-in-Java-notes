package de.pkro;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Main {

  public static void main(String[] args) {
    Person person = new Person();
    Event<PropertyChangedEventArgs>.Subscription sub =
        person.propertyChanged.addHandler(
            x -> {
              System.out.println("Persons " + x.propertyName + " changed to " + person.getAge());
            });

    person.setAge(17);
    person.setAge(28);
    sub.close();
    person.setAge(30);
  }
}

class Event<TArgs> {
  private int count = 0;
  private Map<Integer, Consumer<TArgs>> handlers = new HashMap<>(); // subscribers

  // Consumer: Represents an operation that accepts a single input argument
  // and returns no result. Unlike most other functional interfaces,
  // Consumer is expected to operate via side-effects.
  public Subscription addHandler(Consumer<TArgs> handler) {
    int i = count;
    handlers.put(count++, handler);
    return new Subscription(this, i);
  }

  public void fire(TArgs args) {
    for (Consumer<TArgs> handler : handlers.values()) {
      handler.accept(args);
    }
  }
  // AutoClosable so we can put it in try with ressources construct
  public class Subscription implements AutoCloseable {
    private Event<TArgs> event;
    private int id; // index of the handlers map in event class to remove subscription on close

    public Subscription(Event<TArgs> event, int id) {
      this.event = event;
      this.id = id;
    }

    @Override
    public void close() /*throws Exception*/ {
      event.handlers.remove(id);
    }
  }
}

class PropertyChangedEventArgs {
  public Object source;
  public String propertyName;

  public PropertyChangedEventArgs(Object source, String propertyName) {
    this.source = source;
    this.propertyName = propertyName;
  }
}

class Person {
  public Event<PropertyChangedEventArgs> propertyChanged = new Event<>();
  private int age;

  public int getAge() {
    return age;
  }

  // the canvote stuff is only to show that things get more complicated
  // with dependent properties and not the point of the example
  public void setAge(int age) {
    boolean oldCanVote = getCanVote();
    if (this.age == age) {
      return;
    }
    this.age = age;
    propertyChanged.fire(new PropertyChangedEventArgs(this, "age"));

    if (oldCanVote != getCanVote()) {
      propertyChanged.fire(new PropertyChangedEventArgs(this, "canvote"));
    }
  }

  public boolean getCanVote() {
    return age >= 18;
  }
}
