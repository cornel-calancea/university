package hero;

import abilities.Ability;
import abilities.Deflect;
import abilities.Drain;
import angel.Angel;
import common.Constants;
import magician.Magician;
import map.TerrainTypes;
import strategies.WizardAttackingStrategy;
import strategies.WizardDefensiveStrategy;

import java.util.ArrayList;

public final class Wizard extends Hero {
    public Wizard(final Position position, final int id) {
        this.id = id;
        this.position = position;
        this.hp = Constants.WIZARD_HP;
        this.level = 0;
        this.abilities = new ArrayList<>();
        abilities.add(new Drain());
        abilities.add(new Deflect());
        this.addObserver(Magician.getInstance());
    }

    @Override
    public char getHeroType() {
        return 'W';
    }

    @Override
    public int getMaxHP() {
        return Constants.WIZARD_HP + (level * Constants.WIZARD_LEVELUP_HP);
    }

    @Override
    public float getTerrainAmp() {
        if (terrain.equals(TerrainTypes.Desert)) {
            return 1 + Constants.WIZARD_DESERT_BONUS;
        }
        return 1.0f;
    }

    @Override
    public void becomeEnemy(final Hero hero) {
        for (Ability ability : hero.abilities) {
            ability.setEnemy(this);
        }
        hero.enemy = this;
    }

    @Override
    public void applyStrategy() {
        if (this.cannotMove()) {
            return;
        }
        if (((float) this.hp / this.getMaxHP()) < Constants.WIZARD_STRATEGY_HP_LOWER_LIMIT) {
            this.strategy = new WizardDefensiveStrategy();
            strategy.adapt(this);
            return;
        }
        if (((float) this.hp / this.getMaxHP()) < Constants.WIZARD_STRATEGY_HP_UPPER_LIMIT) {
            this.strategy = new WizardAttackingStrategy();
            strategy.adapt(this);
        }
    }

    @Override
    public void getBlessed(final Angel angel) {
        angel.bless(this);
    }

    @Override
    public String getHeroName() {
        return "Wizard " + id;
    }
}
