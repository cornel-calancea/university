package com.tema1.player;
// contains fields and methods common for all Players and definitions of abstract methods whose
// implementations depend on strategy
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;
import com.tema1.goods.IllegalGoods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static com.tema1.common.Constants.*;

public abstract class Player {
  // stores the player ID
  private int id;
  // stores the current amount of money
  public int money;
  // stores the player strategy
  Strategy strategy;
  // stores the cards given to the player at the beginning of the subround
  // is also used to keep track of the initial order of the cards
  ArrayList<Goods> hand;
  // stores the Goods chosen by the player to take in Nottingham
  ArrayList<Goods> bag;
  // stores a declaration - declared Goods, and the amount of money offered as bribe
  Declaration declaration;
  // stores the Goods that have been brought to Nottingham and their respective quantities
  private HashMap<Goods, Integer> stand;

  public Player(final int id) {
    this.id = id;
    this.money = DEFAULT_SCORE;
    this.hand = new ArrayList<>();
    this.bag = new ArrayList<>();
    this.declaration = new Declaration();
    this.stand = initStand();
  }

  // initializes an empty stand
  private HashMap<Goods, Integer> initStand() {
    HashMap<Goods, Integer> newStand = new HashMap<>();
    GoodsFactory factory = GoodsFactory.getInstance();
    for (Integer product : LEGAL_GOODS) {
      newStand.put(factory.getGoodsById(product), 0);
    }
    return newStand;
  }

  public final ArrayList<Goods> getHand() {
    return hand;
  }

  public final Strategy getStrategy() {
    return strategy;
  }

  public final int getMoney() {
    return money;
  }

  private void setMoney(final int money) {
    this.money = money;
  }

  public final HashMap<Goods, Integer> getStand() {
    return stand;
  }

  public final int getId() {
    return id;
  }

  // when sheriff meets an undeclared card
  private void confiscate(final ArrayList<Goods> undeclaredGoods) {
    for (Goods card : undeclaredGoods) {
      bag.remove(card);
      if (card.getType().equals(GoodsType.Illegal)) {
        burn(card);
      }
    }
  }

  // when card won't be added to the deck at the end
  private void burn(final Goods card) {
    hand.remove(card);
  }

  // picks a new hand of cards
  public final void pickCards(final ArrayList<Goods> deck) {
    if (!(hand.isEmpty())) {
      // if hand
      System.exit(1);
    } else {
      // adds cards until we fill the hand or there are no more cards in the deck
      while ((hand.size() < HAND_SIZE) && !deck.isEmpty()) {
        hand.add(deck.get(0));
        deck.remove(0);
      }
    }
  }

  // adds Goods from the bag to the stand
  public final void updateStand() {
    for (Goods entry : bag) {
      // increment the quantity of each Goods encountered
      if (stand.containsKey(entry)) {
        int quantity = stand.get(entry);
        stand.put(entry, quantity + 1);
      } else {
        stand.put(entry, 1);
      }
      // adds the bonus from any IllegalGoods to the stand
      if (entry instanceof IllegalGoods) {
        HashMap<Goods, Integer> illegalGoodsBonus =
            (HashMap<Goods, Integer>) ((IllegalGoods) entry).getIllegalBonus();
        mergeMaps(stand, illegalGoodsBonus);
      }
    }
    clearBag();
  }

  // used to add a source map containing Goods and their respective quantities to a destination map
  private void mergeMaps(
      final HashMap<Goods, Integer> destination, final HashMap<Goods, Integer> source) {
    source.forEach((goods, quantity) -> destination.put(goods, quantity + destination.get(goods)));
  }

  public abstract void declare();

  public abstract void createBag();

  public abstract void inspect(ArrayList<Player> player);

  final void acceptBribe(final Player p) {
    this.money += p.declaration.bribe;
    p.declaration.bribe = 0;
  }

  final void checkPlayer(final Player p) {
    int penalty;
    // if a player is checked, the bribe is implicitly refused
    refuseBribe(p);
    if (isHonest(p)) {
      // apply penalty to sheriff because player was honest
      penalty = p.bag.get(0).getPenalty() * p.bag.size();
      this.money -= penalty;
      p.money += penalty;
    } else {
      // apply penalty to checked player for each undeclared card
      ArrayList<Goods> undeclaredGoods = new ArrayList<>();
      for (Goods card : p.bag) {
        if (card.getId() != p.declaration.declaredGoods) {
          penalty = card.getPenalty();
          this.money += penalty;
          p.money -= penalty;
          undeclaredGoods.add(card);
        }
      }
      p.confiscate(undeclaredGoods);
    }
  }

  final void refuseBribe(final Player p) {
    if (p.declaration.bribe != 0) {
      p.setMoney(p.getMoney() + p.declaration.bribe);
      p.declaration.bribe = 0;
    }
  }

  private boolean isHonest(final Player p) {
    // check if any of the cards in bag is not of the declared type
    for (Goods card : p.bag) {
      if (card.getId() != p.declaration.declaredGoods) {
        return false;
      }
    }
    return true;
  }

  final boolean lowMoney() {
    return (money < LOW_MONEY);
  }

  // removes all Goods from the bag
  private void clearBag() {
    while (!bag.isEmpty()) {
      bag.remove(0);
    }
  }

  final boolean bagIsNotFull() {
    return (bag.size() < MAX_BAG_SIZE);
  }

  public final void sell() {
    // sell all the goods from the stand
    Iterator i = stand.entrySet().iterator();
    while (i.hasNext()) {
      HashMap.Entry entry = (HashMap.Entry) i.next();
      money += ((Goods) entry.getKey()).getProfit() * (Integer) entry.getValue();
    }
  }
}
