package de.pkro;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// not a good solution, this is more of a mediator

public class Main {

  public static void main(String[] args) {
    Game game = new Game();
    Rat rat = new Rat(game);
    Rat rat2 = new Rat(game);

    System.out.println(rat.attack); // 2
    System.out.println(rat2.attack); // 2
  }
}

// observable or the other way round?
class Game {
  private List<Rat> rats = new ArrayList<>();

  public void addRat(Rat rat) {
    rats.add(rat);
    broadcastAttack();
  }

  public void removeRat(Rat rat) {
    rats.remove(rat);
    broadcastAttack();

  }
  public int getAttack() {
    return rats.size();
  }

  private void broadcastAttack() {
    for (Rat r : rats) {
      r.updateAttack();
    }
  }
}

// observer
class Rat implements Closeable {
  public int attack = 1;
  private Game game;

  public Rat(Game game) {
    this.game = game;
    game.addRat(this);
  }

  public void updateAttack() {
    this.attack = game.getAttack();
  }

  @Override
  public void close() throws IOException {
    game.removeRat(this);
  }
}
