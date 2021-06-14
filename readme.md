# Design patterns in Java

Notes on udemy course of the same name by Dmitri Nesteruk

## Unrelated intellij notes

- Just typing `new Person("John")`, then `str-alt-v` to introduce local variable to turn it into `Person john = new Person("John")`
- typing an expression with .var at the end, e.g. `"fdf".var` creates whole `String varname = "fdf"`

## SOLID principles

Design principles by Robert C. Martin

Examples see Java projects of same name

### Single Responsibility principle (SRP)

- Classes should have one responsibility / concern

### Open-Closed principle + Specification enterprise design pattern (OCP)

Classes should be

- open for extension (inherit from interfaces etc),
- closed for modification (don't change existing code like in the filter class in the example code)

**Specification design pattern** 

used in example: [business rules can be recombined by chaining the business rules together using boolean logic.](https://en.wikipedia.org/wiki/Specification_pattern)

### Liskov Substitution Principle (LSP)

You should be able to substitute a base type for a subtype, meaning a derived (sub-)class should be substitutable by a base class.

Example in code: Square inherits from Rectangle, but Square's setters set both width and height in the overridden
setwidth and setheight methods, which can lead to unexpected results

(One) solution: don't subclass Rectangle but create a Factory that can create squares and rectangles:

    class RectangleFactory {
      public static Rectangle newRectangle(int width, int height) {
        return new Rectangle(width, height);
      }
      public static Rectangle newSquare(int width) {
        return new Rectangle(width, width);
      }
    }

### Interface Segregation principle (ISP)

- Recommendation how to split interfaces into smaller interfaces
- Smaller interfaces can be bundled in other interfaces, e.g.

    interface MultiFunctionDevice extends Printer, Scan {}

YAGNI:  You Ain't Going to Need It - don't implement methods that aren't needed

### Dependeny Inversion principle (DIP)

- High level modules should not depend on low level modules or implementation details like the type of list used; both should depend on abstractions (abstract class or interface)
  - High level module: e.g. containing business logic
  - Low level module: e.g. pure data storage handling modules
- Abstractions should not depend on details, details should depend on abstractions (if you can use abstract classes or interfaces)
- Is this just "program to an interface, not an implementation"?

## Gamma Categorization

Design patterns are split into 3 categories:

### Creational patterns

- deal with creation of objects
- explicit (constructor) vs. implicit (dependency injection, reflection etc.)
- wholesale single statement vs. piecewise (step by step) initialization

### Structural patterns

- concerned with structure of classes such as class memebers
- Many patterns are wrappers that mimic the underlying class' interface
- Stress the importance of good API design

### Behavioral patterns

- no central theme


## Builder pattern (creational)

*When piecewise object construction is complicated, provide an API for doing it succinctly*

- simple objects can be instantiated in a single constructor call
- other objects require more (e.g. StringBuilder)
- It's better to have a piece by piece construction than 10 constructor arguments
- **Builder** provides an API for constructing an object step by step.

Every builder has to have a method that returns the final object, explicitely or implicitely (like StringBuilder.toString).

Fluent interface: returning reference to the object (`this`) from all methods so methods can be chained, e.g.

    new StringBuilder("test")
        .append("one")
        .append("two");

Problem when a builder inherits from another builder for a fluent interface: subclassed builders return different types with `this`

Solution: 

### Java Recursive Generics

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

### Faceted builder

When an object is too complicated, sometimes multiple builders are necessary.

For example, an object to search for holiday offers can contain topically independent blocks of information such as family name, # of persons, children etc. and another block with info regarding the desired locations, holiday lengths, min hotel stars etc.

See very clear and nice code in S3_03

### Summary:

- A builder is a component with the only purpose to build an object of some class
- A builder can be given a constructor or return it via static function; 

> >One example would be when a builder is a nested class within the object it builds. But generally, this is a stylistic choice: do what comes naturally. If you like Person.create() use that, if not, just do new PersonBuilder(). (Course instructor Dmitri in discussion forum)

- To make it fluent, return `this` (or more sophisticated, see faceted builder code example)
- Different facets can be built with different builders working together using a base class

## Factories

*A component responsible solely for the wholesale (not piecewise) creation of objects* 

Motivations: make object creation easier (e.g. avoid constructor overloading hell) for objects not using a piece-wise builder pattern

- By a separate function or functions (factory method) that are not bound by the restrictions of constructors, meaning they can have different names and parameters
- Can exist in a separate class (Factors) - separation of concers
- can create hierarchy of factories with Abstract Factory

### Factory method using static factory methods (simplest way)
    
    class Point {
      private double x, y;
    
      // can / should (if factories should be enforced) be private
      private Point(double x, double y) {
        this.x = x;
        this.y = y;
      }
    
      // problem: we want to have a constructor using polar coordinates like
      // Point(double rho, double theta)
      // we could add a parameter that gives the appropriate coord system, but then
      // we are still bound to 2 double values, which might not work
      // for all ways to construct a point; also, parameters can't have fitting names for their function
      // for each constructor version
      public static Point fromPolar(double rho, double theta) {
        double x = rho * Math.cos(theta);
        double y = rho * Math.sin(theta);
        return new Point(x, y);
      }
    
      public static Point fromCartesian(double x, double y) {
        return new Point(x, y);
      }
    }

### Factory (own class with factory methods)

If there are many factory methods, they should / can be put into their own factory class.

Problem: for the external factory class to be able to create objects of another class, the other class' constructor must be public; that way there would be multiple ways to instantiate an object, which we might not want.
The only solution for this is to make the factory an inner class:

    class Point {
      private double x, y;
    
      // can (only) remain private if factory is inner class
      private Point(double x, double y) {
        this.x = x;
        this.y = y;
      }
    
      public static class Factory {
        public static Point fromPolar(double rho, double theta) {
          double x = rho * Math.cos(theta);
          double y = rho * Math.sin(theta);
          return new Point(x, y);
        }
    
        public static Point fromCartesian(double x, double y) {
          return new Point(x, y);
        }
      }
    }

### Abstract factory

- uncommon design pattern
- basically uses reflection to see which implementations of a base factory exist. I think. 
- see s4_02_abstract_factories

### Summary

- a factory method is a static method that creates objects
- a factory takes care of object creation
- can be external or inner class of the object to be created
- Hierarchies of factories can be used to create related objects

## Prototype

*A partially or fully initialized object that is copied (cloned)*

Copying and customizing an existing object instead of instantiating.

### Motivation:

- reiterate existing design
- complicated objects with complicated instantiation (e.g. by a builder), where only variations with some changes are required
- Requires deep copy (clone)
- Needs to be convenient

### Don't use clone

- Obviously just using `=` just assigns a refference to the same object, so this doesn't work
- The interface `Cloneable` is NOT RECOMMENDED as it's an empty interface with no members / methods defined (but there is `clone()`? What does "no members defined" mean?) and more of a hint, and doesn't enforce a specific way of cloning (deep/shallow)

### Copy constructors

- create an object by passing an existing object of the same type to the constructor:

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
      // ...

      Employee employee = new Employee("Peer", new Address("abcstr. 5", "offebach", "DE"));
      Employee chris = new Employee(employee);

- Problem: a copy constructor needs to be build for every type in the object to clone (as otherwise parts could be a shallow copy)


### Copy through serialization

- using apache commons, which does a deepcopy:

      import org.apache.commons.lang3.SerializationUtils;
      import java.io.Serializable;
      
      public class Main {
        public static void main(String[] args) {
          Foo foo = new Foo(42, "life");
          Foo foo2 = SerializationUtils.roundtrip(foo);
        }
      }
      
      class Foo implements Serializable {
        public int stuff;
        public String whatever;
      
        public Foo(int stuff, String whatever) {
          this.stuff = stuff;
          this.whatever = whatever;
        }
      }

- using a library that uses reflections (then implementing Serializable is not necessary)

### Implement own deep copy functionality

- what it says, see S5_04_exercise

## Singleton

*A component which is instantiated only once and resists to be instantiated more than once*

- controversial if it's a good pattern or an anti-pattern

Motivation: 

- for some components it makes sense to have only one instance 
  - Database repository
  - Object factory
- expensive constructor call
- lazy instantiation and thread safety

### Basic singleton with lazy instantiation

    class BasicSingleton {
      
      // for immediate instantiation when the class is loaded by
      // the runtime, we could also do
      // private static final BasicSingleton instance = new BasicSingleton()
      private static BasicSingleton instance;

      // private constructor makes instantiation with "new" impossible
      // outside the class
      private BasicSingleton() {}
    
      public static BasicSingleton getInstance() {
        // instantiate only when needed (lazy)
        if(instance == null) {
          instance = new BasicSingleton();
        }
        return instance;
      }
    }

Problems:

- private constructor can be (conciously) defeated with reflection
- serialization (was) possible; if the Singleton implements `Serializable` interface, 2 instances could be created by writing and reading in an object via objectInputStream; can be resolve by overriding `readResolve`method:
  > readResolve is used for replacing the object read from the stream. The only use I've ever seen for this is enforcing singletons; when an object is read, replace it with the singleton instance. This ensures that nobody can create another instance by serializing and deserializing the singleton. (https://stackoverflow.com/questions/1168348/java-serialization-readobject-vs-readresolve)
- both are not really big problems as the first requires intention to misuse the class, the other is easily resolved by either NOT implementing `Serializable` or returning the instance in the `readResolve` method 

### Static block singleton

If the singleton constructor can throw an exception, the instantiation can be done in a static block:

    class StaticBlockSingleton {
      private static StaticBlockSingleton instance;
    
      // catches exception from constructor
      // static blocks are executed on class load
      static {
        try {
          instance = new StaticBlockSingleton();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    
      private StaticBlockSingleton() throws IOException {
        System.out.println("Singleton is initializing");
        File.createTempFile(".", "."); // invalid, throws exception
      }
    }

### Laziness and Thread Safety

For lazy instantiation see first basic singleton code example.

Thread safety (avoid that two threads check for null, succeed and both instantiate the singleton):

Double-checked locking (**outdated**) (synchronizing on class:
    
    private static volatile DclSingleton instance;
    public static DclSingleton getInstance() {
        if (instance == null) {
            synchronized (DclSingleton.class) {
                if (instance == null) {
                    instance = new DclSingleton();
                }
            }
        }
        return instance;
    }

Inner static singleton: guarantees thread safety 

>This is called the initialization-on-demand holder idiom. In Java, encapsulating classes do not automatically initialize inner classes. So the inner class only gets initialized by getInstance(). Then again, class initialization is guaranteed to be sequential in Java, so the JVM implicitly renders it thread-safe. (Florian in Q&A section of that lesson)


    class InnerStaticSingleton {
      private InnerStaticSingleton() {}
    
      private static class Impl {
        public static final InnerStaticSingleton INSTANCE = new InnerStaticSingleton();
      }
    
      public InnerStaticSingleton getInstance() {
        return Impl.INSTANCE;
      }
    }

### Enum based singleton

    enum EnumBasedSingleton {
      INSTANCE;
    
      private int value;
      // always private, no public constructor for enum
      EnumBasedSingleton() {
        value = 42;
      }
    
      public int getValue() {
        //...
    }
    
    // main:
    EnumBasedSingleton singleton = EnumBasedSingleton.INSTANCE;

- thread safe
- enums already have a private constructor (but can be implemented explicitely too)
- can't inherit from enum
- can theoretically be serialized but serialization doesn't preserve state (fields), only the instance name

### Monostate

Basically, make members static so they get shared among all instances of it:

    class ChiefExecutiveOfficer {
        private static String name; // only one field for all instances!
    
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            // for explicity Class name instead of this
            ChiefExecutiveOfficer.name = name;
        }
    }

Problem: unintuitive, doesn't make communicate to user that setting a member in one object changes it for all others too

### Multiton

- key-value store with lazy loading
- allows finite restricted set of instances
- not thread safe by default (HashMap is not threadsafe)

      // can be enum, string, int or whatever  
      enum Subsystem {
        PRIMARY,
        AUXILIARY,
        FALLBACK
      }
      
      class Printer {
        private Printer() {}
      
        private static HashMap<Subsystem, Printer> instances = new HashMap<>();
      
        public static Printer get(Subsystem ss) {
          if(instances.containsKey(ss)) {
            return instances.get(ss);
          }
          Printer instance = new Printer();
          instances.put(ss, instance);
          return instance;
        }
      }

      // main
      Printer primary = Printer.get(Subsystem.PRIMARY);
      Printer primary2 = Printer.get(Subsystem.PRIMARY);
      System.out.println(primary==primary2); // true


### Testability issues

- hard dependency - if the singleton initializes from a live DB, the tests test the current state of the DB, while we just want to test the singleton's (or other methods that use the singleton) functionality

Solution: dependency injection; providing a dependency; dependency inversion

Basically, using dummy data; see S6_07_testability_issues

### Summary

- Singletons are difficult to test
- instead of directly using a singleton, consider depending on an abstraction (e.g. interface)


## Adapter (structural)

A construct which adapts an existing interface X to conform to the required interface Y

- Determine the API you have and the API you need
- Create a component which aggregates the adaptee
- Intermediate representations can pile up: use caching and other optimizations
- [Better Explanation maybe](https://www.geeksforgeeks.org/adapter-pattern/)


## Bridge

- A mechanism that decouples and interface (hierarchy) from an implementation (hierarchy)
- Both *can* exist as hierarchies
- Strong form of encapsulation

Motivation: Bridge prevents a "Cartesian product" complexity explosion

Examples: 

- Base class ThreadScheduler
  - can be preemptive or cooperative
  - can run on windows and linux
  - -> 4 different combinations
- Shape
  - shape: circle or square
  - rendering: vector or raster
  - -> VectorCircle, VectorSquare, RasterCircle, RasterSquare

Pattern is basically an example of composition instead of inheritance. 

Pass the desired renderer to the constructor of the shape (that's why there has to be an interface or abstract base class so you can have a `Renderer` type without specifying which one) and use it when rendering

The only important thing here (see s8_03_exercise) is that the class that is passed into the other class has a common type -> interface. That's what "both can exist ans hierarchies" means