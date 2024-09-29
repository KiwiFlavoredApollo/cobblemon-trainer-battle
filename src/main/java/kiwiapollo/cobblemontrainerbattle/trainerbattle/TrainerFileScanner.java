package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.TrainerNameNotExistException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TrainerFileScanner {
    private final String resourcePath;

    public TrainerFileScanner(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public Path toTrainerFilePath(String trainerName) throws TrainerNameNotExistException {
        assertExistTrainerName(trainerName);
        return getTrainerFiles().stream().filter(path -> toTrainerName(path).equals(trainerName)).toList().get(0);
    }

    public static String toTrainerName(Path trainerFilePath) {
        return trainerFilePath.getFileName().toString().replace(".json", "");
    }

    public void assertExistTrainerName(String name) throws TrainerNameNotExistException {
        List<String> trainerNames = getTrainerFiles().stream().map(TrainerFileScanner::toTrainerName).toList();
        if (!trainerNames.contains(name)) {
            throw new TrainerNameNotExistException();
        }
    }

    public List<Path> getTrainerFiles() {
        try {
            URL resource = CobblemonTrainerBattle.class.getClassLoader().getResource(this.resourcePath);

            Path path = Paths.get(resource.toURI());

            return Files.walk(path).filter(Files::isRegularFile).toList();

        } catch (URISyntaxException | IOException e) {
            return new ArrayList<>();
        }
    }
}
