package abilities;

import common.Constants;
import hero.*;

public final class Deflect extends Ability {
    private float percent = Constants.DEFLECT_PERCENTAGE;

    @Override
    public void setTerrainMod(final float modifier) {
        terrainMod = modifier;
    }

    @Override
    public void setEnemy(final Knight enemy) {
        this.enemy = enemy;
        this.enemyMod = Constants.DEFLECT_MOD_KNIGHT;
    }

    @Override
    public void setEnemy(final Pyromancer enemy) {
        this.enemy = enemy;
        this.enemyMod = Constants.DEFLECT_MOD_PYRO;
    }

    @Override
  public void setEnemy(final Rogue enemy) {
    this.enemy = enemy;
    this.enemyMod = Constants.DEFLECT_MOD_ROGUE;
  }

  @Override
  public void setEnemy(final Wizard enemy) {
    this.enemy = enemy;
    this.enemyMod = Constants.DEFLECT_MOD_WIZARD;
  }

  @Override
  public int damage() {
      // doesn't apply to wizards
      if (enemy.getHeroType() == 'W') {
          return 0;
      }
      return Math.round(percent * getTotalMod() * Math.round(terrainMod * enemy.getRawDamage()));
  }

  @Override
  public int getRawDamage() {
    return 0;
  }

  @Override
  public void overtimeDamage(final Hero defender) {
  }

  @Override
  public void updateLevel(final int level) {
      percent =
              Math.min(
                      Constants.DEFLECT_PERCENTAGE + level * Constants.DEFLECT_LEVELUP_PERCENTAGE,
                      Constants.DEFLECT_UPPER_LIMIT);
  }

  @Override
  public int getOvertimeLeft() {
    return 0;
  }
}
