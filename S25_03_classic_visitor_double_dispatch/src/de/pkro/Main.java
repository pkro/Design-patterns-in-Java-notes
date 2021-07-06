package de.pkro;

interface ExpressionVisitor {
  // now subclasses can't be forgotton in expressionvisitor
  void visit(DoubleExpression e);

  void visit(AdditionExpression e);
}

public class Main {

  public static void main(String[] args) {
    // 1 + (2 + 3)
    //Expression e =
    // We have to use the specific type now
    AdditionExpression e =
        new AdditionExpression(
            new DoubleExpression(1),
            new AdditionExpression(new DoubleExpression(2), new DoubleExpression(3)));

    // Problem: we want to be able to print all expressions
    ExpressionPrinter ep = new ExpressionPrinter();
    ep.visit(e);
    System.out.println(ep);

    ExpressionCalculator ec = new ExpressionCalculator();
    ec.visit(e);
    System.out.println(ec);
  }
}

// separation of concerns
class ExpressionPrinter implements ExpressionVisitor {
  private StringBuilder sb = new StringBuilder();

  @Override
  public void visit(DoubleExpression e) {
    sb.append(((DoubleExpression) e).value);
  }

  @Override
  public void visit(AdditionExpression e) {
    sb.append("(");
    e.left.accept(this); // double jump into correct overload
    sb.append("+");
    e.right.accept(this);
    sb.append(")");
  }

  @Override
  public String toString() {
    return sb.toString();
  }
}

// yet another functionality that needs only the visit/accept method
class ExpressionCalculator implements ExpressionVisitor {
  private double result;

  @Override
  public String toString() {
    return ""+result;
  }

  @Override
  public void visit(DoubleExpression e) {
    result = e.value;
  }

  @Override
  public void visit(AdditionExpression e) {
    e.left.accept(this);
    double a = result;
    e.right.accept(this);
    double b = result;
    result = a + b;
  }
}

abstract class Expression {
  public abstract void accept(ExpressionVisitor visitor);
}

class DoubleExpression extends Expression {
  public double value;

  public DoubleExpression(double value) {
    this.value = value;
  }

  @Override
  public void accept(ExpressionVisitor visitor) {
    visitor.visit(this);
  }
}

class AdditionExpression extends Expression {
  public Expression left, right;

  public AdditionExpression(Expression left, Expression right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public void accept(ExpressionVisitor visitor) {
    visitor.visit(this);
  }
}
