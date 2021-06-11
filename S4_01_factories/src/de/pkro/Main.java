package de.pkro;

public class Main {
  public static void main(String[] args) {
    Point p = Point.Factory.fromPolar(3, 5);
    System.out.println(p);

    Point p2 = Point.Factory.fromCartesian(3, 5);
    System.out.println(p2);
  }
}

class Point {
  private double x, y;

  // can (only) remain private if factory is inner class
  private Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "Point{" + "x=" + x + ", y=" + y + '}';
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

