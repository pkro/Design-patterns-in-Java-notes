package de.pkro;

import java.util.*;

public class Main {

  public static void main(String[] args) {
    CodeBuilder cb = new CodeBuilder("Person").addField("name", "String").addField("age", "int");
    System.out.println(cb);
    /*Code code = new Code("Test");
    code.addField("name", "String");
    code.addField("age", "int");
    System.out.println(code);*/
  }
}

class Code {
  public final int indentation = 2;

  private String className;
  private List<List<String>> fields;

  public Code() {
    fields = new ArrayList<>();
  }

  public Code(String className) {
    this();
    this.className = className;

  }

  public void addField(String name, String type) {
    fields.add(Arrays.asList(name, type));
  }

  private String spaces(int numSpaces) {
    return String.join("", Collections.nCopies(numSpaces, " "));
  }

  private String spaces() {
    return spaces(indentation);
  }

  public void setClassName(String className) {
    this.className = className;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    String indent = spaces();
    sb.append("public class ");
    sb.append(className);
    sb.append("\n{");
    for(List<String> field: this.fields) {
      sb.append("\n");
      sb.append(spaces());
      sb.append("public ");

      sb.append(field.get(1));
      sb.append(" ");
      sb.append(field.get(0));
      sb.append(";");
    }
    sb.append("\n");
    sb.append("}");
    return sb.toString();
  }
}

class CodeBuilder<SELF extends CodeBuilder<SELF>> {
  Code code = new Code();

  public CodeBuilder(String className) {
    code.setClassName(className);
  }

  public SELF addField(String name, String type) {
    code.addField(name, type);
    return (SELF) this;
  }

  @Override
  public String toString() {
    return code.toString();
  }
}
