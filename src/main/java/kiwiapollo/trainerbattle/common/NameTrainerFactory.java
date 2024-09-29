package kiwiapollo.trainerbattle.common;

import kiwiapollo.trainerbattle.TrainerBattle;
import kiwiapollo.trainerbattle.exceptions.TrainerNameNotExistException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class NameTrainerFactory {
    public Trainer create(String name) throws TrainerNameNotExistException {
        assertExistTrainerName(name);
        return new TrainerFileParser().parse(toTrainerFilePath(name));
    }

    private Path toTrainerFilePath(String trainerName) {
        return getTrainerFiles().stream().filter(path -> toTrainerName(path).equals(trainerName)).toList().get(0);
    }

    private String toTrainerName(Path trainerFilePath) {
        return trainerFilePath.getFileName().toString().replace(".json", "");
    }

    private void assertExistTrainerName(String name) throws TrainerNameNotExistException {
        List<String> trainerNames = getTrainerFiles().stream().map(this::toTrainerName).toList();
        if (!trainerNames.contains(name)) {
            throw new TrainerNameNotExistException();
        }
    }

    private List<Path> getTrainerFiles() {
        try {
            String resourcePath = "data/trainerbattle/radicalred";
            URL resource = TrainerBattle.class.getClassLoader().getResource(resourcePath);

            Path path = Paths.get(resource.toURI());

            return Files.walk(path).filter(Files::isRegularFile).toList();

        } catch (URISyntaxException | IOException e) {
            return new ArrayList<>();
        }
    }
}
