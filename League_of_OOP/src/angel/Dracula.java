package angel;

import abilities.Ability;
import common.Constants;
import hero.*;

public final class Dracula extends Angel {
    public Dracula(final Position position) {
        super(position);
        this.setAngelType(AngelType.Dracula);
        this.setGoodAngel(false);
    }

    @Override
    public void bless(final Knight hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.DRACULA_KNIGHT_MOD);
            }
        }
        hero.setHp(hero.getHp() + Constants.DRACULA_KNIGHT_HP_BONUS);
    }

    @Override
    public void bless(final Pyromancer hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.DRACULA_PYRO_MOD);
            }
        }
        hero.setHp(hero.getHp() + Constants.DRACULA_PYRO_HP_BONUS);
    }

    @Override
    public void bless(final Rogue hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.DRACULA_ROGUE_MOD);
            }
        }
        hero.setHp(hero.getHp() + Constants.DRACULA_ROGUE_HP_BONUS);
    }

    @Override
    public void bless(final Wizard hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.DRACULA_WIZARD_MOD);
            }
        }
        hero.setHp(hero.getHp() + Constants.DRACULA_WIZARD_HP_BONUS);
    }
}
