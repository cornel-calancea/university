package com.tema1.player;
// this class contains the methods that defines the behaviour of the Bribed strategy player
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsType;

import java.util.ArrayList;

import static com.tema1.common.Constants.*;

public final class Bribe extends Basic {
  public Bribe(final int id) {
    super(id);
    strategy = Strategy.BRIBED;
  }

  public void createBag() {
    /*if doesnt have enough money to give bribe or doesn't have
    legal goods in hand creates the bag just like a basic player*/
    if (cantBribe() || !hasIllegalGoods(hand)) {
      super.createBag();
    } else {
      // bring to the bag all the most profitable goods
      int potentialPenalty = 0;
      ArrayList<Goods> sorted = (ArrayList<Goods>) this.hand.clone();
      sortByProfit(sorted);
      while (bagIsNotFull() && !sorted.isEmpty()) {
        Goods nextCard = sorted.get(0);
        if ((nextCard.getPenalty() + potentialPenalty) >= this.money) {
          sorted.remove(0);
        } else {
          this.bag.add(nextCard);
          sorted.remove(nextCard);
          potentialPenalty += nextCard.getPenalty();
        }
      }
      declare();
    }
  }

  private boolean cantBribe() {
    return (this.money <= SMALL_BRIBE);
  }

  public void declare() {
    int illegalGoodsCount = countIllegalGoods();
    switch (illegalGoodsCount) {
      case 0:
        super.declare();
        break;
      case 1:
      case 2:
        declaration.bribe = SMALL_BRIBE;
        this.money -= SMALL_BRIBE;
        declaration.declaredGoods = APPLE;
        break;
      default:
        declaration.bribe = BIG_BRIBE;
        this.money -= BIG_BRIBE;
        declaration.declaredGoods = APPLE;
    }
  }

  // returns the number of illegal goods in bag
  private int countIllegalGoods() {
    int count = 0;
    for (Goods entry : this.bag) {
      if (entry.getType().equals(GoodsType.Illegal)) {
        count++;
      }
    }
    return count;
  }

  public void inspect(final ArrayList<Player> playerList) {
    if (lowMoney()) {
      getAllPossibleBribes(playerList);

    } else {
      if (playerList.size() < 4) {
        super.inspect(playerList);
      } else {
        bribedInspect(playerList);
      }
    }
  }

  // Inspection the bribed way : inspects left and right neighbours and accepts bribes from the
  // others
  private void bribedInspect(final ArrayList<Player> playerList) {
    Player rightNeighbour = getRightNeighbour(playerList);
    Player leftNeighbour = getLeftNeighbour(playerList);
    for (Player p : playerList) {
      if (p.equals(rightNeighbour) || p.equals(leftNeighbour)) {
        checkPlayer(p);
      } else {
        if (!p.equals(this)) {
          acceptBribe(p);
        }
      }
    }
  }

  private void getAllPossibleBribes(final ArrayList<Player> playerList) {
    Player rightNeighbour = getRightNeighbour(playerList);
    Player leftNeighbour = getLeftNeighbour(playerList);
    for (Player p : playerList) {
      if (p.equals(rightNeighbour) || p.equals(leftNeighbour)) {
        refuseBribe(p);
      } else {
        if (!p.equals(this)) {
          acceptBribe(p);
        }
      }
    }
  }

  private Player getRightNeighbour(final ArrayList<Player> playerList) {
    if (this.getId() == playerList.size() - 1) {
      return playerList.get(0);
    } else {
      return playerList.get(this.getId() + 1);
    }
  }

  private Player getLeftNeighbour(final ArrayList<Player> playerList) {
    if (this.getId() == 0) {
      return playerList.get(playerList.size() - 1);
    } else {
      return playerList.get(this.getId() - 1);
    }
  }
}
