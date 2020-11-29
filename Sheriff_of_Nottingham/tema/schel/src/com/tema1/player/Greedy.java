package com.tema1.player;
// this class contains the methods that define the behaviour of a Greedy player
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsType;
import com.tema1.main.GameState;

import java.util.ArrayList;

public final class Greedy extends Basic {
  public Greedy(final int id) {
    super(id);
    strategy = Strategy.GREEDY;
  }

  private boolean isEvenRound() {
    return ((GameState.getCurrentRound() % 2) != 0);
  }

  // creates the bag as a Basic player
  // adds one illegal card if the round number is even
  public void createBag() {
    super.createBag();
    if (isEvenRound() && (bagIsNotFull()) && (!hand.isEmpty())) {
      ArrayList<Goods> sorted = ((ArrayList<Goods>) this.hand.clone());
      sortByProfit(sorted);
      if (sorted.get(0).getType().equals(GoodsType.Illegal)) {
        bag.add(sorted.get(0));
      }
    }
  }

  @Override
  // accepts all the possible bribes and checks those that dont offer bribe
  public void inspect(final ArrayList<Player> playerList) {
    for (Player p : playerList) {
      if (!(p.equals(this))) {
        if (p.declaration.bribe == 0) {
          checkPlayer(p);
        } else {
          acceptBribe(p);
        }
      }
    }
  }
}
