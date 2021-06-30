package de.pkro;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class Main {

  public static void main(String[] args) {
    Creature creature = new Creature();
    creature.setAgility(12);
    creature.setIntelligence(5);
    creature.setStrength(15);

    System.out.println("creature has a max stat of " + creature.max());
    System.out.println("creature has a stat sum of " + creature.sum());
    System.out.println("creature has an avg stat of " + creature.average());

    // iterate
    for(Integer stat: creature) {
      System.out.println(stat);
    }
  }
}

// WITH array backed properties
class Creature implements Iterable<Integer> {
  private final int STRENGTH = 0;
  private final int AGILITY = 1;
  private final int INTELLIGENCE = 2;

  private int[] stats = new int[3];

  public int sum() {
    return IntStream.of(stats).sum();
  }

  public int max() {
    return IntStream.of(stats).max().getAsInt();
  }

  public double average() {
    return IntStream.of(stats).average().getAsDouble();
  }

  public int getStrength() {
    return stats[STRENGTH];
  }

  public void setStrength(int strength) {
    this.stats[STRENGTH] = strength;
  }

  public int getAgility() {
    return stats[AGILITY];
  }

  public void setAgility(int agility) {
    this.stats[AGILITY] = agility;
  }

  public int getIntelligence() {
    return stats[INTELLIGENCE];
  }

  public void setIntelligence(int intelligence) {
    this.stats[INTELLIGENCE] = intelligence;
  }

  @Override
  public Iterator<Integer> iterator() {
    return IntStream.of(stats).iterator();
  }

  @Override
  public void forEach(Consumer<? super Integer> action) {
    for (int x : stats) {
      action.accept(x);
    }
  }

  @Override
  public Spliterator<Integer> spliterator() {
    return IntStream.of(stats).spliterator();
  }
}

// without array backed properties
class SimpleCreature {
  private int strength, agility, intelligence;

  public int max() {
    return Math.max(strength, Math.max(agility, intelligence));
  }

  public int sum() {
    return strength + agility + intelligence;
  }

  public double average() {
    return sum() / 3;
  }

  public int getStrength() {
    return strength;
  }

  public void setStrength(int strength) {
    this.strength = strength;
  }

  public int getAgility() {
    return agility;
  }

  public void setAgility(int agility) {
    this.agility = agility;
  }

  public int getIntelligence() {
    return intelligence;
  }

  public void setIntelligence(int intelligence) {
    this.intelligence = intelligence;
  }
}
