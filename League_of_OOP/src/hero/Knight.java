package hero;

import abilities.Ability;
import abilities.Execute;
import abilities.Slam;
import angel.Angel;
import common.Constants;
import magician.Magician;
import map.TerrainTypes;
import strategies.KnightAttackingStrategy;
import strategies.KnightDefensiveStrategy;

import java.util.ArrayList;

public final class Knight extends Hero {
    public Knight(final Position position, final int id) {
        this.id = id;
        this.position = position;
        this.hp = Constants.KNIGHT_HP;
        this.level = 0;
        this.abilities = new ArrayList<>();
        abilities.add(new Execute());
        abilities.add(new Slam());
        this.addObserver(Magician.getInstance());
    }

    @Override
    public char getHeroType() {
        return 'K';
    }

    @Override
    public int getMaxHP() {
        return Constants.KNIGHT_HP + (Constants.KNIGHT_LEVELUP_HP * this.level);
    }

    @Override
    public float getTerrainAmp() {
        if (terrain.equals(TerrainTypes.Land)) {
            return 1 + Constants.KNIGHT_LAND_BONUS;
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
        if (((float) this.hp / this.getMaxHP()) < Constants.KNIGHT_STRATEGY_HP_LOWER_LIMIT) {
            this.strategy = new KnightDefensiveStrategy();
            strategy.adapt(this);
            return;
        }
        if (((float) this.hp / this.getMaxHP()) < Constants.KNIGHT_STRATEGY_HP_UPPER_LIMIT
                && ((float) this.hp / this.getMaxHP()) > Constants.KNIGHT_STRATEGY_HP_LOWER_LIMIT) {
            this.strategy = new KnightAttackingStrategy();
            strategy.adapt(this);
        }
    }

    @Override
    public void getBlessed(final Angel angel) {
        angel.bless(this);
    }

    @Override
    public String getHeroName() {
        return "Knight " + id;
    }
}
