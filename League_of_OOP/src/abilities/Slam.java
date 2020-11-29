package abilities;

import common.Constants;
import hero.*;

public final class Slam extends Ability implements Cloneable {
    private int overtimeLeft;
    private int baseDamage = Constants.SLAM_DAMAGE;

    @Override
    public void setTerrainMod(final float modifier) {
        this.terrainMod = modifier;
    }

    @Override
    public void setEnemy(final Knight enemy) {
        this.enemyMod = Constants.SLAM_MOD_KNIGHT;
        this.enemy = enemy;
    }

    @Override
    public void setEnemy(final Pyromancer enemy) {
        this.enemyMod = Constants.SLAM_MOD_PYRO;
        this.enemy = enemy;
    }

  @Override
  public void setEnemy(final Rogue enemy) {
    this.enemyMod = Constants.SLAM_MOD_ROGUE;
    this.enemy = enemy;
  }

  @Override
  public void setEnemy(final Wizard enemy) {
    this.enemyMod = Constants.SLAM_MOD_WIZARD;
    this.enemy = enemy;
  }

  @Override
  public int damage() throws CloneNotSupportedException {
    Slam clone = (Slam) this.clone();
    clone.overtimeLeft = 1;
    enemy.setOvertimeAbility(clone);
      return Math.round(getRawDamage() * getTotalMod());
  }

  @Override
  public int getRawDamage() {
    return Math.round(terrainMod * baseDamage);
  }

  @Override
  public void overtimeDamage(final Hero defender) {
    overtimeLeft--;
  }

  @Override
  public void updateLevel(final int level) {
    baseDamage = Constants.SLAM_DAMAGE + (Constants.SLAM_LEVELUP_DAMAGE * level);
  }

  @Override
  public int getOvertimeLeft() {
    return overtimeLeft;
  }
}
