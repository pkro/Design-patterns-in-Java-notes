package de.pkro;

interface Renderer {
  void renderCircle(float radius);
}

public class Main {

  public static void main(String[] args) {
    RasterRenderer rasterRenderer = new RasterRenderer();
    VectorRenderer vectorRenderer = new VectorRenderer();
    Circle circle = new Circle(vectorRenderer, 5);
    circle.draw();
    circle.resize(2);
    circle.draw();

    circle.renderer = rasterRenderer;
    circle.draw();
  }
}

class VectorRenderer implements Renderer {
  @Override
  public void renderCircle(float radius) {
    System.out.println("Drawing circle of radius " + radius);
  }
}

class RasterRenderer implements Renderer {
  @Override
  public void renderCircle(float radius) {
    System.out.println("Drawing pixels for circle of radius " + radius);
  }
}

abstract class Shape {
  protected Renderer renderer;

  public Shape(Renderer renderer) {
    this.renderer = renderer;
  }

  public abstract void draw();

  public abstract void resize(float factor);
}

class Circle extends Shape {
  public float radius;

  public Circle(Renderer renderer, float radius) {
    super(renderer);
    this.radius = radius;
  }

  @Override
  public void draw() {
    renderer.renderCircle(radius);
  }

  @Override
  public void resize(float factor) {
    radius *= factor;
  }
}
