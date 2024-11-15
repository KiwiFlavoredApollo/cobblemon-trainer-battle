package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerGroupProfileStorage;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerProfileStorage;
import net.minecraft.util.Identifier;

import java.io.FileNotFoundException;

public class ResourceValidator {
    public static void assertTrainerExist(Identifier identifier) throws FileNotFoundException {
        if (!TrainerProfileStorage.getProfileRegistry().containsKey(identifier)) {
            throw new FileNotFoundException();
        }
    }

    public static void assertTrainerGroupExist(Identifier identifier) throws FileNotFoundException {
        if (!TrainerGroupProfileStorage.getProfileRegistry().containsKey(identifier)) {
            throw new FileNotFoundException();
        }
    }
}
