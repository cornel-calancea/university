package com.tema1.player;
// this class defines the methods that define the behaviour of the Basic player
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.tema1.common.Constants.APPLE;

public class Basic extends Player {
  public Basic(final int id) {
    super(id);
    this.strategy = Strategy.BASIC;
  }

  public void createBag() {
    ArrayList<Goods> sorted = ((ArrayList<Goods>) this.hand.clone());
    if (hasLegalGoods(hand)) {
      // get the most frequent goods
      sortByFrequency(sorted);
      for (int i = 0; i < Collections.frequency(sorted, sorted.get(0)); i++) {
        if (bagIsNotFull()) {
          Goods addedCard = sorted.get(0);
          this.bag.add(addedCard);
          this.hand.remove(addedCard);
        } else {
          break;
        }
      }
    } else {
      // get only the most profitable illegal card
      sortByProfit(sorted);
      Goods addedCard = sorted.get(0);
      this.bag.add(addedCard);
      this.hand.remove(addedCard);
      // an illegal good will never return to the deck
    }
    declare();
  }

  private boolean hasLegalGoods(final ArrayList<Goods> array) {
    return array.stream().anyMatch(entry -> entry.getType().equals(GoodsType.Legal));
  }

  boolean hasIllegalGoods(final ArrayList<Goods> array) {
    return array.stream().anyMatch(entry -> entry.getType().equals(GoodsType.Illegal));
  }

  private void sortByFrequency(final ArrayList<Goods> array) {
    // sorts goods by type-frequency-profit-id
    array.sort(
        Comparator.comparing(Goods::getType)
            .thenComparing(
                Comparator.comparing((Goods i) -> Collections.frequency(array, i)).reversed())
            .thenComparing(Comparator.comparingInt(Goods::getProfit).reversed())
            .thenComparing(Comparator.comparingInt(Goods::getId).reversed()));
  }

  final void sortByProfit(final ArrayList<Goods> array) {
    array.sort(
        (Comparator.comparing(Goods::getProfit).reversed())
            .thenComparing(Comparator.comparing(Goods::getId).reversed()));
  }

  public void declare() {
    declaration.bribe = 0;
    if (hasIllegalGoods(bag)) {
      declaration.declaredGoods = APPLE;
    } else {
      declaration.declaredGoods = bag.get(0).getId();
    }
  }

  public void inspect(final ArrayList<Player> playerList) {
    if (lowMoney()) {
      for (Player p : playerList) {
        refuseBribe(p);
      }
    }
    for (Player p : playerList) {
      if (!(p.equals(this))) {
        checkPlayer(p);
      }
    }
  }
}
