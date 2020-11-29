package com.tema1.main;
/*to keep the defining elements of the game in the same place, this class has been created*/
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.player.Basic;
import com.tema1.player.Bribe;
import com.tema1.player.Greedy;
import com.tema1.player.Player;

import java.util.ArrayList;
import java.util.List;

public final class GameState {
  // stores the total number of players in the game
  private int nrPlayers;
  // stores the number of rounds to be played
  private int nrRounds;
  // stores the count of the current round played
  // in particular used to define the behaviour of Greedy players
  private static int currentRound = 0;
  // an array with all the players in the game
  private ArrayList<Player> players;
  // stores all the cards in the game in the given order
  private ArrayList<Goods> deck;

  public static int getCurrentRound() {
    return currentRound;
  }

  public static void setCurrentRound(final int currentRound) {
    GameState.currentRound = currentRound;
  }

  int getNrPlayers() {
    return nrPlayers;
  }

  int getNrRounds() {
    return nrRounds;
  }

  public GameState(final GameInput input) {
    this.nrPlayers = input.getPlayerNames().size();
    this.nrRounds = input.getRounds();
    this.players = createPlayerList(input.getPlayerNames());
    this.deck = createDeck(input.getAssetIds());
  }

  // creates the array of players according to the given list
  private static ArrayList<Player> createPlayerList(final List<String> playerNames) {
    int id = 0;
    ArrayList<Player> playerList = new ArrayList<Player>();
    for (String type : playerNames) {
      switch (type) {
        case "basic":
          playerList.add(new Basic(id));
          break;
        case "bribed":
          playerList.add(new Bribe(id));
          break;
        case "greedy":
          playerList.add(new Greedy(id));
        default:
      }
      id++;
    }
    return playerList;
  }

  // creates the deck of cards according to given asset IDs
  private static ArrayList<Goods> createDeck(final List<Integer> assetIds) {
    ArrayList<Goods> deck = new ArrayList<Goods>();
    GoodsFactory factory = GoodsFactory.getInstance();
    for (Integer asset : assetIds) {
      deck.add(factory.getGoodsById(asset));
    }
    return deck;
  }

  public ArrayList<Player> getPlayers() {
    return players;
  }

  public ArrayList<Goods> getDeck() {
    return deck;
  }
}
