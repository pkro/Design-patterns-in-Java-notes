# Design patterns in Java

Notes on udemy course of the same name by Dmitri Nesteruk

## Unrelated intellij notes

Just typing `new Person("John")`, then `str-alt-v` to introduce local variable to turn it into `Person john = new Person("John")`

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

