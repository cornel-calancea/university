package abilities;

import common.Constants;
import hero.*;

public final class Execute extends Ability {
    private float percentageLimit = Constants.EXECUTE_HP_PERCENTAGE_LIMIT;
    private int baseDamage = Constants.EXECUTE_DAMAGE;

    @Override
    public void setTerrainMod(final float modifier) {
        this.terrainMod = modifier;
    }

    @Override
    public void setEnemy(final Knight enemy) {
        this.enemy = enemy;
        this.enemyMod = Constants.EXECUTE_MOD_KNIGHT;
    }

    @Override
    public void setEnemy(final Pyromancer enemy) {
        this.enemy = enemy;
        this.enemyMod = Constants.EXECUTE_MOD_PYRO;
    }

  @Override
  public void setEnemy(final Rogue enemy) {
    this.enemy = enemy;
    this.enemyMod = Constants.EXECUTE_MOD_ROGUE;
  }

  @Override
  public void setEnemy(final Wizard enemy) {
    this.enemy = enemy;
    this.enemyMod = Constants.EXECUTE_MOD_WIZARD;
  }

  @Override
  public int damage() {
    float hpLimit = percentageLimit * enemy.getMaxHP();
    if ((enemy.getHp() < hpLimit) && (enemy.getHp() > 0)) {
      return enemy.getHp();
    } else {
        return Math.round(getRawDamage() * getTotalMod());
    }
  }

  @Override
  public int getRawDamage() {
    float hpLimit = percentageLimit * enemy.getMaxHP();
    if ((enemy.getHp() < hpLimit) && (enemy.getHp() > 0)) {
      return enemy.getHp();
    } else {
      return Math.round(baseDamage * terrainMod);
    }
  }

  @Override
  public void overtimeDamage(final Hero defender) {
  }

  @Override
  public void updateLevel(final int level) {
      baseDamage = Constants.EXECUTE_DAMAGE + (level * Constants.EXECUTE_LEVELUP_DAMAGE);
      percentageLimit =
              Math.min(
                      Constants.EXECUTE_MAX_PERCENTAGE,
                      (level * Constants.EXECUTE_LEVELUP_PERCENTAGE) + Constants.EXECUTE_HP_PERCENTAGE_LIMIT);
  }

  @Override
  public int getOvertimeLeft() {
    return 0;
  }
}
