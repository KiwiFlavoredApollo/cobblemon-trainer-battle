package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidResourceStateException;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.util.Objects;

public class ResourceValidator {
    public void assertExistValidTrainerResource(String trainerResourcePath) throws InvalidResourceStateException {
        try {
            new Identifier(CobblemonTrainerBattle.NAMESPACE, trainerResourcePath);

        } catch (InvalidIdentifierException e) {
            throw new InvalidResourceStateException(
                    "Unable to find trainer file",
                    InvalidResourceState.NOT_FOUND,
                    trainerResourcePath
            );
        }
    }

    public void assertExistValidGroupResource(String groupResourcePath) throws InvalidResourceStateException {
        try {
            Identifier identifier = new Identifier(CobblemonTrainerBattle.NAMESPACE, groupResourcePath);
            TrainerGroup trainerGroup = CobblemonTrainerBattle.trainerGroups.get(identifier);

            if (trainerGroup.trainers.isEmpty()) {
                throw new InvalidResourceStateException(
                        "Group has no trainer",
                        InvalidResourceState.CANNOT_BE_READ,
                        groupResourcePath
                );
            }

            boolean isExistAllTrainers = trainerGroup.trainers.stream()
                    .map(resourcePath -> Identifier.of(CobblemonTrainerBattle.NAMESPACE, resourcePath))
                    .allMatch(Objects::nonNull);
            if (!isExistAllTrainers) {
                throw new InvalidResourceStateException(
                        "One or more trainers cannot be found",
                        InvalidResourceState.CANNOT_BE_READ,
                        groupResourcePath
                );
            };

        } catch (InvalidIdentifierException | NullPointerException | ClassCastException e) {
            throw new InvalidResourceStateException(
                    "Unable to read group file",
                    InvalidResourceState.CANNOT_BE_READ,
                    groupResourcePath
            );
        }
    }
}
