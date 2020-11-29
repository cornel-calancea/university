package angel;

import hero.*;

public final class Doomer extends Angel {
    public Doomer(final Position position) {
        super(position);
        this.setAngelType(AngelType.TheDoomer);
        this.setGoodAngel(false);
    }

    @Override
    public void bless(final Knight hero) {
        hero.setHp(0);
        hero.setDead(true);
    }

    @Override
    public void bless(final Pyromancer hero) {
        hero.setHp(0);
        hero.setDead(true);
    }

    @Override
    public void bless(final Rogue hero) {
        hero.setHp(0);
        hero.setDead(true);
    }

    @Override
    public void bless(final Wizard hero) {
        hero.setHp(0);
        hero.setDead(true);
    }
}
