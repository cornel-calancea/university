package game;

import hero.Hero;
import map.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public final class Battle {
  private Battle() {}

  static void fightBattles(final ArrayList<ArrayList<Tile>> gameSpace, final ArrayList<Hero> heroes)
      throws CloneNotSupportedException {
    /* Gets the battles and sorts them according to the player's ID.
    There may be dead players on the tile so we need to get the alive players to fight */
    ArrayList<ArrayList<Hero>> battles = getBattles(gameSpace);
    Collections.sort(battles, Comparator.comparing(a -> a.get(0)));
    for (ArrayList<Hero> battle : battles) {
      Hero player1 = null, player2 = null;
      for (int i = 0; i < battle.size(); i++) {
        if (battle.get(i).isDead()) {
          continue;
        } else {
          player1 = battle.get(i);
          for (int j = i; j < battle.size(); j++) {
            if (battle.get(j).isDead()) {
              continue;
            } else {
              player2 = battle.get(j);
            }
          }
          break;
        }
      }
      player1.abilitiesSetTerrain();
      player2.abilitiesSetTerrain();
      player1.becomeEnemy(player2);
      player2.becomeEnemy(player1);
      player1.attack(player2);
      player2.attack(player1);
      player1.addXP(player2);
      player2.addXP(player1);
    }
  }

  static ArrayList<ArrayList<Hero>> getBattles(final ArrayList<ArrayList<Tile>> gameSpace) {
    /*analyzes the map and returns the battles(where there is more than 1 alive player)*/
    ArrayList<ArrayList<Hero>> battles = new ArrayList<>();
    for (ArrayList<Tile> row : gameSpace) {
      for (Tile tile : row) {
        if (getNrOfAlivePlayers(tile.getHeroes()) > 1) {
          battles.add(tile.getHeroes());
        }
      }
    }
    return battles;
  }

  static int getNrOfAlivePlayers(final ArrayList<Hero> list) {
    int i = 0;
    for (Hero hero : list) {
      if (!hero.isDead()) {
        i++;
      }
    }
    return i;
  }
}
