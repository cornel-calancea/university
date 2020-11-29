package strategies;

import abilities.Ability;
import common.Constants;
import hero.Hero;

public final class PyroDefensiveStrategy implements Strategy {
  public void adapt(final Hero h) {
    h.setHp((int) (h.getHp() * (1 + Constants.PYRO_HP_BONUS_DEFENSIVE)));
    for (Ability ability : h.getAbilities()) {
      if (Float.compare(ability.getEnemyMod(), 1f) == 0) {
        continue;
      } else {
        ability.setStrategyMod(ability.getStrategyMod() + Constants.PYRO_MOD_BONUS_DEFENSIVE);
      }
    }
  }
}
