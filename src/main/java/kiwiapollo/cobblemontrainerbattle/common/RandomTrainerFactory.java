package kiwiapollo.cobblemontrainerbattle.common;

import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class RandomTrainerFactory {
    public Trainer create() {
        return new TrainerFileParser().parse(getRandomTrainerFile());
    }

    private Path getRandomTrainerFile() {
        List<Path> trainerFiles = RadicalRedTrainerFileScanner.getTrainerFiles();
        int random = new Random().nextInt(trainerFiles.size() - 1);
        return trainerFiles.get(random);
    }
}
