package input;

import angel.Angel;
import hero.TypePositionPair;

import java.util.ArrayList;

public final class GameInput {

  private int length;
  private int width;
  private ArrayList<ArrayList<Character>> terrain;
  private int nrOfHeroes;
  private ArrayList<TypePositionPair> characterPositionMap;
  private int nrOfRounds;
  private ArrayList<String> moves;
  private ArrayList<ArrayList<Angel>> angelsByRounds;

  public GameInput(
          final int length,
          final int width,
          final ArrayList<ArrayList<Character>> terrain,
          final int nrOfHeroes,
          final ArrayList<TypePositionPair> characterPositionMap,
          final int nrOfRounds,
          final ArrayList<String> moves,
          final ArrayList<ArrayList<Angel>> angels) {
    this.length = length;
    this.width = width;
    this.terrain = terrain;
    this.nrOfHeroes = nrOfHeroes;
    this.characterPositionMap = characterPositionMap;
    this.nrOfRounds = nrOfRounds;
    this.moves = moves;
    this.angelsByRounds = angels;
  }

  public int getLength() {
    return length;
  }

  public int getWidth() {
    return width;
  }

  public ArrayList<ArrayList<Character>> getTerrain() {
    return terrain;
  }

  public ArrayList<TypePositionPair> getCharacterPositionMap() {
    return characterPositionMap;
  }

  public int getNrOfRounds() {
    return nrOfRounds;
  }

  public ArrayList<String> getMoves() {
    return moves;
  }

  public ArrayList<ArrayList<Angel>> getAngels() {
    return angelsByRounds;
  }
}
