package de.pkro;

public class Main {

  public static void main(String[] args) {
    // 1 + (2 + 3)
    Expression e =
        new AdditionExpression(
            new DoubleExpression(1),
            new AdditionExpression(new DoubleExpression(2), new DoubleExpression(3)));

    // Problem: we want to be able to print all expressions
    StringBuilder sb = new StringBuilder();
    ExpressionPrinter.print(e, sb);
    System.out.println(sb);
  }
}

class ExpressionPrinter {
  public static void print(Expression e, StringBuilder sb) {
    if (e.getClass() == DoubleExpression.class) {
      sb.append(((DoubleExpression) e).value);
    } else if (e.getClass() == AdditionExpression.class) {
      AdditionExpression ae = (AdditionExpression) e;
      sb.append("(");
      print(ae.left, sb);
      sb.append("+");
      print(ae.right, sb);
      sb.append(")");
    }
  }
}

// expression like 1+2
abstract class Expression {

}

class DoubleExpression extends Expression {
  public double value;

  public DoubleExpression(double value) {
    this.value = value;
  }
}

class AdditionExpression extends Expression {
  public Expression left, right;

  public AdditionExpression(Expression left, Expression right) {
    this.left = left;
    this.right = right;
  }
}
