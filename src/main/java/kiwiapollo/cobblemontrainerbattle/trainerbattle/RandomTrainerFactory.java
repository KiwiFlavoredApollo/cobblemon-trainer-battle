package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Random;

public class RandomTrainerFactory {
    private final List<TrainerFile> trainerFiles;

    public RandomTrainerFactory(List<TrainerFile> trainerFiles) {
        this.trainerFiles = trainerFiles;
    }

    public Trainer create(ServerPlayerEntity player) {
        int random = new Random().nextInt(trainerFiles.size() - 1);
        return new TrainerFileParser(player).parse(trainerFiles.get(random));
    }
}
