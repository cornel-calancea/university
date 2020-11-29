package angel;

import hero.*;
import magician.Magician;
import map.Tile;

import java.util.Observable;

public abstract class Angel extends Observable {
  private Position position;
  private AngelType angelType;
  private Boolean isGoodAngel;
  public int x = 9;

  public final Position getPosition() {
    return position;
  }

  public final void setGoodAngel(final Boolean goodAngel) {
    isGoodAngel = goodAngel;
  }

  public final AngelType getAngelType() {
    return angelType;
  }

  public final void setAngelType(final AngelType angelType) {
    this.angelType = angelType;
  }

  public final void setPosition(final Position position) {
    this.position = position;
  }

  /*sends an angel to a zone of the map*/
  public final void deploy(final Tile areaOfDeployment) {
    /*notify the Great Mage about the position of deployment*/
    setChanged();
    notifyObservers(
        "Angel "
            + this.getAngelType()
            + " was spawned at "
            + this.position.getRow()
            + " "
            + this.position.getColumn());
    clearChanged();
    /* influence all alive players or revive player if angel is Spawner*/
    for (Hero hero : areaOfDeployment.getHeroes()) {
      if (hero.isDead() ^ (this.angelType == AngelType.Spawner)) {

        continue;
      }
      setChanged();
      if (this.isGoodAngel) {
        notifyObservers(this.getAngelType() + " helped " + hero.getHeroName());
      } else {
        notifyObservers(this.getAngelType() + " hit " + hero.getHeroName());
      }
      clearChanged();
      hero.getBlessed(this);
      if (hero.isDead()) {
        setChanged();
        notifyObservers("Player " + hero.getHeroName() + " was killed by an angel");
        clearChanged();
      }
    }
  }

  public Angel(final Position position) {
    this.position = position;
    this.addObserver(Magician.getInstance());
  }

  public Angel() {}

  public abstract void bless(Knight hero);

  public abstract void bless(Pyromancer hero);

  public abstract void bless(Rogue hero);

  public abstract void bless(Wizard hero);
}
