package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.parser.ProfileRegistry;
import net.minecraft.util.Identifier;

import java.io.FileNotFoundException;

public class ResourceValidator {
    public static void assertTrainerExist(Identifier identifier) throws FileNotFoundException {
        if (!ProfileRegistry.trainer.containsKey(identifier)) {
            throw new FileNotFoundException();
        }
    }

    public static void assertTrainerGroupExist(Identifier identifier) throws FileNotFoundException {
        if (!ProfileRegistry.trainerGroup.containsKey(identifier)) {
            throw new FileNotFoundException();
        }
    }
}
