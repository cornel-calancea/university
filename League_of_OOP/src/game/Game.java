package game;

import angel.Angel;
import hero.Hero;
import hero.HeroFactory;
import input.GameInput;
import magician.Magician;
import map.Map;
import map.Tile;

import java.util.ArrayList;
import java.util.Observable;

public final class Game extends Observable {
  private int nrOfRounds;
  private Map map;
  private ArrayList<Hero> heroes;
  /*each string represents the movements in a round*/
  private ArrayList<String> moves;
  /*list of angels by round of deployment
   * each round(i) has an array with the angels deployed in it - angels.get(i)*/
  private ArrayList<ArrayList<Angel>> angels;

  public Game(final GameInput input) {
    this.nrOfRounds = input.getNrOfRounds();
    this.moves = input.getMoves();
    HeroFactory f = HeroFactory.getInstance();
    heroes = f.getHeroesList(input.getCharacterPositionMap());
    this.map = Map.getInstance(input, heroes);
    this.angels = input.getAngels();
    this.addObserver(Magician.getInstance());
  }

  public void run() throws CloneNotSupportedException {
    for (int i = 0; i < nrOfRounds; i++) {
      System.out.println("ROUND " + i);
      /*notify the Great Mage of the changes*/
      setChanged();
      notifyObservers("~~ Round " + (i + 1) + " ~~");
      clearChanged();
      applyOvertimeDamages();
      applyStrategies();
      map.updatePositions(moves.get(i));
      Battle.fightBattles(map.getGameSpace(), map.getHeroes());
      removeRedundantOvertimes();
      deployAngels(i);
      updateLevels();
      setChanged();
      notifyObservers("");
      clearChanged();
      System.out.println("RESULTS AFTER ROUND " + i);
      System.out.println(getResults());
    }
    setChanged();
    notifyObservers("~~ Results ~~" + System.lineSeparator() + getResults());
    clearChanged();
  }

  private void applyStrategies() {
    System.out.println("Strategies : ");
    for (Hero hero : this.heroes) {
      if (hero.isDead() || hero.cannotMove()) {
        hero.setStrategy(null);
        continue;
      } else {
        hero.setStrategy(null);
        hero.applyStrategy();
      }
      System.out.println(hero.getHeroName() + " : " + hero.getStrategy());
    }
  }

  private void deployAngels(final int roundNr) {
    for (Angel angel : this.angels.get(roundNr)) {
      int row = angel.getPosition().getRow();
      int column = angel.getPosition().getColumn();
      Tile areaOfDeployment = map.getGameSpace().get(row).get(column);
      angel.deploy(areaOfDeployment);
    }
  }

  private void updateLevels() {
    for (Hero hero : this.heroes) {
      hero.updateLevel();
    }
  }

  private void removeRedundantOvertimes() {
    for (Hero h : heroes) {
      h.updateOvertimes();
    }
  }

  private void applyOvertimeDamages() throws CloneNotSupportedException {
    for (Hero hero : heroes) {
      if (hero.isDead()) {
        continue;
      }
      if (hero.getOvertimeAbility() != null) {
        hero.getOvertimeAbility().overtimeDamage(hero);
      }
    }
  }

  public final String getResults() {
    StringBuilder results = new StringBuilder();
    for (Hero hero : heroes) {
      results.append(hero.getHeroType() + " ");
      if (hero.isDead()) {
        results.append("dead").append(System.lineSeparator());
      } else {
        results
                .append(hero.getLevel())
                .append(" ")
                .append(hero.getXp())
                .append(" ")
                .append(hero.getHp())
                .append(" ")
                .append(hero.getPosition().getRow())
                .append(" ")
                .append(hero.getPosition().getColumn())
                .append(System.lineSeparator());
      }
    }
    results.append(System.lineSeparator());
    return results.toString();
  }
}
