package de.pkro;

import java.util.*;
import java.util.function.Consumer;

public class Main {

  public static void main(String[] args) {
    Game game = new Game();
    Creature goblin = new Creature(game, "Strong goblin", 2, 2);
    System.out.println(goblin);
    IncreasedDefenseModifier icm = new IncreasedDefenseModifier(game, goblin);
    // modifiers are already applied as the getAttack / getDefense used in toString fire the events
    // (or whatever)
    System.out.println(goblin);

    DoubleAttackModifier dam = new DoubleAttackModifier(game, goblin);
    // try with ressources so doubleattackmodifier gets cleaned up automatically
    // as it implements the autocloseable interface
    try (dam) {
      System.out.println(goblin);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

// Command Query Separation / Observer pattern
class Event<Args> {
  private int index = 0;
  private Map<Integer, Consumer<Args>> handlers = new HashMap<>();

  public int subscribe(Consumer<Args> handler) {
    int i = index;
    handlers.put(index++, handler);
    return i;
  }

  public void unsubscribe(int key) {
    handlers.remove(key);
  }

  public void fire(Args args) {
    for (Consumer<Args> handler : handlers.values()) {
      handler.accept(args);
    }
  }
}

class Query {
  public String creatureName;
  public Argument argument;
  public int result;

  public Query(String creatureName, Argument argument, int result) {
    this.creatureName = creatureName;
    this.argument = argument;
    this.result = result;
  }

  enum Argument {
    ATTACK,
    DEFENSE
  }
}

// Mediator
// central location where every modifier can subscribe itself
class Game {
  public Event<Query> queries = new Event<>();
}

class Creature {
  public String name;
  public int baseAttack, baseDefense;
  private Game game;

  public Creature(Game game, String name, int baseAttack, int baseDefense) {
    this.game = game;
    this.name = name;
    this.baseAttack = baseAttack;
    this.baseDefense = baseDefense;
  }

  public int getAttack() {
    Query q = new Query(name, Query.Argument.ATTACK, baseAttack);
    game.queries.fire(q);
    return q.result;
  }

  public int getDefense() {
    Query q = new Query(name, Query.Argument.DEFENSE, baseDefense);
    game.queries.fire(q);
    return q.result;
  }

  @Override
  public String toString() {
    return "Creature{"
        + "game="
        + game
        + ", name='"
        + name
        + '\''
        + ", Attack="
        + getAttack() // this actually fires the events
        + ", Defense="
        + getDefense()
        + '}';
  }
}

class CreatureModifier {
  protected Game game;
  protected Creature creature;

  public CreatureModifier(Game game, Creature creature) {
    this.game = game;
    this.creature = creature;
  }
}

class DoubleAttackModifier extends CreatureModifier implements AutoCloseable {
  // so we have a handle to get rid of subscription if DoubleAttackModifier gets "closed"
  private final int token;

  public DoubleAttackModifier(Game game, Creature creature) {
    super(game, creature);
    token =
        game.queries.subscribe(
            q -> {
              if (q.creatureName.equals(creature.name) && q.argument == Query.Argument.ATTACK) {
                q.result *= 2;
              }
            });
  }

  @Override
  public void close() /*throws Exception*/ {
    game.queries.unsubscribe(token);
  }
}

// without autoclosable
class IncreasedDefenseModifier extends CreatureModifier {

  public IncreasedDefenseModifier(Game game, Creature creature) {
    super(game, creature);

    game.queries.subscribe(
        q -> {
          if (q.creatureName.equals(creature.name) && q.argument == Query.Argument.DEFENSE) {
            q.result += 3;
          }
        });
  }
}
