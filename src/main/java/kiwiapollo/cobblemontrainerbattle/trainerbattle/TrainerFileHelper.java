package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.util.Identifier;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class TrainerFileHelper {
    public static String toTrainerName(Identifier identifier) {
        return Paths.get(identifier.getPath()).getFileName()
                .toString().replace(".json", "");
    }

    public static List<String> getTrainerNames() {
        return CobblemonTrainerBattle.trainerFiles.entrySet().stream()
                .map(Map.Entry::getKey)
                .map(TrainerFileHelper::toTrainerName)
                .toList();
    }
}
