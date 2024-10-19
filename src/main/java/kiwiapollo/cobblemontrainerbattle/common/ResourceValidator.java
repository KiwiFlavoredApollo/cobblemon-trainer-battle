package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.groupbattle.TrainerGroupProfile;
import net.minecraft.util.Identifier;

import java.io.FileNotFoundException;

public class ResourceValidator {
    public static void assertTrainerExist(Identifier identifier) throws FileNotFoundException {
        if (!isTrainerExist(identifier)) {
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
                .map(Identifier::new)
                .allMatch(ResourceValidator::isTrainerExist);
        if (!isExistAllTrainers) {
            throw new IllegalArgumentException();
        }
    }

    private static boolean isTrainerExist(Identifier identifier) {
        return CobblemonTrainerBattle.trainerProfileRegistry.containsKey(identifier);
    }
}
