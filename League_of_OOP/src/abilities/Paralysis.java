package abilities;

import common.Constants;
import hero.*;
import map.TerrainTypes;

public final class Paralysis extends Ability implements Cloneable {
    private int baseDamage = Constants.PARALYSIS_DAMAGE;
    private int overtimeLeft;

    @Override
    public void setTerrainMod(final float modifier) {
        this.terrainMod = modifier;
        if (terrainMod > 1) {
            overtimeLeft = Constants.PARALYSIS_OVERTIME_ROUNDS_WOODS;
        } else {
            overtimeLeft = Constants.PARALYSIS_OVERTIME_ROUNDS;
        }
    }

    @Override
    public void setEnemy(final Knight enemy) {
        this.enemyMod = Constants.PARALYSIS_MOD_KNIGHT;
        this.enemy = enemy;
    }

    @Override
  public void setEnemy(final Pyromancer enemy) {
    this.enemyMod = Constants.PARALYSIS_MOD_PYRO;
    this.enemy = enemy;
  }

  @Override
  public void setEnemy(final Rogue enemy) {
    this.enemyMod = Constants.PARALYSIS_MOD_ROGUE;
    this.enemy = enemy;
  }

  @Override
  public void setEnemy(final Wizard enemy) {
    this.enemyMod = Constants.PARALYSIS_MOD_WIZARD;
    this.enemy = enemy;
  }

  @Override
  public int damage() throws CloneNotSupportedException {
    // create a clone of this ability so that it doesn't change in the future
    Paralysis clone = (Paralysis) this.clone();
    if (enemy.getTerrain().equals(TerrainTypes.Woods)) {
      clone.overtimeLeft = Constants.PARALYSIS_OVERTIME_ROUNDS_WOODS;
    } else {
      clone.overtimeLeft = Constants.PARALYSIS_OVERTIME_ROUNDS;
    }
    enemy.setOvertimeAbility(clone);
      return Math.round(getTotalMod() * getRawDamage());
  }

  @Override
  public int getRawDamage() {
    return Math.round(baseDamage * terrainMod);
  }

  @Override
  public void overtimeDamage(final Hero defender) {
      defender.setHp(defender.getHp() - Math.round(baseDamage * getTotalMod() * terrainMod));
      overtimeLeft--;
  }

  @Override
  public void updateLevel(final int level) {
    this.baseDamage = Constants.PARALYSIS_DAMAGE + (level * Constants.PARALYSIS_LEVELUP_DAMAGE);
  }

  @Override
  public int getOvertimeLeft() {
    return overtimeLeft;
  }
}
