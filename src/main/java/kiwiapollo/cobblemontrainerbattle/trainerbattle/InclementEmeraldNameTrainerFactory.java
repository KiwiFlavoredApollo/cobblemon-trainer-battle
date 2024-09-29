package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.exceptions.TrainerNameNotExistException;
import net.minecraft.server.network.ServerPlayerEntity;

public class InclementEmeraldNameTrainerFactory {
    TrainerFileScanner trainerFileScanner;

    public InclementEmeraldNameTrainerFactory() {
        this.trainerFileScanner = new InclementEmeraldTrainerFileScanner();
    }

    public Trainer create(ServerPlayerEntity player, String name) throws TrainerNameNotExistException {
        trainerFileScanner.assertExistTrainerName(name);
        return new TrainerFileParser(player).parse(trainerFileScanner.toTrainerFilePath(name));
    }
}
