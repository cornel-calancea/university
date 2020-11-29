package hero;

import abilities.Ability;
import abilities.Backstab;
import abilities.Paralysis;
import angel.Angel;
import common.Constants;
import magician.Magician;
import map.TerrainTypes;
import strategies.RogueAttackingStrategy;
import strategies.RogueDefensiveStrategy;

import java.util.ArrayList;

public final class Rogue extends Hero {
    public Rogue(final Position position, final int id) {
        this.id = id;
        this.position = position;
        this.hp = Constants.ROGUE_HP;
        this.level = 0;
        this.abilities = new ArrayList<>();
        abilities.add(new Backstab());
        abilities.add(new Paralysis());
        this.addObserver(Magician.getInstance());
    }

    @Override
    public char getHeroType() {
        return 'R';
    }

    @Override
    public int getMaxHP() {
        return Constants.ROGUE_HP + (level * Constants.ROGUE_LEVELUP_HP);
    }

    @Override
    public float getTerrainAmp() {
        if (terrain.equals(TerrainTypes.Woods)) {
            return 1 + Constants.ROGUE_WOODS_BONUS;
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
        if (((float) this.hp / this.getMaxHP()) < Constants.ROGUE_STRATEGY_HP_LOWER_LIMIT) {
            this.strategy = new RogueDefensiveStrategy();
            strategy.adapt(this);
            return;
        }
        if (((float) this.hp / this.getMaxHP()) < Constants.ROGUE_STRATEGY_HP_UPPER_LIMIT) {
            this.strategy = new RogueAttackingStrategy();
            strategy.adapt(this);
        }
    }

    @Override
    public void getBlessed(final Angel angel) {
        angel.bless(this);
    }

    @Override
    public String getHeroName() {
        return "Rogue " + id;
    }
}
