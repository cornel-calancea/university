package strategies;

import abilities.Ability;
import common.Constants;
import hero.Hero;

public final class KnightDefensiveStrategy implements Strategy {
  @Override
  public void adapt(final Hero h) {
    h.setHp((h.getHp() + (int) (h.getHp() * Constants.KNIGHT_HP_BONUS_DEFENSIVE)));
    for (Ability ability : h.getAbilities()) {
      if (Float.compare(ability.getEnemyMod(), 1f) == 0) {
        continue;
      } else {
        ability.setStrategyMod(ability.getStrategyMod() + Constants.KNIGHT_MOD_BONUS_DEFENSIVE);
      }
    }
  }
}
