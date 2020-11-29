package angel;

import abilities.Ability;
import common.Constants;
import hero.*;

public final class SmallAngel extends Angel {
    public SmallAngel(final Position position) {
        super(position);
        this.setAngelType(AngelType.SmallAngel);
        this.setGoodAngel(true);
    }

    @Override
    public void bless(final Knight hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.SMALL_ANGEL_KNIGHT_MOD);
            }
        }
        hero.setHp(hero.getHp() + Constants.SMALL_ANGEL_KNIGHT_HP_BONUS);
    }

    @Override
    public void bless(final Pyromancer hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.SMALL_ANGEL_PYRO_MOD);
            }
        }
        hero.setHp(hero.getHp() + Constants.SMALL_ANGEL_PYRO_HP_BONUS);
    }

    @Override
    public void bless(final Rogue hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.SMALL_ANGEL_ROGUE_MOD);
            }
        }
        hero.setHp(hero.getHp() + Constants.SMALL_ANGEL_ROGUE_HP_BONUS);
    }

    @Override
    public void bless(final Wizard hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.SMALL_ANGEL_WIZARD_MOD);
            }
        }
        hero.setHp(hero.getHp() + Constants.SMALL_ANGEL_WIZARD_HP_BONUS);
    }
}
