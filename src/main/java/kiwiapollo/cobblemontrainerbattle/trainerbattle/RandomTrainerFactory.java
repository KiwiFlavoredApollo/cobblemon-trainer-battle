package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomTrainerFactory {
    public Trainer create(ServerPlayerEntity player) {
        List<String> trainerFilePaths = new ArrayList<>(
                CobblemonTrainerBattle.trainerFiles.keySet().stream().toList());
        Collections.shuffle(trainerFilePaths);

        return new Trainer(trainerFilePaths.get(0), new TrainerFileParser(player)
                .parse(CobblemonTrainerBattle.trainerFiles.get(trainerFilePaths.get(0))));
    }

    public Trainer create(ServerPlayerEntity player, String group) {
        List<String> trainerFilePaths = new ArrayList<>(
                CobblemonTrainerBattle.trainerFiles.keySet().stream()
                        .filter(identifier -> identifier.equals(group)).toList());
        Collections.shuffle(trainerFilePaths);

        return new Trainer(trainerFilePaths.get(0), new TrainerFileParser(player)
                .parse(CobblemonTrainerBattle.trainerFiles.get(trainerFilePaths.get(0))));
    }
}
