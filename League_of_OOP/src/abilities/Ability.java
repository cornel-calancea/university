package abilities;

import hero.*;

import java.util.ArrayList;

public abstract class Ability {
  /* Class made to represent a superpower of a Hero.
   Each Hero has some of those - when he attacks, the ability "is applied" to the enemy.
   Any particular superpower must extend this class and implement its abstract methods.*/
  protected float terrainMod = 0f;
  protected float enemyMod = 0f;
  protected Hero enemy;
  //an array with all the angel modifiers applied to a player
  protected ArrayList<Float> angelMod = new ArrayList<>();
  //all strategy mods will be added to this variable
  protected float strategyMod = 0f;

  public final float getTerrainMod() {
    return terrainMod;
  }

  /*returns the total race modifier(angel + strategy + enemy) */
  public final float getTotalMod() {
    float totalMod = 0f;
    totalMod += getEnemyMod();
    for (Float modifier : angelMod) {
      totalMod += modifier;
    }
    totalMod += getStrategyMod();
    return totalMod;
  }

  public abstract void setTerrainMod(float terrainMod);

  public final float getEnemyMod() {
    return enemyMod;
  }

  public final Hero getEnemy() {
    return enemy;
  }

  public final ArrayList<Float> getAngelMod() {
    return angelMod;
  }

  public final float getStrategyMod() {
    return strategyMod;
  }

  public final void setStrategyMod(final float strategyMod) {
    this.strategyMod = strategyMod;
  }

  @Override
  public final Ability clone() throws CloneNotSupportedException {
    Ability clone = (Ability) super.clone();
    clone.angelMod = (ArrayList<Float>) this.angelMod.clone();
    return clone;
  }

  /*Adapt the ability according to the enemy (set enemy and enemyMod variables)*/
  public abstract void setEnemy(Knight enemy);

  public abstract void setEnemy(Pyromancer enemy);

  public abstract void setEnemy(Rogue enemy);

  public abstract void setEnemy(Wizard enemy);

  /*Return the amount of damage with all modifiers.
    In case the ability has overtime effect, clone it and set it in the overtimeAbility field
    of the enemy.*/
  public abstract int damage() throws CloneNotSupportedException;

  /*Return the damage given by the ability without race modifiers (only with terrainMod)*/
  public abstract int getRawDamage();

  /*apply any overtime effects of the ability to the enemy(defender)*/
  public abstract void overtimeDamage(Hero defender) throws CloneNotSupportedException;

  /*Adapt the ability according to the Hero's level*/
  public abstract void updateLevel(int level);

  /*Return the number of overtime rounds left*/
  public abstract int getOvertimeLeft();
}
