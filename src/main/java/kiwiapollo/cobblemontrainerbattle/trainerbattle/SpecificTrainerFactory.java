package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.InvalidResourceState;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidResourceStateException;
import net.minecraft.server.network.ServerPlayerEntity;

public class SpecificTrainerFactory {
    public Trainer create(ServerPlayerEntity player, String resourcePath) throws InvalidResourceStateException {
        if (!CobblemonTrainerBattle.trainerFiles.containsKey(resourcePath)) {
            throw new InvalidResourceStateException(
                    String.format("Trainer file is not loaded: %s", resourcePath),
                    InvalidResourceState.UNREADABLE,
                    resourcePath
            );
        }

        TrainerFile trainerFile = CobblemonTrainerBattle.trainerFiles.get(resourcePath);
        return new Trainer(resourcePath, new TrainerFileParser(player).parse(trainerFile));
    }
}
