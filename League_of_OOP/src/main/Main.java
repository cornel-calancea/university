package main;

import angel.Angel;
import angel.DamageAngel;
import game.Game;
import input.GameIOLoader;
import input.GameInput;
import magician.Magician;

import java.io.IOException;

public final class Main {
  private Main() {
  }

  public static void main(final String[] args) throws IOException, CloneNotSupportedException {
    GameIOLoader gameIOLoader = new GameIOLoader(args[0], args[1]);
    GameInput gameInput = gameIOLoader.loadInput();
    Game game = new Game(gameInput);
    game.run();
    gameIOLoader.loadOutput(Magician.getReport());
    Angel angel1 = new DamageAngel();
    DamageAngel angel = new DamageAngel();
    System.out.println(angel.x);
    System.out.println(angel1.x);
  }
}
