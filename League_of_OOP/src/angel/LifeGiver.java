package angel;

import common.Constants;
import hero.*;

public final class LifeGiver extends Angel {
    public LifeGiver(final Position position) {
        super(position);
        this.setAngelType(AngelType.LifeGiver);
        this.setGoodAngel(true);
    }

    @Override
    public void bless(final Knight hero) {
        hero.setHp(Math.min(hero.getHp() + Constants.LIFEGIVER_KNIGHT_HP_BONUS, hero.getMaxHP()));
    }

    @Override
    public void bless(final Pyromancer hero) {
        hero.setHp(Math.min(hero.getHp() + Constants.LIFEGIVER_PYRO_HP_BONUS, hero.getMaxHP()));
    }

    @Override
    public void bless(final Rogue hero) {
        hero.setHp(Math.min(hero.getHp() + Constants.LIFEGIVER_ROGUE_HP_BONUS, hero.getMaxHP()));
    }

    @Override
    public void bless(final Wizard hero) {
        hero.setHp(Math.min(hero.getHp() + Constants.LIFEGIVER_WIZARD_HP_BONUS, hero.getMaxHP()));
    }
}
