package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.CreateTrainerFailedException;
import net.minecraft.server.network.ServerPlayerEntity;

public class SpecificTrainerFactory {
    public Trainer create(ServerPlayerEntity player, String resourcePath) throws CreateTrainerFailedException {
        if (!CobblemonTrainerBattle.trainerFiles.containsKey(resourcePath)) {
            throw new CreateTrainerFailedException(String.format("Trainer file is not loaded: %s", resourcePath));
        }

        TrainerFile trainerFile = CobblemonTrainerBattle.trainerFiles.get(resourcePath);
        return new Trainer(resourcePath, new TrainerFileParser(player).parse(trainerFile));
    }
}
