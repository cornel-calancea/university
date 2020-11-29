package input;

import angel.Angel;
import angel.AngelFactory;
import fileio.FileSystem;
import hero.Position;
import hero.TypePositionPair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public final class GameIOLoader {
  private final String mInputPath;
  private final String mOutputPath;

  public GameIOLoader(final String inputPath, final String outputPath) {
    mInputPath = inputPath;
    mOutputPath = outputPath;
  }

  public void loadOutput(final String results) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(mOutputPath));
    writer.write(results);
    writer.close();
  }

  public GameInput loadInput() {
    int rows = 0;
    int columns = 0;
    ArrayList<ArrayList<Character>> terrain = new ArrayList<>();
    int nrOfHeroes = 0;
    ArrayList<TypePositionPair> characterPositionList = new ArrayList<>();
    int nrOfRounds = 0;
    ArrayList<String> moves = new ArrayList<>();
    ArrayList<ArrayList<Angel>> angels = new ArrayList<>();
    AngelFactory angelFactory = AngelFactory.getInstance();

    try {
      FileSystem fs = new FileSystem(mInputPath, mOutputPath);

      rows = fs.nextInt();
      columns = fs.nextInt();
      characterPositionList = new ArrayList<>();

      for (int i = 0; i < rows; ++i) {
        terrain.add(new ArrayList<>());
        char[] row = fs.nextWord().toCharArray();
        for (int j = 0; j < columns; j++) {
          terrain.get(i).add(row[j]);
        }
      }

      nrOfHeroes = fs.nextInt();
      characterPositionList = new ArrayList<>();
      for (int i = 0; i < nrOfHeroes; i++) {
        TypePositionPair newCharacter =
            new TypePositionPair(fs.nextWord().charAt(0), new Position(fs.nextInt(), fs.nextInt()));
        characterPositionList.add(newCharacter);
      }

      nrOfRounds = fs.nextInt();
      for (int i = 0; i < nrOfRounds; i++) {
        moves.add(fs.nextWord());
      }

      for (int i = 0; i < nrOfRounds; i++) {
        int nrOfAngels = fs.nextInt();
        angels.add(new ArrayList<>());
        for (int j = 0; j < nrOfAngels; j++) {
          String[] input = fs.nextWord().split(",");
          Position position = new Position(Integer.parseInt(input[1]), Integer.parseInt(input[2]));
          angels.get(i).add(angelFactory.getAngel(input[0], position));
        }
      }
      fs.close();

    } catch (Exception e1) {
      e1.printStackTrace();
    }

    return new GameInput(
        rows, columns, terrain, nrOfHeroes, characterPositionList, nrOfRounds, moves, angels);
  }
}
