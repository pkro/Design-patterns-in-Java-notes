package de.pkro;

interface Shape {
  String info();
}

public class Main {

  public static void main(String[] args) {
    Circle circle = new Circle(10);
    System.out.println(circle.info());

    ColoredShape blueSquare = new ColoredShape(new Square(20), "blue");
    System.out.println(blueSquare.info());

    TransparentShape greenTransparentCircle =
        new TransparentShape(new ColoredShape(new Circle(10), "green"), 0.5);

    System.out.println(greenTransparentCircle.info());

    // Note that greenTransparentCircle is not a Circle and thus can't be resized; for this we would need to delegate again
  }
}

class Circle implements Shape {
  public float radius;

  public Circle() {}

  public Circle(float radius) {
    this.radius = radius;
  }

  void resize(float factor) {
    radius *= factor;
  }

  @Override
  public String info() {
    return "A circle of " + radius;
  }
}

class Square implements Shape {
  private float side;

  public Square() {}

  public Square(float side) {
    this.side = side;
  }

  @Override
  public String info() {
    return "A square of " + side;
  }
}

// Motivation:
// We don't want to modify shape or its subclasses, but want to add color and transparency

class ColoredShape implements Shape {
  private Shape shape;
  private String color;

  public ColoredShape(Shape shape, String color) {
    this.shape = shape;
    this.color = color;
  }

  @Override
  public String info() {
    return shape.info() + " of color " + color;
  }
}

class TransparentShape implements Shape {
  private Shape shape;
  private double transparency;

  public TransparentShape(Shape shape, double transparency) {
    this.shape = shape;
    this.transparency = transparency;
  }

  @Override
  public String info() {
    return shape.info() + " with a transparency of %" + transparency;
  }
}
