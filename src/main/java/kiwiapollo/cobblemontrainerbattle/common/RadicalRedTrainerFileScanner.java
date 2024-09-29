package kiwiapollo.cobblemontrainerbattle.common;

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

public class RadicalRedTrainerFileScanner {
    public static final String RESOURCE_PATH = "data/cobblemontrainerbattle/radicalred";

    public static Path toTrainerFilePath(String trainerName) throws TrainerNameNotExistException {
        assertExistTrainerName(trainerName);
        return getTrainerFiles().stream().filter(path -> toTrainerName(path).equals(trainerName)).toList().get(0);
    }

    public static String toTrainerName(Path trainerFilePath) {
        return trainerFilePath.getFileName().toString().replace(".json", "");
    }

    public static void assertExistTrainerName(String name) throws TrainerNameNotExistException {
        List<String> trainerNames = getTrainerFiles().stream().map(RadicalRedTrainerFileScanner::toTrainerName).toList();
        if (!trainerNames.contains(name)) {
            throw new TrainerNameNotExistException();
        }
    }

    public static List<Path> getTrainerFiles() {
        try {
            URL resource = CobblemonTrainerBattle.class.getClassLoader().getResource(RESOURCE_PATH);

            Path path = Paths.get(resource.toURI());

            return Files.walk(path).filter(Files::isRegularFile).toList();

        } catch (URISyntaxException | IOException e) {
            return new ArrayList<>();
        }
    }
}
