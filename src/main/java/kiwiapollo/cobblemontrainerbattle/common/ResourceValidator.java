package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.util.Identifier;

import java.io.FileNotFoundException;
import java.util.Objects;

public class ResourceValidator {
    public static void assertTrainerExist(Identifier identifier) throws FileNotFoundException {
        if (!CobblemonTrainerBattle.trainerRegistry.containsKey(identifier)) {
            throw new FileNotFoundException();
        }
    }

    public static void assertTrainerGroupExist(Identifier identifier) throws FileNotFoundException {
        if (!CobblemonTrainerBattle.trainerGroupRegistry.containsKey(identifier)) {
            throw new FileNotFoundException();
        }
    }

    public static void assertTrainerGroupValid(Identifier identifier) throws IllegalArgumentException {
        TrainerGroup trainerGroup = CobblemonTrainerBattle.trainerGroupRegistry.get(identifier);

        if (trainerGroup.trainers.isEmpty()) {
            throw new IllegalArgumentException();
        }

        boolean isExistAllTrainers = trainerGroup.trainers.stream()
                .map(resourcePath -> Identifier.of(CobblemonTrainerBattle.NAMESPACE, resourcePath))
                .allMatch(Objects::nonNull);
        if (!isExistAllTrainers) {
            throw new IllegalArgumentException();
        }
    }
}
