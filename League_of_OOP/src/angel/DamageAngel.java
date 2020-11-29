package angel;

import abilities.Ability;
import common.Constants;
import hero.*;

public final class DamageAngel extends Angel {
    public int x = 2;
    public DamageAngel(final Position position) {
        super(position);
        this.setAngelType(AngelType.DamageAngel);
        this.setGoodAngel(true);
    }

    public DamageAngel() {
    }

    @Override
    public void bless(final Knight hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.DAMAGE_ANGEL_KNIGHT_MOD);
            }
        }
    }

    @Override
    public void bless(final Pyromancer hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.DAMAGE_ANGEL_PYRO_MOD);
            }
        }
    }

    @Override
    public void bless(final Rogue hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.DAMAGE_ANGEL_ROGUE_MOD);
            }
        }
    }

    @Override
    public void bless(final Wizard hero) {
        for (Ability ability : hero.getAbilities()) {
            if (ability.getEnemyMod() != 1) {
                ability.getAngelMod().add(Constants.DAMAGE_ANGEL_WIZARD_MOD);
            }
        }
    }
}
