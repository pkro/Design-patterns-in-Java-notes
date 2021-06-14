package de.pkro;

public class Main {

  public static void main(String[] args) {
    // write your code here
  }
}

interface Rectangle {
  int getWidth();
  int getHeight();
  default int getArea() {
    return getWidth() * getHeight();
  }
}

class Square {
  public int side;

  public Square(int side) {
    this.side = side;
  }
}

class SquareToRectangleAdapter implements Rectangle {
  private int w;
  private int h;

  public SquareToRectangleAdapter(Square square) {
    this.w = square.side;
    this.h = square.side;
  }

  @Override
  public int getWidth() {
    return this.w;
  }

  @Override
  public int getHeight() {
    return this.h;
  }
}
