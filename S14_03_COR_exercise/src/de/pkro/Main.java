package de.pkro;

import java.util.ArrayList;
import java.util.List;

enum Statistic {
  ATTACK,
  DEFENSE
}

public class Main {

  public static void main(String[] args) {
    Game game = new Game();
    Goblin goblin = new Goblin(game);
    game.creatures.add(goblin);
    System.out.println(goblin.getAttack());
    System.out.println(goblin.getDefense());

    Goblin goblin2 = new Goblin(game);
    game.creatures.add(goblin2);
    System.out.println(goblin.getAttack());
    System.out.println(goblin.getDefense());

    GoblinKing gk = new GoblinKing(game);
    game.creatures.add(gk);
    System.out.println(goblin.getAttack());
    System.out.println(goblin.getDefense());

  }
}

abstract class Creature {
  public abstract int getAttack();

  public abstract int getDefense();
}

class Goblin extends Creature {
  protected int baseAttack = 1;
  protected int baseDefense = 1;
  private Game game;

  public Goblin(Game game) {
    this.game = game;
  }

  @Override
  public int getAttack() {
    int attack = baseAttack;
    for(Creature creature: game.creatures) {
      if(creature != this) {
        switch (creature.getClass().getSimpleName()) {
          case "GoblinKing":
            attack += 1;
            break;
        }
      }
    }
    return attack;
  }

  @Override
  public int getDefense() {
    int defense = baseDefense;
    for(Creature creature: game.creatures) {
      if(creature != this) {
        switch (creature.getClass().getSimpleName()) {
          case "Goblin":
          case "GoblinKing":
            defense +=1;
            break;
        }
      }
    }
    return defense;
  }
}

class GoblinKing extends Goblin {
  public GoblinKing(Game game) {
    super(game);
    baseAttack = 3;
    baseDefense = 3;
  }
}

class Game {
  public List<Creature> creatures = new ArrayList<>();
}
