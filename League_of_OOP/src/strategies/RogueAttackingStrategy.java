package strategies;

import abilities.Ability;
import common.Constants;
import hero.Hero;

public final class RogueAttackingStrategy implements Strategy {
  @Override
  public void adapt(final Hero h) {
    h.setHp((int) (h.getHp() * (1 + Constants.ROGUE_HP_BONUS_ATTACKING)));
    for (Ability ability : h.getAbilities()) {
      if (Float.compare(ability.getEnemyMod(), 1f) == 0) {
        continue;
      } else {
        ability.setStrategyMod(ability.getStrategyMod() + Constants.ROGUE_MOD_BONUS_ATTACKING);
      }
    }
  }
}
