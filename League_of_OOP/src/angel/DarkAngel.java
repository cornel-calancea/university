package angel;

import common.Constants;
import hero.*;

public final class DarkAngel extends Angel {
    public DarkAngel(final Position position) {
        super(position);
        this.setAngelType(AngelType.DarkAngel);
        this.setGoodAngel(false);
    }

    @Override
    public void bless(final Knight hero) {
        hero.setHp(hero.getHp() + Constants.DARK_ANGEL_KNIGHT_HP_BONUS);
    }

    @Override
    public void bless(final Pyromancer hero) {
        hero.setHp(hero.getHp() + Constants.DARK_ANGEL_PYRO_HP_BONUS);
    }

    @Override
    public void bless(final Rogue hero) {
        hero.setHp(hero.getHp() + Constants.DARK_ANGEL_ROGUE_HP_BONUS);
    }

    @Override
    public void bless(final Wizard hero) {
        hero.setHp(hero.getHp() + Constants.DARK_ANGEL_WIZARD_HP_BONUS);
    }
}
