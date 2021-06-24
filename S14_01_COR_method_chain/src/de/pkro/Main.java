package de.pkro;

public class Main {

  public static void main(String[] args) {
    Creature goblin = new Creature("Goblin", 2, 2);
    System.out.println(goblin);
    // starting point for adding handlers, CreatureModifier does nothing on its own
    CreatureModifier root = new CreatureModifier(goblin);
    System.out.println("doubling attack");
    root.add(new DoubleAttackModifier(goblin));
    System.out.println("increasing defense");
    root.add(new IncreaseDefenseModifier(goblin));
    root.handle(); // traverse all modifiers
    System.out.println(goblin);

    Creature cursedGoblin = new Creature("cursed goblin", 2, 2);
    CreatureModifier root2 = new CreatureModifier(cursedGoblin);
    System.out.println(cursedGoblin);
    root2.add(new NoBonusesModifier(cursedGoblin)); // add before all others
    root2.add(new DoubleAttackModifier(cursedGoblin)); // doesn't do anything as NoBonusModifier blocks chain
    root2.handle();
    System.out.println(cursedGoblin);
  }
}

class Creature {
  public String name;
  public int attack, defense;

  public Creature(String name, int attack, int defense) {
    this.name = name;
    this.attack = attack;
    this.defense = defense;
  }

  @Override
  public String toString() {
    return "Creature{"
        + "name='"
        + name
        + '\''
        + ", attack="
        + attack
        + ", defense="
        + defense
        + '}';
  }
}

class CreatureModifier {
    protected Creature creature;
    protected CreatureModifier next;

  public CreatureModifier(Creature creature) {
    this.creature = creature;
  }

  public void add(CreatureModifier cm) {
    if(next == null) {
      next = cm;
      return;
    }
    next.add(cm);
  }

  public void handle() {
    if(next != null) {
      next.handle();
    }
  }
}

class DoubleAttackModifier extends CreatureModifier {
  public DoubleAttackModifier(Creature creature) {
    super(creature);
  }

  @Override
  public void handle() {
    System.out.println("Doubling " + creature.name + "'s attack");
    creature.attack *= 2;
    super.handle();
  }
}

class IncreaseDefenseModifier extends CreatureModifier {
  public IncreaseDefenseModifier(Creature creature) {
    super(creature);
  }

  @Override
  public void handle() {
    System.out.println("Adding 3 to " + creature.name + "'s defense");
    creature.defense += 3;
    super.handle(); // -> pass to next modifier (next.handle())
  }
}

class NoBonusesModifier extends CreatureModifier {
  public NoBonusesModifier(Creature creature) {
    super(creature);
  }

  @Override
  public void handle() {
    System.out.println("You're cursed, no bonuses!");
    //nothing, no super.handle, cancels out all other bonuses
  }
}