package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.exceptions.TrainerNameNotExistException;
import net.minecraft.server.network.ServerPlayerEntity;

public class RadicalRedNameTrainerFactory {
    TrainerFileScanner trainerFileScanner;

    public RadicalRedNameTrainerFactory() {
        this.trainerFileScanner = new RadicalRedTrainerFileScanner();
    }

    public Trainer create(ServerPlayerEntity player, String name) throws TrainerNameNotExistException {
        trainerFileScanner.assertExistTrainerName(name);
        return new TrainerFileParser(player).parse(trainerFileScanner.toTrainerFilePath(name));
    }
}
