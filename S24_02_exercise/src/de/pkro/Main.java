package de.pkro;

public class Main {

  public static void main(String[] args) {
    Creature c1 = new Creature(1, 2);
    Creature c2 = new Creature(1, 2);
    // CardGame game = new PermanentCardDamageGame(new Creature[]{c1, c2});
    CardGame game = new TemporaryCardDamageGame(new Creature[] {c1, c2});

    System.out.println(game.combat(0, 1)); // -1
    System.out.println(game.combat(0, 1)); // -1
  }
}

class Creature {
  public int attack, health;

  public Creature(int attack, int health) {
    this.attack = attack;
    this.health = health;
  }
}

abstract class CardGame {
  public Creature[] creatures;

  public CardGame(Creature[] creatures) {
    this.creatures = creatures;
  }

  // returns -1 if no clear winner (both alive or both dead)
  public int combat(int creature1, int creature2) {
    Creature first = creatures[creature1];
    Creature second = creatures[creature2];
    hit(first, second);
    hit(second, first);
    if ((first.health <= 0 && second.health <= 0) || (first.health > 0 && second.health > 0)) {
      return -1;
    }

    return first.health <= 0 ? creature2 : creature1;
  }

  // attacker hits other creature
  protected abstract void hit(Creature attacker, Creature other);
}

class TemporaryCardDamageGame extends CardGame {

  public TemporaryCardDamageGame(Creature[] creatures) {
    super(creatures);
  }

  @Override
  protected void hit(Creature attacker, Creature other) {
    // I'm stupid
    int oldHealth = other.health;
    other.health -= attacker.attack;
    if (other.health > 0) other.health = oldHealth;
  }
}

class PermanentCardDamageGame extends CardGame {
  public PermanentCardDamageGame(Creature[] creatures) {
    super(creatures);
  }

  @Override
  protected void hit(Creature attacker, Creature other) {
    other.health -= attacker.attack;
  }
}
