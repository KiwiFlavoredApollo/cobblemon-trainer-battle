package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidResourceStateException;

public class ResourceStateChecker {
    public static void assertExistTrainerResource(String trainerResourcePath) throws InvalidResourceStateException {
        if (!CobblemonTrainerBattle.trainerFiles.containsKey(trainerResourcePath)) {
            throw new InvalidResourceStateException(
                    String.format("Trainer file is not loaded: %s", trainerResourcePath),
                    InvalidResourceState.NOT_FOUND,
                    trainerResourcePath
            );
        }
    }
}
