package com.tema1.main;
//
import com.tema1.player.Player;

public final class Turn {
  // the ID of the sheriff in the current turn(sub-round)
  private int sheriff;
  private GameState state;

  private void setSheriff(final int sheriff) {
    this.sheriff = sheriff;
  }

  public Turn(final GameState state) {
    this.state = state;
  }

  void playTurn(final int turnCount) {
    setSheriff(turnCount);
    newHand();
    createBags();
    inspection();
    fillStands();
    clearHands();
  }

  // Players hands may still have cards, because they were kept to know the initial order of the
  // cards in the deck
  private void clearHands() {
    for (Player p : state.getPlayers()) {
      p.getHand().removeAll(p.getHand());
    }
  }

  private void createBags() {
    Player player;
    for (int playerCount = 0; playerCount < state.getNrPlayers(); playerCount++) {
      if (playerCount == sheriff) {
        continue;
      }
      player = state.getPlayers().get(playerCount);
      player.createBag();
    }
  }

  private void inspection() {
    state.getPlayers().get(sheriff).inspect(state.getPlayers());
  }

  private void newHand() {
    Player player;
    for (int playerCount = 0; playerCount < state.getNrPlayers(); playerCount++) {
      if (playerCount == sheriff) {
        continue;
      }
      player = state.getPlayers().get(playerCount);
      player.pickCards(state.getDeck());
    }
  }

  private void fillStands() {
    for (Player p : state.getPlayers()) {
      p.updateStand();
    }
  }
}
