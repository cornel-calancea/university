package angel;

import common.Constants;
import hero.*;

public final class XPAngel extends Angel {
    public XPAngel(final Position position) {
        super(position);
        this.setAngelType(AngelType.XPAngel);
        this.setGoodAngel(true);
    }

    @Override
    public void bless(final Knight hero) {
        hero.setXp(hero.getXp() + Constants.XP_ANGEL_KNIGHT_HP_BONUS);
        hero.updateLevel();
    }

    @Override
    public void bless(final Pyromancer hero) {
        hero.setXp(hero.getXp() + Constants.XP_ANGEL_PYRO_HP_BONUS);
        hero.updateLevel();
    }

    @Override
    public void bless(final Rogue hero) {
        hero.setXp(hero.getXp() + Constants.XP_ANGEL_ROGUE_HP_BONUS);
        hero.updateLevel();
    }

    @Override
    public void bless(final Wizard hero) {
        hero.setXp(hero.getXp() + Constants.XP_ANGEL_WIZARD_HP_BONUS);
        hero.updateLevel();
    }
}
