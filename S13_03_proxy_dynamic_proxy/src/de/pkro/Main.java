package de.pkro;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

interface Human {
  void walk();

  void talk();
}

public class Main {

  public static void main(String[] args) {
    Person person = new Person();
    Human logged = withLogging(person, Human.class);
    logged.talk();
    logged.walk();
    logged.talk();
    System.out.println(logged.toString()); // {talk=2, walk=1}
  }

  // utility function to create a proxy with logging for any kind of object
  @SuppressWarnings("unchecked") // just for the IDE to stop underlying shit with generics
  // a static object of type T that takes in type T and returns type T, basically a replacement for the object?
  // so basically this?
  // public static Human withLogging(Human target, Class<Human> itf)
  public static <T> T withLogging(T target, Class<T> itf) {
    return (T) // unchecked cast
        Proxy.newProxyInstance(
                // Class<?>[] = array of Class objects (Classes are objects, too!)
            itf.getClassLoader(), new Class<?>[] {itf}, new LoggingHander(target));
  }
}

class Person implements Human {
  @Override
  public void walk() {
    System.out.println("I am walking");
  }

  @Override
  public void talk() {
    System.out.println("I am talking");
  }
}

class LoggingHander implements InvocationHandler {
  private final Object target;
  // this is the specific functionality we want to add here, count method calls
  private Map<String, Integer> calls = new HashMap<>();

  public LoggingHander(Object target) {
    this.target = target;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String name = method.getName();
    // intercept and count are 2 different functionalities we want to implement
    // here in the example and don't depend on each other

    // intercept
    if (name.contains("toString")) {
      return calls.toString();
    }
    // count
    calls.merge((name), 1, Integer::sum);
    return method.invoke(target, args);
  }
}
