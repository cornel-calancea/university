package com.tema1.main;
// this class stores the state of the game and has the methods that define the course of the game
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.LegalGoods;
import com.tema1.player.Player;

import java.util.Comparator;

import static com.tema1.common.Constants.*;

public final class Game {
  private GameState state;
  private Round round;

  public Game(final GameInput input) {
    this.state = new GameState(input);
    this.round = new Round(state);
  }

  // sorts the list of players according by score(descending), then by their id
  private void sortByScore() {
    this.state
        .getPlayers()
        .sort(Comparator.comparing(Player::getMoney).reversed().thenComparing(Player::getId));
  }

  // simulates the game according to given parameters
  void playGame() {
    for (int roundCount = 1; roundCount <= state.getNrRounds(); roundCount++) {
      // plays a round and updates the round count
      round.playRound();
      GameState.setCurrentRound(roundCount);
    }
    sellGoods();
    applyKingQueenBonus();
    sortByScore();
    printResults();
  }

  // used to print the final results
  private void printResults() {
    for (Player p : state.getPlayers()) {
      System.out.println(p.getId() + " " + p.getStrategy() + " " + p.getMoney());
    }
  }

  private void applyKingQueenBonus() {
    GoodsFactory factory = GoodsFactory.getInstance();
    final Goods[] legalGoods = {
      factory.getGoodsById(APPLE),
      factory.getGoodsById(CHEESE),
      factory.getGoodsById(BREAD),
      factory.getGoodsById(CHICKEN),
      factory.getGoodsById(TOMATO),
      factory.getGoodsById(CORN),
      factory.getGoodsById(POTATO),
      factory.getGoodsById(WINE),
      factory.getGoodsById(SALT),
      factory.getGoodsById(SUGAR)
    };
    for (Goods product : legalGoods) {
      state
          .getPlayers()
          .sort(
              Comparator.comparing((Player p) -> p.getStand().get(product))
                  .reversed()
                  .thenComparingInt(Player::getId));
      if (state.getPlayers().get(0).getStand().get(product) != 0) {
        state.getPlayers().get(0).money += ((LegalGoods) product).getKingBonus();
        if (state.getPlayers().get(1).getStand().get(product) != 0) {
          state.getPlayers().get(1).money += ((LegalGoods) product).getQueenBonus();
        }
      }
    }
  }

  private void sellGoods() {
    for (Player p : state.getPlayers()) {
      p.sell();
    }
  }
}
