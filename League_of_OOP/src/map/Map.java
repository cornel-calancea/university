package map;

import hero.Hero;
import hero.Position;
import input.GameInput;

import java.util.ArrayList;
import java.util.Collections;

public final class Map {
  /*The map of the game - as requested, Singleton <3 */
  private static Map instance = null;
  private int length;
  private int width;
  private ArrayList<ArrayList<Tile>> gameSpace = new ArrayList<>();
  private ArrayList<Hero> heroes = new ArrayList<>();

  public ArrayList<Hero> getHeroes() {
    return heroes;
  }

  public ArrayList<ArrayList<Tile>> getGameSpace() {
    return gameSpace;
  }

  private Map(final GameInput input, final ArrayList<Hero> heroList) {
    this.length = input.getLength();
    this.width = input.getWidth();
    for (int i = 0; i < this.length; i++) {
      gameSpace.add(new ArrayList<>());
      for (int j = 0; j < this.width; j++) {
        gameSpace.get(i).add(new Tile(input.getTerrain().get(i).get(j)));
      }
      heroes = heroList;
    }
    for (Hero hero : heroList) {
      addToMap(hero);
    }
  }

  private void addToMap(final Hero hero) {
    Position p = hero.getPosition();
    gameSpace.get(p.getRow()).get(p.getColumn()).getHeroes().add(hero);
    Collections.sort(gameSpace.get(p.getRow()).get(p.getColumn()).getHeroes());
    hero.setTerrain(gameSpace.get(p.getRow()).get(p.getColumn()).getTerrainType());
  }

  public static Map getInstance(final GameInput input, final ArrayList<Hero> heroList) {
    if (instance == null) {
      instance = new Map(input, heroList);
    }
    return instance;
  }

  public void updatePositions(final String movements) {
    for (int i = 0; i < movements.length(); i++) {
      Hero player = heroes.get(i);
      if (player.isDead()) {
        continue;
      }
      if (player.cannotMove()) {
        continue;
      }
      switch (movements.charAt(i)) {
        case 'U':
          moveUp(player);
          break;
        case 'D':
          moveDown(player);
          break;
        case 'L':
          moveLeft(player);
          break;
        case 'R':
          moveRight(player);
          break;
        case '_':
        default:
      }
    }
  }

  /*Removes player from map if has valid position*/
  public void removeFromMap(final Hero h) {
    Position p = h.getPosition();
    if (p.getRow() >= 0 && p.getColumn() >= 0) {
      gameSpace.get(p.getRow()).get(p.getColumn()).getHeroes().remove(h);
    }
  }

  private void moveUp(final Hero h) {
    removeFromMap(h);
    h.getPosition().setRow(h.getPosition().getRow() - 1);
    if ((h.getPosition().getColumn() >= 0) && (h.getPosition().getRow() >= 0)) {
      addToMap(h);
    }
  }

  private void moveDown(final Hero h) {
    removeFromMap(h);
    h.getPosition().setRow(h.getPosition().getRow() + 1);
    if ((h.getPosition().getColumn() >= 0) && (h.getPosition().getRow() >= 0)) {
      addToMap(h);
    }
  }

  private void moveLeft(final Hero h) {
    removeFromMap(h);
    h.getPosition().setColumn(h.getPosition().getColumn() - 1);
    if ((h.getPosition().getColumn() >= 0) && (h.getPosition().getRow() >= 0)) {
      addToMap(h);
    }
  }

  private void moveRight(final Hero h) {
    removeFromMap(h);
    h.getPosition().setColumn(h.getPosition().getColumn() + 1);
    if ((h.getPosition().getColumn() >= 0) && (h.getPosition().getRow() >= 0)) {
      addToMap(h);
    }
  }
}
