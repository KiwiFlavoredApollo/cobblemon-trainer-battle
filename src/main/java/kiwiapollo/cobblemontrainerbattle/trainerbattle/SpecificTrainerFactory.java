package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.InvalidResourceState;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidResourceStateException;
import net.minecraft.server.network.ServerPlayerEntity;

public class SpecificTrainerFactory {
    public Trainer create(ServerPlayerEntity player, String resourcePath) {
        TrainerFile trainerFile = CobblemonTrainerBattle.trainerFiles.get(resourcePath);
        return new Trainer(resourcePath, new TrainerFileParser(player).parse(trainerFile));
    }
}
