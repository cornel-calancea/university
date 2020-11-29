package angel;

import hero.Position;

public final class AngelFactory {
    /*class to generate angels*/
    private static AngelFactory factory;

    private AngelFactory() {
    }

    public static AngelFactory getInstance() {
        if (factory == null) {
            factory = new AngelFactory();
        }
        return factory;
    }

    public Angel getAngel(final String type, final Position position) {
        switch (type) {
            case "DamageAngel":
                return new DamageAngel(position);
            case "DarkAngel":
                return new DarkAngel(position);
            case "Dracula":
                return new Dracula(position);
            case "GoodBoy":
                return new GoodBoy(position);
            case "LevelUpAngel":
                return new LevelUpAngel(position);
            case "LifeGiver":
                return new LifeGiver(position);
            case "SmallAngel":
                return new SmallAngel(position);
            case "Spawner":
                return new Spawner(position);
            case "TheDoomer":
                return new Doomer(position);
            case "XPAngel":
                return new XPAngel(position);
            default:
        }
        return null;
    }
}
