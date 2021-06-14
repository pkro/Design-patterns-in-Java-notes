package de.pkro;


import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    System.out.println(new Triangle(new RasterRenderer()).toString());
  }
}

interface Renderer {
  public String whatToRenderAs();
}

class VectorRenderer implements Renderer {
  @Override
  public String whatToRenderAs() {
    return "lines";
  }
}

class RasterRenderer implements Renderer {
  @Override
  public String whatToRenderAs() {
    return "pixels";
  }
}

abstract class Shape {
  protected Renderer renderer;

  public abstract String getName();

  public Shape(Renderer renderer) {
    this.renderer = renderer;
  }

  @Override
  public String toString() {
    return "Drawing " + getName() + " as " + renderer.whatToRenderAs();
  }
}

class Triangle extends Shape {
  @Override
  public String getName() {
    return "Triangle";
  }

  public Triangle(Renderer renderer) {
    super(renderer);
  }
}

class Square extends Shape {
  @Override
  public String getName() {
    return "Square";
  }

  public Square(Renderer renderer) {
    super(renderer);
  }
}


// imagine VectorTriangle and RasterTriangle are here too
