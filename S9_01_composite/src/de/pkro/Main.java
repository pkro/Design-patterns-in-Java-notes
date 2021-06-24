package de.pkro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    GraphicObject drawing = new GraphicObject();
    drawing.setName("drawing");
    drawing.children.add(new Square("red"));
    drawing.children.add(new Circle("yellow"));

    GraphicObject group = new GraphicObject();
    group.children.add(new Circle("blue"));
    group.children.add(new Square("blue"));

    drawing.children.add(group);

    System.out.println(drawing);
  }
}

// not an abstract class as we might want to instantiate it as a group of different objects
class GraphicObject {
  public String color;
  // the class can be a singular item (scalar) or a group of them
  public List<GraphicObject> children = new ArrayList<>();
  protected String name = "Group";

  public GraphicObject() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    print(sb, 0);
    return sb.toString();
  }

  // print is the same method for singular objects and for groups of objects (the children)
  private void print(StringBuilder sb, int depth) {
    // just does this for depth 0,1,2...
    // [color] name
    // * [color] name
    // ** [color] name
    sb.append(String.join("", Collections.nCopies(depth, "*")))
        .append(depth > 0 ? " " : "")
        .append((color == null || color.isEmpty()) ? "" : color + " ")
        .append(getName())
        .append(System.lineSeparator());

    // if the children are empty simply nothing will happen here
    for (GraphicObject child : children) {
      child.print(sb, depth + 1);
    }
  }
}

class Circle extends GraphicObject {
  public Circle(String color) {
    name = "Circle";
    this.color = color;
  }
}

class Square extends GraphicObject {
  public Square(String color) {
    name = "Square";
    this.color = color;
  }
}