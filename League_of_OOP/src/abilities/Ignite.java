package abilities;

import common.Constants;
import hero.*;

public final class Ignite extends Ability implements Cloneable {
  private int overtimeLeft;
  private int baseDamage = Constants.IGNITE_DAMAGE;
  private int overtimeDamage = Constants.IGNITE_OVERTIME_DAMAGE;

  @Override
  public void setTerrainMod(final float modifier) {
    this.terrainMod = modifier;
  }

  @Override
  public void setEnemy(final Knight enemy) {
    this.enemy = enemy;
    this.enemyMod = Constants.IGNITE_MOD_KNIGHT;
  }

  @Override
  public void setEnemy(final Pyromancer enemy) {
    this.enemy = enemy;
    this.enemyMod = Constants.IGNITE_MOD_PYRO;
  }

  @Override
  public void setEnemy(final Rogue enemy) {
    this.enemy = enemy;
    this.enemyMod = Constants.IGNITE_MOD_ROGUE;
  }

  @Override
  public void setEnemy(final Wizard enemy) {
    this.enemy = enemy;
    this.enemyMod = Constants.IGNITE_MOD_WIZARD;
  }

  @Override
  public int damage() throws CloneNotSupportedException {
    Ignite clone = (Ignite) this.clone();
    clone.overtimeLeft = Constants.IGNITE_OVERTIME;
    enemy.setOvertimeAbility(clone);
    return Math.round(getRawDamage() * getTotalMod());
  }

  @Override
  public int getRawDamage() {
    return Math.round(baseDamage * terrainMod);
  }

  @Override
  public void overtimeDamage(final Hero defender) {
    overtimeLeft--;
    defender.setHp(
            defender.getHp()
                    - Math.round(Math.round(this.overtimeDamage * terrainMod) * getTotalMod()));
  }

  @Override
  public void updateLevel(final int level) {
    baseDamage = Constants.IGNITE_DAMAGE + (Constants.IGNITE_LEVELUP_DAMAGE * level);
    overtimeDamage = Constants.IGNITE_OVERTIME_DAMAGE + (Constants.IGNITE_LEVELUP_OVERTIME * level);
  }

  @Override
  public int getOvertimeLeft() {
    return overtimeLeft;
  }
}
