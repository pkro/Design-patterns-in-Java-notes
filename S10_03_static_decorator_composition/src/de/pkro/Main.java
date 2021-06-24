package de.pkro;

import java.util.function.Supplier;

interface Shape {
  String info();
}

public class Main {

  public static void main(String[] args) {
    ColoredShape<Square> blueSquare = new ColoredShape<>(() -> new Square(20), "blue");
    System.out.println(blueSquare.info());

    TransparentShape<ColoredShape<Circle>> myCircle =
        new TransparentShape<>(() -> new ColoredShape<>(() -> new Circle(5), "green"), 50);
    System.out.println(myCircle.info());
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

class ColoredShape<T extends Shape> implements Shape {
  private Shape shape;
  private String color;

  public ColoredShape(Supplier<? extends T> ctor, String color) {
    // we can't just do (like we could in C#) because "type erasure in java"
    // shape = new T()
    shape = ctor.get();
    this.color = color;
  }

  @Override
  public String info() {
    return shape.info() + " of color " + color;
  }
}

class TransparentShape<T extends Shape> implements Shape {
  private Shape shape;
  private double transparency;

  public TransparentShape(Supplier<? extends T> ctor, double transparency) {
    this.shape = ctor.get();
    this.transparency = transparency;
  }

  @Override
  public String info() {
    return shape.info() + " with a transparency of %" + transparency;
  }
}
