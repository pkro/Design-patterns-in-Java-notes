package de.pkro;

import javax.print.attribute.standard.Media;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    Mediator mediator = new Mediator();
    Participant p1 = new Participant(mediator);
    Participant p2 = new Participant(mediator);
    p1.say(3);
    p2.say(2);

    System.out.println("p1: " + p1.getValue());
    System.out.println("p2: " + p2.getValue());
  }
}

class Participant {
  public int value = 0;
  private Mediator mediator;

  public Participant(Mediator mediator) {
    this.mediator = mediator;
    mediator.add(this);
  }

  public void say(int n) {
    mediator.broadcast(this, n);
  }

  public void updateValue(int value) {
    this.value += value;
  }

  public int getValue() {
    return value;
  }
}

class Mediator {
  private List<Participant> participants = new ArrayList<>();
  public void add(Participant p) {
    participants.add(p);
  }
  public void broadcast(Participant originator, int value) {
    for(Participant p: participants) {
      if(p != originator) {
        p.updateValue(value);
      }
    }
  }
}
