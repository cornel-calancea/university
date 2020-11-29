package angel;

import abilities.Ability;
import common.Constants;
import hero.*;

public final class GoodBoy extends Angel {
    public GoodBoy(final Position position) {
        super(position);
        this.setAngelType(AngelType.GoodBoy);
        this.setGoodAngel(true);
    }

    @Override
    public void bless(final Knight hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.GOODBOY_KNIGHT_MOD);
            }
        }
        hero.setHp(hero.getHp() + Constants.GOODBOY_KNIGHT_HP_BONUS);
    }

    @Override
    public void bless(final Pyromancer hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.GOODBOY_PYRO_MOD);
            }
        }
        hero.setHp(hero.getHp() + Constants.GOODBOY_PYRO_HP_BONUS);
    }

    @Override
    public void bless(final Rogue hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.GOODBOY_ROGUE_MOD);
            }
        }
        hero.setHp(hero.getHp() + Constants.GOODBOY_ROGUE_HP_BONUS);
    }

    @Override
    public void bless(final Wizard hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.GOODBOY_WIZARD_MOD);
            }
        }
        hero.setHp(hero.getHp() + Constants.GOODBOY_WIZARD_HP_BONUS);
    }
}
