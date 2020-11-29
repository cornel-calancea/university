package abilities;

import common.Constants;
import hero.*;

public final class Fireblast extends Ability {
    private int baseDamage = Constants.FIREBLAST_DAMAGE;

    @Override
    public void setTerrainMod(final float modifier) {
        terrainMod = modifier;
    }

    @Override
    public void setEnemy(final Knight enemy) {
        enemyMod = Constants.FIREBLAST_MOD_KNIGHT;
    }

    @Override
    public void setEnemy(final Pyromancer enemy) {
        enemyMod = Constants.FIREBLAST_MOD_PYRO;
    }

    @Override
    public void setEnemy(final Rogue enemy) {
        enemyMod = Constants.FIREBLAST_MOD_ROGUE;
  }

  @Override
  public void setEnemy(final Wizard enemy) {
    enemyMod = Constants.FIREBLAST_MOD_WIZARD;
  }

  @Override
  public int damage() {
      return Math.round(getRawDamage() * getTotalMod());
  }

  @Override
  public int getRawDamage() {
    return Math.round(baseDamage * terrainMod);
  }

  @Override
  public void overtimeDamage(final Hero defender) {
  }

  @Override
  public void updateLevel(final int level) {
    baseDamage = Constants.FIREBLAST_DAMAGE + (Constants.FIREBLAST_LEVELUP_DAMAGE * level);
  }

  @Override
  public int getOvertimeLeft() {
    return 0;
  }
}
