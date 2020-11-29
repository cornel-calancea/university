package hero;

public final class TypePositionPair {
    private char heroType;
    private Position position;

    public TypePositionPair(final char heroType, final Position position) {
        this.heroType = heroType;
        this.position = position;
    }

    public char getHeroType() {
        return heroType;
    }

    public Position getPosition() {
        return position;
    }
}
