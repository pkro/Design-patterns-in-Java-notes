# Design patterns in Java

Notes on udemy course of the same name by Dmitri Nesteruk

## Unrelated intellij notes

- Just typing `new Person("John")`, then `str-alt-v` to introduce local variable to turn it into `Person john = new Person("John")`
- same with alt-enter, "introduce local variable"
- or typing an expression with .var at the end, e.g. `"fdf".var` creates whole `String varname = "fdf"`
- writing a constructor, putting the cursor in the parameters and alt+enter -> bind constructor parameters to fields creates all fields and assigns them in the constructor
- alt-enter on switch keyword -> generate missing cases
## General java notes

- [Map.merge](`https://www.nurkiewicz.com/2019/03/mapmerge-one-method-to-rule-them-all.html`) is VERY useful to update maps, e.g. add to an existing field, create a new field if it doesn't exist or even delete it if the value given is null:


    Map<String, Integer> myWordCounterMap = new Map<>();
    words.forEach(word ->
            // or just Integer::sum instead of (prev, one) -> prev + one
            myWordCounterMap.merge(word, 1, (prev, one) -> prev + one)
    );

- It's also thread safe with `ConcurrentHashMap`


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

The Liskov substitution principle means that child classes should maintain the behavior of their parent classes.

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
- Can exist in a separate class (Factors) - separation of concerns
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

The only important thing here (see s8_03_exercise) is that the class that is passed into the other class has a common type -> interface. That's what "both can exist as hierarchies" means.

## Composite

Treating individual and aggregate objects uniformly

A mechanism for treating individual (scalar) objects and compositions of objects in a uniform manner
Motivation:

- objects use other objects fields / methods through inheritance and composition
- Composition lets us make compound objects
  - e.g. mathematical expression composed of simple expressions
  - shape group made of different shapes
- Composite design pattern is used to treat both single (scalar) and composite objects uniformly, meaning Foo and List<Foo> having common APIs 

In essence, make a collection in an object that can contain objects of its own type, and make the methods in a way that they can apply to a singular instance as well as to their children. See the example in S9_01_composite.

That means that you can also make a single element iterable by masking as a collection e.g. by implementing `Iterable` and overriding Iterator by returning an iterator of a set of `this`:

    @Override
    public Iterator<Neuron> iterator() {
      return Collections.singleton(this).iterator();
    }

### Summary

- Some composed and singular objects need similar / identical behaviors
- Composite design pattern lets us treat both uniformly
- Java supports container iteration using `Iterable<T>` interface
- A single object can masquerade as a collection by returning a single element collections containing only this (see example code above)

## Decorator

Adding behaviour without altering the class itself

Facilitates the addition of behaviours to *individual objects* without inheriting from them

Motivation:

- want to augment an object with additonal functionality
- don't want to rewrite existing code (Open-Closed Principle)
- want to keep new functionality separate (Separation Of Concerns)
- Need to be able to interact with existing structures
- Two options:
  - Inherit from required object if possible (class is not final)
  - Build a decorator, which simply references the decorated object and adds new functionality
  
Example: `String` is final; if we want to add a method (but still keep the other methods of String accessible), we would need to delegate all String method (in IntelliJ these can be generated, alt-v -> delegate methods).

For String with it's rich API these would be dozens of methods.

    class MagicString {
        private String string;
    
        public MagicString(String string) {
            this.string = string;
        }

        // this is the only method we want to add
        public long getNumberOfVowels() {
            return string.chars().mapToObj(c->(char) c).filter(c->"aeiou".contains(c.toString())).count();
        }
        
        // have to do this otherwise we will just get a reference
        @Override
        public String toString() {
            return string;
        }
        
        // all the delegated String methods
        public String toLowerCase(Locale locale) {
          return string.toLowerCase(locale);
        }
    
        public String toLowerCase() {
            return string.toLowerCase();
        }
        // dozens more  
     
    }

### Dynamic decorator composition:

Basically the same as the example above combined with "program to an interface"; see S10_02 code.

> The main advantage is runtime composition of required functionality without rigid dependencies. (Dmitri in lesson Q&A)

### Static decorator composition:

- determines types at compile time using generics <T extends...>

### Adapter decorator

### Summary

