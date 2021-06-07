package de.pkro;

public class Main {

  public static void main(String[] args) {
    // this workd
    useIt(new Rectangle(2, 3));

    // does NOT work, as r.setHeight in useIt method sets also
    // the width for a square in the setHeight method of Square
    // The problem is the subclassing of Rectangle by Square
    Rectangle sq = new Square();
    sq.setWidth(5);
    useIt(sq);
  }

  static void useIt(Rectangle r) {
    int width = r.getWidth();
    r.setHeight(10);
    System.out.println("Expected area of " + (width*10) + ", got " + r.getArea());
  }
}

class Rectangle {
  protected int width, height;

  public Rectangle() {}

  public Rectangle(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  @Override
  public String toString() {
    return "Rectangle{" + "width=" + width + ", height=" + height + '}';
  }

  public int getArea() {
    return width*height;
  }

  public boolean isSquare() {
    return width == height;
  }
}

class RectangleFactory {
  public static Rectangle newRectangle(int width, int height) {
    return new Rectangle(width, height);
  }
  public static Rectangle newSquare(int width) {
    return new Rectangle(width, width);
  }
}

// violates Liskov substitution principle
class Square extends Rectangle {
  private int length;
  public Square() {}
  public Square(int length) {
    super(length, length);
    this.length = length;
  }

  @Override
  public void setWidth(int width) {
    super.setWidth(width);
    super.setHeight(width);
  }

  @Override
  public void setHeight(int height) {
    super.setWidth(height);
    super.setHeight(height);
  }
}
