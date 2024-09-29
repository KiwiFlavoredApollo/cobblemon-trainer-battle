package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTrainerFactory {
    public Trainer create() {
        return new TrainerFileParser().parse(getRandomTrainerFile());
    }

    private Path getRandomTrainerFile() {
        List<Path> trainerFiles = getTrainerFiles();
        int random = new Random().nextInt(trainerFiles.size() - 1);
        return trainerFiles.get(random);
    }

    private List<Path> getTrainerFiles() {
        try {
            String resourcePath = "data/cobblemontrainerbattle/radicalred";
            URL resource = CobblemonTrainerBattle.class.getClassLoader().getResource(resourcePath);

            Path path = Paths.get(resource.toURI());

            return Files.walk(path).filter(Files::isRegularFile).toList();

        } catch (URISyntaxException | IOException e) {
            return new ArrayList<>();
        }
    }
}
