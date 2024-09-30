package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.exceptions.TrainerNameNotExistException;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class NameTrainerFactory {
    private final List<TrainerFile> trainerFiles;

    public NameTrainerFactory(List<TrainerFile> trainerFiles) {
        this.trainerFiles = trainerFiles;
    }

    public Trainer create(ServerPlayerEntity player, String name) throws TrainerNameNotExistException {
        List<String> trainerNames = trainerFiles.stream()
                .map(TrainerFileHelper::toTrainerName).toList();
        if (!trainerNames.contains(name)) {
            throw new TrainerNameNotExistException();
        }

        TrainerFile trainer = trainerFiles.stream()
                .filter(trainerFile -> TrainerFileHelper.toTrainerName(trainerFile).equals(name))
                .findFirst().get();
        return new TrainerFileParser(player).parse(trainer);
    }
}
