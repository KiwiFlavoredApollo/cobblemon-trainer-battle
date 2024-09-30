package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import java.nio.file.Paths;

public class TrainerFileHelper {
    public static String toTrainerName(TrainerFile trainerFile) {
        return Paths.get(trainerFile.identifier.getPath()).getFileName()
                .toString().replace(".json", "");
    }
}
