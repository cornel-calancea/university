package abilities;

import common.Constants;
import hero.*;

public final class Drain extends Ability {
  private float percent = Constants.DRAIN_PERCENTAGE;

  @Override
  public void setTerrainMod(final float modifier) {
    this.terrainMod = modifier;
  }

  @Override
  public void setEnemy(final Knight enemy) {
    this.enemyMod = Constants.DRAIN_MOD_KNIGHT;
    this.enemy = enemy;
  }

  @Override
  public void setEnemy(final Pyromancer enemy) {
    this.enemyMod = Constants.DRAIN_MOD_PYRO;
    this.enemy = enemy;
  }

  @Override
  public void setEnemy(final Rogue enemy) {
    this.enemyMod = Constants.DRAIN_MOD_ROGUE;
    this.enemy = enemy;
  }

  @Override
  public void setEnemy(final Wizard enemy) {
    this.enemyMod = Constants.DRAIN_MOD_WIZARD;
    this.enemy = enemy;
  }

  @Override
  public int damage() {
    float baseHP = Math.min(Constants.DRAIN_BASE_HP * enemy.getMaxHP(), enemy.getHp());
    return Math.round(percent * getTotalMod() * getTerrainMod() * baseHP);
  }

  @Override
  public int getRawDamage() {
    float baseHP = Math.min(Constants.DRAIN_BASE_HP * enemy.getMaxHP(), enemy.getHp());
    return Math.round(percent * terrainMod * baseHP);
  }

  @Override
  public void overtimeDamage(final Hero defender) {
  }

  @Override
  public void updateLevel(final int level) {
    percent = Constants.DRAIN_PERCENTAGE + (level * Constants.DRAIN_LEVELUP_PERCENTAGE);
  }

  @Override
  public int getOvertimeLeft() {
    return 0;
  }
}
