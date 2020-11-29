package angel;

import abilities.Ability;
import common.Constants;
import hero.*;

public final class LevelUpAngel extends Angel {
    public LevelUpAngel(final Position position) {
        super(position);
        this.setAngelType(AngelType.LevelUpAngel);
        this.setGoodAngel(true);
    }

    @Override
    public void bless(final Knight hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.LEVELUP_ANGEL_KNIGHT_MOD);
            }
        }
        hero.setXp(hero.xpForLevelUp());
        hero.updateLevel();
    }

    @Override
    public void bless(final Pyromancer hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.LEVELUP_ANGEL_PYRO_MOD);
            }
        }
        hero.setXp(hero.xpForLevelUp());
        hero.updateLevel();
    }

    @Override
    public void bless(final Rogue hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.LEVELUP_ANGEL_ROGUE_MOD);
            }
        }
        hero.setXp(hero.xpForLevelUp());
        hero.updateLevel();
    }

    @Override
    public void bless(final Wizard hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.LEVELUP_ANGEL_WIZARD_MOD);
            }
        }
        hero.setXp(hero.xpForLevelUp());
        hero.updateLevel();
    }
}
