package de.pkro;

public class Main {

  public static void main(String[] args) {
    // write your code here
  }
}

class Bird {
  public int age;

  public String fly() {
    return age < 10 ? "flying" : "too old";
  }
}

class Lizard {
  public int age;

  public String crawl() {
    return (age > 1) ? "crawling" : "too young";
  }
}

class Dragon {
  private int age;
  Bird bird = new Bird();
  Lizard lizard = new Lizard();

  public void setAge(int age) {
    this.age = age;
    bird.age = age;
    lizard.age = age;
  }

  public String fly() {
    return bird.fly();
  }

  public String crawl() {
    return lizard.crawl();
  }
}