package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.exceptions.TrainerNameNotExistException;
import net.minecraft.server.network.ServerPlayerEntity;

public class CustomNameTrainerFactory {
    TrainerFileScanner trainerFileScanner;

    public CustomNameTrainerFactory() {
        this.trainerFileScanner = new CustomTrainerFileScanner();
    }

    public Trainer create(ServerPlayerEntity player, String name) throws TrainerNameNotExistException {
        trainerFileScanner.assertExistTrainerName(name);
        return new TrainerFileParser(player).parse(trainerFileScanner.toTrainerFilePath(name));
    }
}
