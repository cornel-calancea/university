package com.tema1.main;
// this class stores the state of the game and has the methods that define the course of a round
import com.tema1.goods.Goods;
import com.tema1.player.Player;

final class Round {
  private GameState state;
  private Turn turn;

  Round(final GameState state) {
    this.state = state;
    this.turn = new Turn(state);
  }

  // plays a single round
  void playRound() {
    for (int turnCount = 0; turnCount < state.getNrPlayers(); turnCount++) {
      turn.playTurn(turnCount);
    }
    addConfiscatedGoods();
  }

  // inserts confiscated goods at the end of the deck
  private void addConfiscatedGoods() {
    for (Player p : state.getPlayers()) {
      for (Goods card : p.getHand()) {
        state.getDeck().add(card);
      }
      while (!p.getHand().isEmpty()) {
        p.getHand().remove(0);
      }
    }
  }
}
