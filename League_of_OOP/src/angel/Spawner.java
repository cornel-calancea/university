package angel;

import common.Constants;
import hero.*;

public final class Spawner extends Angel {
    public Spawner(final Position position) {
        super(position);
        this.setAngelType(AngelType.Spawner);
        this.setGoodAngel(true);
    }

    @Override
    public void bless(final Knight hero) {
        if (hero.isDead()) {
            hero.setDead(false);
            hero.setHp(Constants.SPAWNER_KNIGHT_HP);
            setChanged();
            notifyObservers("Player " + hero.getHeroName() + " was brought to life by an angel");
            clearChanged();
        }
    }

    @Override
    public void bless(final Pyromancer hero) {
        if (hero.isDead()) {
            hero.setDead(false);
            hero.setHp(Constants.SPAWNER_PYRO_HP);
            setChanged();
            notifyObservers("Player " + hero.getHeroName() + " was brought to life by an angel");
            clearChanged();
        }
    }

    @Override
    public void bless(final Rogue hero) {
        if (hero.isDead()) {
            hero.setDead(false);
            hero.setHp(Constants.SPAWNER_ROGUE_HP);
            setChanged();
            notifyObservers("Player " + hero.getHeroName() + " was brought to life by an angel");
            clearChanged();
        }
    }

    @Override
    public void bless(final Wizard hero) {
        if (hero.isDead()) {
            hero.setDead(false);
            hero.setHp(Constants.SPAWNER_WIZARD_HP);
            setChanged();
            notifyObservers("Player " + hero.getHeroName() + " was brought to life by an angel");
            clearChanged();
        }
    }
}
