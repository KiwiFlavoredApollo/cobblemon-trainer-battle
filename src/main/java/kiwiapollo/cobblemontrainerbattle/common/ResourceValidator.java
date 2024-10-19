package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.groupbattle.TrainerGroupProfile;
import net.minecraft.util.Identifier;

import java.io.FileNotFoundException;

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
}