- A decorator keeps the reference to the decorated objects
- may or may not forward (delegate) calls; IDE can generate delegated members
- static variation: `X<Y>Foo>>(/* unpleasant constructor args */)`; limited in java for [type erasure](https://www.baeldung.com/java-type-erasure) and that you can't inherit from type parameters (like `foo extends T`) in java

## Facade

Exposing several components through a single interface

Provide a simple, easy to understand interface over a large and sophisticated body of code

Motivation

- balancing complexity and presentation / usability (easy API)
- example: a house has many subsystems (electrical, sanitation, internal structure) that are not exposed to the tenant

Example console:
    
    // main:
    // low level usage of subsystems without facade:
    Buffer buffer = new Buffer(30, 20);
    Viewport viewport = new Viewport(buffer, 30, 20, 0, 0);
    Console console = new Console(30, 20);
    console.addViewport(viewport);
    console.render();

    // with facade method, single buffer, single viewport
    Console console1 = Console.newConsole(30, 20);
    console1.render();

    // class Console
    // the actual facade with common defaults
    public static Console newConsole(int width, int height) {
      Buffer buffer = new Buffer(width, height);
      Viewport viewport = new Viewport(buffer, width, height, 0, 0);
      Console console = new Console(width, height);
      return console;
    }
  
### Summary

- build a facade to provide a simplified API over a set of classes
- optionally expose internals through facade for "powerusers"
- may allow users to escalate to more complex API if needed

# Flyweight

Space optimization

A space optimization technique that lets us use less memory by externally storing the data associated with similar objects 

Motivation:

- Avoid redundancy when storing data
- e.g. MMORPG
  - plenty of users with same first / last name
  - instead, store list of names and point to them
  - basically like db normalization 
- e.g. bold or italic text in console
  - don't store format information for each character
  - operate on ranges (e.g. line number, start / end etc)
  - nice implementation of storing a range and its formatting in S12_02

### Summary

- store common data externally
- specify an index or reference to the external data store
- define metadata like ranges on homogenous collections and store data for these metadata objects instead of the individual objects in the collection itself
- don't forget [String.intern()](https://www.baeldung.com/string/intern)

## Proxy

Interface for accessing a particular ressource

A class that functions as an interface to a particular resource. That resource may be remote, expensive to construct or require logging or some other added funcitonality

*Isn't that a bit like facade or decorator? -> see explanation of difference proxy/decorator below*

Motivation:

- calling foo.bar()
- this assumes that foo is in the same process as bar()
- what if, later on, it is desired to put all Foo related operations in a separate process?

-> Proxy: gives the same interface, entirely different behaviour (communication proxy)

Other types: logging, virtual, guarding...

### Protection proxy

Performs extra checks before calling the parent (proxy'd) class method. The CarProxy is still of supertype Car, so the code using it doesn't need to change assigned types and optionally use dependency injection.

    class CarProxy extends Car {
      public CarProxy(Driver driver) {
        super(driver);
      }
    
      @Override
      public void drive() {
        if (driver.age > 16) {
          super.drive();__
        } else {
          System.out.println("Driver is too young to drive");
        }
      }
    }

### Property proxy

Unusual, doesn't really fall into proxy category

Replacing a field (member variable) with something that forces you to use getters / setters; not a proxy in the sense that in java, the `=` operator can't be overloaded, so the interface doesn not stay the same as it would with a "real" proxy. This is possible with the in-built proxy object in Javascript for example.

    class Property<T> {
        private T value;
        public Property(T value) {
            this.value = value;
        }
    
        public T getValue() {
            return value;
        }
    
        public void setValue(T value) {
            // do logging here
            this.value = value;
        }
    
        @Override
        public boolean equals(Object o) {
           //...
        }
    
        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
    
    class Creature {
        private Property<Integer> agility = new Property<>(10);
    
        public void setAgility(int value) { // we keep the normal int as parameter
            // logging or whatever here
            agility.setValue(value);
        }
    
        public int getAgility() {
            return agility.getValue();
        }
    }

### Dynamic proxy for logging

Constructed at runtime. Important pattern as it's used in many frameworks.

Java provided `java.lang.reflect.InvocationHandler` interface for this which allows to intercept different methods using `invoke`

See S13_03_proxy_dynamic_proxy for example, complicated

### Proxy vs decorator:

- Proxy provides an identical interface; decorator provides an enhanced interface (additional members etc)
- decorator typically aggregates or has reference to what it is decorating; proxy doesn't have to
- Proxy might not even be working with a materialized (existing) object (?)

### Summary

- a proxy has the same interface as the underlying object
- to create a proxy, replicate the interface of an object
- add relevant functionality to the redefined member functions
- different proxies (communication, logging, caching etc.) have completely different behaviours


## Chain of responsibility

Sequence of handlers processing an event one after another

A chain of components who all get a chance to process a command or query, optionally having default processing implementation and an ability to terminate the processing chain

Example: button click handlers in html (button, parent element, window...)

### Method chain

Recursively calling the next handler after applying its own, clear example in S14_01_COR_method_chain.

### Sidenote: Command Query Separation (CQS)

- Command = asking for an action or change (e.g. set attack value to 2)
- Query = asking for information (please give current attack value) without any sideeffects such as changing a value
- CQS = having separate means of sending commands and queries

### Broker Chain

COR + Observer + mediator + memento

No direct references between methods (modifiers in the example) necessary.  
Complicated but understandable, see S14_02_COR_broker_chain

**EXERCISE WORKS BUT DOESN'T REALLY USE COR**

### Summary

- COR can be implemented as a chain of references or a centralized construct
- enlist objects in the chain, possibly controlling their order
- Object removal from chain (e.g. in AutoCloseable close())


## Command pattern

An object which represents an instruction to perform a particular action. Contains all the information necessary for the action to be taken.

- Ordinary Java statements are perishable:
  - can't undo a field assignment
  - can't directly serialize a sequence of actions (calls)
- want an object that represents an operation 
  - X should change its field Y to value Z
  - X should do w()
- Uses: GUI commands, multi-level undo/redo, macro recording and more

### Summary

- encapsulate all details of an operation, e.g. arguments passed to the actual method that performs the action and a variable indicating the success of the operation, in a separate object
- define instruction for applying the command (eihter in the command itself or elsewhere)
- optionally define instructions for undoing the command
- can create composite commands (aka macros)

## Interpreter

A component that processes structured text data. It does so by turining it into seperate lexical tokens (*lexing*) and then interpreting sequences of said tokens (*parsing*)

- Textual input needs to be processed, e.g. turned into OOP structures
- Examples:
  - programming language compilers, interpreters and IDEs
  - HTML, XML etc
  - numeric expressions (4+4/5)
  - Regex

- see S16_01_interpreter for lexer / parser implementation

### ANTLR

- Usually parsers aren't written by hand; one (most known) tool for this is [ANTLR](http://www.antlr.org)

### Summary

- barring simple cases, an interpreter acts in 2 stages:
  - lexing turns text into a set of tokens:  
  `3*(4+5)` -> `Lit[3] Star Lparen Lit[4] Plus Lit[5] Rparen`
  - parsing parses tokens into meaningful constructs, building an abstract syntax tree:  
  

    MultiplicationExpression[
      Integer[3],
      AdditionExpression[
        Integer[4], Integer[5]
      ]
    ]

  - parsed data can then be traversed / interpreted

  
## Iterator

A **helper** object that facilitates the traversal of a data structure.

Motivation

- Iteration (traversal) is a core functionality of data structures
- iterator is a class that facilitates traversal
  - keeps reference to current element
  - knows how to move to a different element
- Java has Iterator<T> and Iterable<T>
  - Iterator<T> specifies the iterator API
  - a class needs to be Iterable to support for (Foo foo: bar) loops
  
### Array backed properties

Terminology: Property = field + getter + setter

Basically, replacing single property fields with ONE array to make :

Instead of:

    class SimpleCreature {
      private int strength, agility, intelligence;
    
      public int max() {
        return Math.max(strength, Math.max(agility, intelligence));
      }
    
      public int sum() {
        return strength + agility + intelligence;
      }
      // ...
    
      public int getStrength() {
        return strength;
      }
    
      public void setStrength(int strength) {
        this.strength = strength;
      }
      // ...


WITH array backed properties:

    class Creature implements Iterable<Integer> {
      private int [] stats = new int[3];
  
      public int sum() {
          return IntStream.of(stats).sum();
      }
  
      public int max() {
          return IntStream.of(stats).max().getAsInt();
      }
      // ...

      public int getStrength() {
          return stats[0];
      }
  
      public void setStrength(int strength) {
          this.stats[0] = strength;
      }
      //...

Advantage: aggregate functions like sum, max don't need to be modified when adding properties.

Undesired [magic numbers](https://en.wikipedia.org/wiki/Magic_number_(programming)), but this can be overcome by defining constants for easier access:

    private final int STRENGTH = 0;
    private final int AGILITY = 1;
    private final int INTELLIGENCE = 2;
    
    private int [] stats = new int[3];
    
    public int getStrength() {
        return stats[STRENGTH];
    }
    // ...

Having properties as arrays makes it possible to (easily) implement the Iterable interface:

    @Override
    public Iterator<Integer> iterator() {
      return IntStream.of(stats).iterator();
    }
  
    @Override
    public void forEach(Consumer<? super Integer> action) {
      for (int x : stats) {
        action.accept(x);
      }
    }
  
    @Override
    public Spliterator<Integer> spliterator() {
      return IntStream.of(stats).spliterator();
    }

In the context of the example making a "Creature" iterable is a bit unclear, unless a creature is regarded as just a collection of stats (and nothing else). Rightfuly pointed out by Gyorgy in the QA section

### Summary

- an iterator specifies how you can traverse an object
- iterator can't be recursive (no coroutines) in Java
- Iterator implements Iterator<T>, iterable object implements Iterable<T>

## Mediator

Facilitates communication between components

A component that facilitates communication between other components without them necessarily being aware of each other of having direct (reference) access to each other.

Motivation

- components may go in and out of a system any time
  - chat room participants (the chatroom is the mediator between participants)
  - players in MMORPG
- It makes no sense for them to have direct references to one another as they might go dead
- Solution: have all refer to a central component that facilitates communication: Mediator

### Reactive Extensions Event Broker

For example using reactive extensions (io.reactivex) see code example. No idea.

### Summary

- create the mediator and have each object in the system refer to it (e.g. a list)
- mediator engages in bidirecional communication with its connected components
- mediator has functions that the components can call
- componentes have functions the mediator can call
- event processing (e.g. Rx) libraries make communication easier to implement

## Memento

Keep a memento of an objects state to return to that state later

A token / handle representing the system state; lets us roll back to the state when the token was generated; may or may not directly expose state information. **Typically immutable**.

Motivation

- An object or system goes through changes (e.g. bank account)
- different ways to navigate changes
  - record every change (command pattern) and teach a command to undo itself; low memory footprint
  - save snapshots of the system; higher memory footprint if object has many properties
  
### Basic memento

    class BankAccount {
      private int balance;
      // ... constructor etc
      public Memento deposit(int amount) {
        balance += amount;
        return new Memento(balance);
      }
    
      public void restore(Memento m) {
        this.balance = m.getBalance();
      }

    class Memento { // or BankAccountToken
      private int balance; // immutable, no setter
    
      public Memento(int balance) {
        this.balance = balance;
      }
    
      public int getBalance() {
       //..
    
    // Main
    BankAccount bankAccount = new BankAccount(); // initializes to 0
    Memento state1 = bankAccount.deposit(300);
    Memento state2 = bankAccount.deposit(250);
    System.out.println(bankAccount); // 550
    bankAccount.restore(state1);
    System.out.println(bankAccount); // 300

### Memento for Interoperability between different languages

- calling C++ functions from java is easy
- Problem: can't instantiate C++ classes in Java or pass objects
- Solution: Expose top level functions that create objects on the C++ side that return a (primitive) memento like an int as a representation that can be passed to a native C++ function that manipulates the object

### Summary

- Mementos are used to roll back states arbitrarily
- A memento is a token / handle class with no functionality of its own
- memento is not required to expose states to which it reverts the system; should be immutable in most cases
- can be used, like command pattern, to undo / redo

## Null object

A behavioral design pattern with no behaviors; not present in GOF book

A no-op Object that conforms to the required interface satisfying a dependency requirement of some other object.

Useful in testing

Motivation

- When component A uses component B, it typically assumes that B is non-null
  - you inject B, not some Option\<B\> type (?)
  - you don't check for null on every call on the B object
- There is no option of telling A *not* to use an instance of B (its use is hard-coded)
- Thus, we build a no-op, non-functioning inheritor of B (or some interface that B implements) and pass it to A, so that e.g. calling methods on the empty B object doesn't cause any exceptions (like they would with `null`)

### Null Object

Just make a class implementing the required interface that has empty method bodies.

### Dynamic null object

- If there are multiple nullable objects, creating "empty" classes for each is tedious
- Solution: construct an object at runtime that conforms to the right interface using the proxy pattern
- Might not work for classes without constructor, e.g. classes that can only be instantiated by a factory
- more computational intensive at runtime than; still useful for unittests and where performance doesn't matter 

    
    // method somewhere, e.g. Main class
    @SuppressWarnings("unchecked")
    public static <T> T noOp(Class<T> itf) {
      // build fake object that conforms to given interface
      return (T)
          Proxy.newProxyInstance(
              itf.getClassLoader(),
              new Class<?>[] {itf},
              (proxy, method, args) -> {
                if (method.getReturnType().equals(Void.TYPE)) {
                  return null;
                } else {
                  return method.getReturnType().getConstructor().newInstance();
                }
              });
    }
  
  Usage:

    // Dynamic null object using dynamic proxy (noOp method below)
    Log noOp = noOp(Log.class);
    BankAccount accountUsingNoOpProxy = new BankAccount(noOp);
    accountUsingNoOpProxy.deposit(500);

### Summary

- implement required interface
- rewrite methods with empty bodies (if non-void, return default value or minimally implement)
- supply instance of null object in place of actual object
- cross fingers

## Observer

An *observer* is an object that wishes to be informed about events happening in the system. The entity generating the events is an *observable*

Motivation:

- We need to be informed when:
  - an objects field changes
  - object does something
  - some external event occurs
- Typical pattern involves addXxxListener()
- Java now has functional objects that can be stored in an array and fired when necessary
  - `Supplier<T>`, [Consumer\<T\>](https://www.geeksforgeeks.org/java-8-consumer-interface-in-java-with-examples/), `Function<T>`

In order to not having to inherit / implement observable / observer, we can encapsulate the idea of an event in an event class, which is a container of subscriptions, then go through each subscription and fire the event (?)

PropertyChangedNotifications is more complicated with dependent properties like getCanVote() in the S21_02 example.

For the exercise, see the instructor solution, my own solution was pretty much a mediator type solution.

### Summary

- Observer is an intrusive approach: an observible must provide an event to subscribe to
- Sprecial care must be taken to prevent issues in multithreading
- Rx uses Observer<T> / Obervable<T> (?)

## State

A pattern in which the objects beavior is determined by its state. An object transitions from one state to another (something needs to trigger a transition)

A formalized construct wich manages state and transitions is called a *state machine*

Motivation:

- consider ordinary phoe
- what you do with it depends of state of phone / line
  - ringing or you want to make a call: pick  up
  - must be off the hook to make a call
  - when line is busy, you put it down
- changes in state can be explicit or in response to event (observer pattern)

### Classic GOF implementation

- not used in practice anymore
- every state is a class (OnState, OffState etc)
- see S22_01_state_classic

### Handmade State machine

See S22_02 for classic state machine implementation

### Summary

- Given sufficient complexity, it pays to formally define possible states and events / triggers
- Can define:
  - state entry / exit behaviours
  - action when a particular event causes a transition
  - guard conditions enabling / disabling a transition
  - default action when no transitions are found for an event

## Strategy (also known as *policy* in C++)

System behaviour partially specified at runtime.

Enables the exact behavior of a system to be selected either at runtime (dynamic) or compile time (static).

Motivation

- Many algorithms can be decomposed into higher and lower level parts (general and specific parts?)
- Making tea can be decomposed into
  - making hot beverage: boil water, pour into cup (higher level = general part)
  - put teabag into water (tea specific, lower level = specific part)
- The high-level algorithm can be reused for making coffee or hot chocolate
  - supported by beverage-specific *strategies*

### Summary

- Defione an algorithm at high level
- Define interface each strateg should follow
- provide for dynamic or static composition of strategy in the overall algorithm

## Template method

High level blueprint for an algorithm to be completed by inheritors

Allows us to define the skeleton of the algorithm, with concrete implementations defined in subclasses.

*In Java, this is just the concept of the abstract base class?*

Motivation

- Algorithms can be decomposed into common parts + specifics
- Strategy pattern does this through composition
  - High level algo uses an interface
  - concrete implementations implement the interface
- Template method does the same thing through inheritance
  - overall algorithm makes use of abstract member
  - Inheritors override the abstract members
  - Parent template method invoked
  
Example:

    abstract class Game {
      protected final int numberOfPlayers;
      protected int currentPlayer;
    
      public Game(int numberOfPlayer) {
        this.numberOfPlayers = numberOfPlayer;
      }
    
      public void run() {
        // instead of strategy.start(), strategy.haveWinner() etc we use 
        // abstract methods to be implemented by the concrete class 
        start();
        while (!haveWinner()) {
          takeTurn();
          System.out.println("player " + getWinningPlayer() + " wins");
        }
      }
    
      protected abstract void takeTurn();
      protected abstract boolean haveWinner();
      protected abstract void start();
      protected abstract int getWinningPlayer();
    }
    
    class Chess extends Game {
      private int maxTurns = 10;
      private int turn = 1;
    
      public Chess() {
        super(2);
      }
    
      @Override
      protected void takeTurn() {
        System.out.println("Turn " + (turn++) + " taken by player " + currentPlayer);
      }
    
      @Override
      protected boolean haveWinner() {
        return turn == maxTurns;
      }
      // ...
    }

## Visitor

Allows adding extra behaviors to entire class hierarchies

A pattern where a component (visitor) is allowed to traverse the entire inheritance hierarchy. Implemented by propagating a single `visit()` method throughout the entire hierarchy.

Motivation

- Need to define a new operation on an entire class hierarchy without modifying all (sub-) classes, e.g. making a document model printable (outputable) to different formats like HTML / Markdown 
- need access to the non-common aspects of classes in the hierarchy (?)
- create an external component to handle rendering (but avoid type checks)

### Intrusive visitor

We just implement the method we need in the abstract base class and override them in the subclasses, breaking both open-closed (classes should be closed to modification) and separation of concerns (a data class should not be responsible for print output) principle.

    // expression like 1+2
    abstract class Expression {
      public abstract void print(StringBuilder sb);
    }
    
    class DoubleExpression extends Expression {
      private double value;
    
      public DoubleExpression(double value) {
        this.value = value;
      }
    
      @Override
      public void print(StringBuilder sb) {
        sb.append("" + value);
      }
    }
    
    class AdditionExpression extends Expression {
      private Expression left, right;
    
      public AdditionExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
      }
    
      @Override
      public void print(StringBuilder sb) {
        sb.append("(");
        left.print(sb);
        sb.append("+");
        right.print(sb);
        sb.append(")");
      }
    }

### Reflective visitor

Put desired functionality in a separate class.

    class ExpressionPrinter {
      public static void print(Expression e, StringBuilder sb) {
        if (e.getClass() == DoubleExpression.class) {
          sb.append(((DoubleExpression) e).value);
        } else if (e.getClass() == AdditionExpression.class) {
          AdditionExpression ae = (AdditionExpression) e;
          sb.append("(");
          print(ae.left, sb);
          sb.append("+");
          print(ae.right, sb);
          sb.append(")");
        }
      }
    }
    
    // expression like 1+2
    abstract class Expression {}
    
    class DoubleExpression extends Expression {
      public double value;
    
      public DoubleExpression(double value) {
        this.value = value;
      }
      // nothing special needs to be implemented here for printing 
    }
    
    class AdditionExpression extends Expression { 
    // ...

Issues: 

- code is slow due to reflection / type checks / casts
- no check if every case is implemented (it prints fine if a class type isn't in the if/else tree, but elements are skipped)

Solution (with small intrusion):

### Classic ("real world use") visitor (double dispatch)

- accept method (and its overloads) has to be implemented in the class implementing te visitor
- just the accept() method has to be implemented in all subclasses to be able to add multiple functionalities
- see S25_03_classic_visitor_double_dispatch

Issues: cyclic dependencies (visit / accept)

### Acyclic visitor

- basically interface segregation principle
- I don't understand, too early in the morning

### summary

- Proagate an `accept(Visitor v)` method throughout the entire hierarchy
- create a visitro with `visit(Foo=), visit(Bar), ...` for each element in the hierarchy
- Each `accept()` simply calls `visitor.visit(this)`
- Acyclyic visitor allows greater flexibility at cost of performance
