package abilities;

import common.Constants;
import hero.*;

public final class Backstab extends Ability {
    private int criticalHitCounter = -1;
    private int baseDamage = Constants.BACKSTAB_DAMAGE;

    @Override
    public void setTerrainMod(final float modifier) {
        criticalHitCounter++;
        this.terrainMod = modifier;
    }

    @Override
    public void setEnemy(final Knight enemy) {
        enemyMod = Constants.BACKSTAB_MOD_KNIGHT;
    }

    @Override
    public void setEnemy(final Pyromancer enemy) {
        enemyMod = Constants.BACKSTAB_MOD_PYRO;
    }

  @Override
  public void setEnemy(final Rogue enemy) {
    enemyMod = Constants.BACKSTAB_MOD_ROGUE;
  }

  @Override
  public void setEnemy(final Wizard enemy) {
    enemyMod = Constants.BACKSTAB_MOD_WIZARD;
  }

  @Override
  public int damage() {
      return Math.round(getRawDamage() * getTotalMod());
  }

  @Override
  public int getRawDamage() {
    if ((criticalHitCounter % 3) == 0 && terrainMod > 1) {
        //critical hit
      return Math.round(1.5f * baseDamage * terrainMod);
    }
    return Math.round(baseDamage * terrainMod);
  }

  @Override
  public void overtimeDamage(final Hero defender) {
  }

  @Override
  public void updateLevel(final int level) {
    baseDamage = Constants.BACKSTAB_DAMAGE + (level * Constants.BACKSTAB_LEVELUP_DAMAGE);
  }

  @Override
  public int getOvertimeLeft() {
    return 0;
  }


}
