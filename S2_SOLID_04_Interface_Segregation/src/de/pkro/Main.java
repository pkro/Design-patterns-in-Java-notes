package de.pkro;

import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    // write your code here
  }
}

interface Machine {
  void print(Document document);

  void fax(Document document);

  void scan(Document document);
}

interface Printer {
  void print(Document d);
}

interface Fax {
  void fax(Document d);
}

interface Scan {
  void scan(Document d);
}

class Document {}

class MultiFunctionPrinter implements Machine {
  @Override
  public void print(Document document) {}

  @Override
  public void fax(Document document) {}

  @Override
  public void scan(Document document) {}
}
// Problem: needs to override methods he can't do (fax, scan)
// When leaving empty, it gives the user of the class the impression
// that it can fax and scan
// Solution: split Machine into 3 separate interfaces
class oldFashionedPrinterBadInterface implements Machine {
  @Override
  public void print(Document document) {
    System.out.println("Printing");
  }

  @Override
  public void fax(Document document) {
    //
  }

  @Override
  public void scan(Document document) {
    //
  }
}

// using multiple, smaller interfaces
class oldFashionedPrinter implements Printer {
  public void print(Document document) {
    System.out.println("Printing");
  }
}

class PhotoCopier implements Printer, Scan {
  @Override
  public void print(Document d) {

  }

  @Override
  public void scan(Document d) {

  }
}

interface MultiFunctionDevice extends Printer, Scan {}

class MultiFunctionMachine implements MultiFunctionDevice {
  @Override
  public void print(Document d) {

  }

  @Override
  public void scan(Document d) {

  }
}