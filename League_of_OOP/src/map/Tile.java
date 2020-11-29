package map;

import hero.Hero;

import java.util.ArrayList;

public final class Tile {
  /*A square of the map, defined by the terrain types and heroes that stand there*/
  private TerrainTypes terrainType;
  private ArrayList<Hero> heroes;

  public Tile(final Character terrainType) {
    switch (terrainType) {
      case 'V':
        this.terrainType = TerrainTypes.Volcanic;
        break;
      case 'W':
        this.terrainType = TerrainTypes.Woods;
        break;
      case 'L':
        this.terrainType = TerrainTypes.Land;
        break;
      case 'D':
        this.terrainType = TerrainTypes.Desert;
      default:
    }
    heroes = new ArrayList<>();
  }

  public TerrainTypes getTerrainType() {
    return terrainType;
  }

  public ArrayList<Hero> getHeroes() {
    return heroes;
  }
}
