package de.pkro;

import java.util.Random;

public class Main {

  public static void main(String[] args) {
    new Chess().run();
  }
}

abstract class Game {
  protected final int numberOfPlayers;
  protected int currentPlayer;

  public Game(int numberOfPlayer) {
    this.numberOfPlayers = numberOfPlayer;
  }

  public void run() {
    start();
    while (!haveWinner()) {
      takeTurn();
    }
    System.out.println("player " + getWinningPlayer() + " wins");
  }

  protected abstract void takeTurn();

  protected abstract boolean haveWinner();

  protected abstract void start();

  protected abstract int getWinningPlayer();
}

class Chess extends Game {
  private int maxTurns = 10;
  private int turn = 1;

  public Chess() {
    super(2);
    currentPlayer = 1;
  }

  @Override
  protected void takeTurn() {
    System.out.println("Turn " + (turn++) + " taken by player " + currentPlayer);
    currentPlayer = currentPlayer == 1 ? 2 : 1;
  }

  @Override
  protected boolean haveWinner() {
    return turn == maxTurns;
  }

  @Override
  protected void start() {
    System.out.println("Game starting");
  }

  @Override
  protected int getWinningPlayer() {
    return new Random().nextInt(2) + 1;
  }
}
