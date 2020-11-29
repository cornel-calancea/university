package hero;

import abilities.Ability;
import abilities.Paralysis;
import abilities.Slam;
import angel.Angel;
import common.Constants;
import map.TerrainTypes;
import strategies.Strategy;

import java.util.ArrayList;
import java.util.Observable;

public abstract class Hero extends Observable implements Comparable<Hero> {
  /* One of the main components of the game - gets into fights with other heroes
   * and applies his abilities on them. A particular type of Hero must extend this class*/
  protected int id;
  protected int hp;
  protected int xp;
  protected int level;
  protected boolean isDead;
  protected Position position;
  protected TerrainTypes terrain;
  /* a player's own abilities */
  protected ArrayList<Ability> abilities;
  /* overtime abilities that damage the player */
  protected Ability overtimeAbility;
  protected Strategy strategy;
  protected Hero enemy;

  @Override
  public final int compareTo(final Hero h) {
    return this.id - h.id;
  }

  public final Strategy getStrategy() {
    return strategy;
  }

  public final void setStrategy(final Strategy strategy) {
    this.strategy = strategy;
  }

  public final boolean isDead() {
    return isDead;
  }

  public final Position getPosition() {
    return position;
  }

  public final ArrayList<Ability> getAbilities() {
    return abilities;
  }

  public final int getHp() {
    return hp;
  }

  public final void setHp(final int hp) {
    this.hp = hp;
    this.isDead = (this.hp <= 0);
  }

  public final void setXp(final int xp) {
    this.xp = xp;
  }

  public final void setDead(final boolean dead) {
    isDead = dead;
  }

  public final int getLevel() {
    return level;
  }

  public final void setOvertimeAbility(final Ability overtimeAbility) {
    this.overtimeAbility = overtimeAbility;
  }

  public final Ability getOvertimeAbility() {
    return overtimeAbility;
  }

  /*Adds XP according to the killed player*/
  public final void addXP(final Hero loser) {
    if (loser.isDead && !this.isDead) {
      this.xp +=
          Math.max(
              0,
              Constants.DEFAULT_KILL_XP
                  - ((this.level - loser.level) * Constants.KILL_XP_PER_LEVEL));
      updateLevel();
    }
  }

  public final void attack(final Hero otherHero) throws CloneNotSupportedException {
    int damage;
    for (Ability ability : this.getAbilities()) {
      damage = ability.damage();
      System.out.println("Race + strategy + angel mod : " + ability.getTotalMod());
      System.out.println(this.getHeroName() + " has dealt " + damage + " to " + otherHero.getHeroName());
      otherHero.setHp(otherHero.getHp() - damage);
    }
    if (otherHero.isDead) {
      setChanged();
      notifyObservers("Player " + otherHero.getHeroName() + " was killed by " + this.getHeroName());
      clearChanged();
    }
  }

  public final TerrainTypes getTerrain() {
    return terrain;
  }

  public final void setTerrain(final TerrainTypes terrain) {
    this.terrain = terrain;
  }

  public final boolean cannotMove() {
    return (this.overtimeAbility instanceof Paralysis) || (this.overtimeAbility instanceof Slam);
  }

  public final void updateOvertimes() {
    if ((overtimeAbility != null) && overtimeAbility.getOvertimeLeft() == 0) {
      overtimeAbility = null;
    }
  }

  public final int xpForLevelUp() {
    /* returns at what xp the hero will level up */
    return Constants.LEVEL_1_XP + level * Constants.LEVELUP_XP;
  }

  public final void updateLevel() {
    if (this.xp < xpForLevelUp()) {
      return;
    }
    int oldLevel = this.level;
    this.level = (this.xp - Constants.LEVEL_1_XP) / Constants.LEVELUP_XP + 1;
    this.hp = getMaxHP();
    for (Ability ability : abilities) {
      ability.updateLevel(this.level);
    }
    for (int i = oldLevel + 1; i <= this.level; i++) {
      setChanged();
      notifyObservers(this.getHeroName() + " reached level " + i);
      clearChanged();
    }
  }

  public final float getRawDamage() {
    /*returns the damage that the player gives without race modifier*/
    float damage = 0f;
    for (Ability ability : this.abilities) {
      damage += ability.getRawDamage();
    }
    return damage;
  }

  public final void abilitiesSetTerrain() {
    for (Ability ability : this.abilities) {
      ability.setTerrainMod(getTerrainAmp());
    }
  }

  public final int getXp() {
    return xp;
  }

  public abstract char getHeroType();

  /*Returns the maximum HP possible for the Hero at his level*/
  public abstract int getMaxHP();

  public abstract float getTerrainAmp();

  /*Adapt the hero according to his enemy*/
  public abstract void becomeEnemy(Hero hero);

  /*Decide what strategy to apply in each round*/
  public abstract void applyStrategy();

  /*only for double dispatch - angel.bless(this)*/
  public abstract void getBlessed(Angel angel);

  /*Returns Hero name and id*/
  public abstract String getHeroName();

  public final Hero getEnemy() {
    return enemy;
  }
}
