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

## Builder pattern