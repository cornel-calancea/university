package hero;

import abilities.Ability;
import abilities.Fireblast;
import abilities.Ignite;
import angel.Angel;
import common.Constants;
import magician.Magician;
import map.TerrainTypes;
import strategies.PyroAttackingStrategy;
import strategies.PyroDefensiveStrategy;

import java.util.ArrayList;

public final class Pyromancer extends Hero {
    public Pyromancer(final Position position, final int id) {
        this.id = id;
        this.hp = Constants.PYRO_HP;
        this.xp = 0;
        this.level = 0;
        this.position = position;
        this.abilities = new ArrayList<>();
        abilities.add(new Fireblast());
        abilities.add(new Ignite());
        this.addObserver(Magician.getInstance());
    }

    @Override
    public char getHeroType() {
        return 'P';
    }

    @Override
    public int getMaxHP() {
        return Constants.PYRO_HP + (level * Constants.PYRO_LEVELUP_HP);
    }

    @Override
    public float getTerrainAmp() {
        if (terrain.equals(TerrainTypes.Volcanic)) {
            return 1 + Constants.PYRO_VOLCANIC_BONUS;
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
        if (((float) this.hp / this.getMaxHP()) < Constants.PYRO_STRATEGY_HP_LOWER_LIMIT) {
            this.strategy = new PyroDefensiveStrategy();
            strategy.adapt(this);
            return;
        }
        if (((float) this.hp / this.getMaxHP()) < Constants.PYRO_STRATEGY_HP_UPPER_LIMIT) {
            this.strategy = new PyroAttackingStrategy();
            strategy.adapt(this);
        }
    }

    @Override
    public void getBlessed(final Angel angel) {
        angel.bless(this);
    }

    @Override
    public String getHeroName() {
        return "Pyromancer " + id;
    }
}
