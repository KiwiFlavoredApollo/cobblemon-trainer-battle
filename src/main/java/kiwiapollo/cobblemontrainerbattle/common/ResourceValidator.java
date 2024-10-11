package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.io.FileNotFoundException;
import java.util.Objects;

public class ResourceValidator {
    public void assertExistResource(String resourcePath) throws FileNotFoundException {
        try {
            new Identifier(CobblemonTrainerBattle.NAMESPACE, resourcePath);

        } catch (InvalidIdentifierException e) {
            throw new FileNotFoundException();
        }
    }

    public void assertValidGroupResource(String groupResourcePath) throws IllegalArgumentException {
        Identifier identifier = new Identifier(CobblemonTrainerBattle.NAMESPACE, groupResourcePath);
        TrainerGroup trainerGroup = CobblemonTrainerBattle.trainerGroups.get(identifier);

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
