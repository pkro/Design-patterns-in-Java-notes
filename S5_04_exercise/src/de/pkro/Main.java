package de.pkro;

public class Main {

  public static void main(String[] args) {
    Line l = new Line(new Point(4, 5), new Point(9, 15));
    Line l2 = l.deepCopy();
    l2.end = new Point(99,100);

    System.out.println(l);
    System.out.println(l2);
  }
}

class Point
{
  public int x, y;

  public Point(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "Point{" + "x=" + x + ", y=" + y + '}';
  }
}

class Line
{
  public Point start, end;

  public Line(Point start, Point end)
  {
    this.start = start;
    this.end = end;
  }

  public Line deepCopy()
  {
    return new Line(new Point(start.x, start.y), new Point(end.x, end.y));
  }

  @Override
  public String toString() {
    return "Line{" + "start=" + start + ", end=" + end + '}';
  }
}