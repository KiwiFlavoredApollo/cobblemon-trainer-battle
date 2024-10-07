package kiwiapollo.cobblemontrainerbattle.common;

import com.google.gson.JsonElement;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidResourceStateException;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupFile;

public class ResourceValidator {
    public static void assertExistTrainerResource(String trainerResourcePath) throws InvalidResourceStateException {
        if (!CobblemonTrainerBattle.trainerFiles.containsKey(trainerResourcePath)) {
            throw new InvalidResourceStateException(
                    "Unable to find trainer file",
                    InvalidResourceState.NOT_FOUND,
                    trainerResourcePath
            );
        }
    }

    public static void assertValidGroupResource(String groupResourcePath) throws InvalidResourceStateException {
        try {
            GroupFile groupFile = CobblemonTrainerBattle.groupFiles.get(groupResourcePath);
            if (groupFile == null) {
                throw new InvalidResourceStateException(
                        "Unable to find group file",
                        InvalidResourceState.NOT_FOUND,
                        groupResourcePath
                );
            }

            if (groupFile.configuration.get("trainers").getAsJsonArray().isEmpty()) {
                throw new InvalidResourceStateException(
                        "Group has no trainer",
                        InvalidResourceState.CANNOT_BE_READ,
                        groupResourcePath
                );
            }

            if (!groupFile.configuration.get("trainers").getAsJsonArray().asList().stream()
                    .map(JsonElement::getAsString)
                    .allMatch(CobblemonTrainerBattle.trainerFiles::containsKey)) {
                throw new InvalidResourceStateException(
                        "One or more trainers cannot be found",
                        InvalidResourceState.CANNOT_BE_READ,
                        groupResourcePath
                );
            };

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException | ClassCastException e) {
            throw new InvalidResourceStateException(
                    "Unable to read group file",
                    InvalidResourceState.CANNOT_BE_READ,
                    groupResourcePath
            );
        }
    }
}
