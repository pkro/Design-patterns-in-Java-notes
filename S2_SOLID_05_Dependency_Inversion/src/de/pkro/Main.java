package de.pkro;

import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

enum Relationship {
  PARENT,
  CHILD,
  SIBLING
}

interface RelationshipBrowser {
  List<Person> findAllChildrenOf(String name);
}

public class Main {

  public static void main(String[] args) {
    Person parent = new Person("John");
    Person child1 = new Person("Chris");
    Person child2 = new Person("Matt");

    Relationships relationships = new Relationships();
    relationships.addParentAndChild(parent, child1);
    relationships.addParentAndChild(parent, child2);

    new Research(relationships);
  }
}

class Person {
  public String name;

  public Person(String name) {
    this.name = name;
  }
}

// low level data storage module, no business logic
class Relationships implements RelationshipBrowser {
  private List<Triplet<Person, Relationship, Person>> relations = new ArrayList<>();

  public void addParentAndChild(Person parent, Person child) {
    relations.add(new Triplet<>(parent, Relationship.PARENT, child));
    relations.add(new Triplet<>(child, Relationship.CHILD, parent));
  }

  public List<Triplet<Person, Relationship, Person>> getRelations() {
    return relations; // problem: exposes internal state
  }

  @Override
  public List<Person> findAllChildrenOf(String name) {
    return relations.stream()
        .filter(
            x -> Objects.equals(x.getValue0().name, name) && x.getValue1() == Relationship.PARENT)
        .map(Triplet::getValue2)
        .collect(Collectors.toList());
  }
}

class Research { // High level module, business logic
  // depends on low level class, violates dependency inversion principle
  /*public Research(Relationships relationships) {
    List<Triplet<Person, Relationship, Person>> relations = relationships.getRelations();
  }*/

  public Research(RelationshipBrowser browser) {
    List<Person> children = browser.findAllChildrenOf("John");
    for(Person child: children) {
      System.out.println("John has a child named " + child.name);
    }
  }
}
