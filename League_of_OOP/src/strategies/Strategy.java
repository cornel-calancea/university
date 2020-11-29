package strategies;

import hero.Hero;

public interface Strategy {
  /*Every particular strategy should implement this interface and adapt its hero accordingly*/
  void adapt(Hero h);
}
