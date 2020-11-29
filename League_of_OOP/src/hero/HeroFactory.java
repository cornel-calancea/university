package hero;

import java.util.ArrayList;

public final class HeroFactory {
    /* class to create Heroes of all types*/
    private static HeroFactory factory;
    private int playerId = -1;

    private HeroFactory() {
    }

    public static HeroFactory getInstance() {
        if (factory == null) {
            factory = new HeroFactory();
        }
        return factory;
    }

    private Hero getHero(final Character type, final Position position) {
        playerId++;
        switch (type) {
            case 'K':
                return new Knight(position, playerId);
            case 'R':
                return new Rogue(position, playerId);
            case 'W':
                return new Wizard(position, playerId);
            case 'P':
                return new Pyromancer(position, playerId);
            default:
        }
        return null;
    }

    public ArrayList<Hero> getHeroesList(final ArrayList<TypePositionPair> list) {
        ArrayList<Hero> heroList = new ArrayList<>();
        for (TypePositionPair entry : list) {
            heroList.add(getHero(entry.getHeroType(), entry.getPosition()));
        }
        return heroList;
    }
}
