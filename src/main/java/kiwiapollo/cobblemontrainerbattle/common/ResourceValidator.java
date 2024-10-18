package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.util.Identifier;

import java.io.FileNotFoundException;
import java.util.Objects;

public class ResourceValidator {
    public static void assertTrainerExist(Identifier identifier) throws FileNotFoundException {
        if (!CobblemonTrainerBattle.trainerProfileRegistry.containsKey(identifier)) {
            throw new FileNotFoundException();
        }
    }

    public static void assertTrainerGroupExist(Identifier identifier) throws FileNotFoundException {
        if (!CobblemonTrainerBattle.trainerGroupProfileRegistry.containsKey(identifier)) {
            throw new FileNotFoundException();
        }
    }

    public static void assertTrainerGroupValid(Identifier identifier) throws IllegalArgumentException {
        TrainerGroupProfile trainerGroupProfile = CobblemonTrainerBattle.trainerGroupProfileRegistry.get(identifier);

        if (trainerGroupProfile.trainers.isEmpty()) {
            throw new IllegalArgumentException();
        }

        boolean isExistAllTrainers = trainerGroupProfile.trainers.stream()
                .map(resourcePath -> Identifier.of(CobblemonTrainerBattle.NAMESPACE, resourcePath))
                .allMatch(Objects::nonNull);
        if (!isExistAllTrainers) {
            throw new IllegalArgumentException();
        }
    }
}
