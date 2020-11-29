package magician;

import java.util.Observable;
import java.util.Observer;

public final class Magician implements Observer {
  /*The Great Mage - receives notifications when something happens in the game*/
  private static Magician instance;
  private static StringBuilder report = new StringBuilder();

  private Magician() {
  }

  public static Magician getInstance() {
    if (instance == null) {
      instance = new Magician();
    }
    return instance;
  }

  @Override
  public void update(final Observable o, final Object arg) {
    report.append(arg).append(System.lineSeparator());
  }

  public static String getReport() {
    return report.toString();
  }
}
