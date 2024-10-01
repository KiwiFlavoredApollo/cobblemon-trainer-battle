package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.CreateTrainerFailedException;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;

public class RandomTrainerFactory {
    public Trainer create(ServerPlayerEntity player) {
        List<Identifier> identifiers = new ArrayList<>(
                CobblemonTrainerBattle.trainerFiles.keySet().stream().toList());
        Collections.shuffle(identifiers);

        return new Trainer(identifiers.get(0).toString(), new TrainerFileParser(player)
                .parse(CobblemonTrainerBattle.trainerFiles.get(identifiers.get(0))));
    }

    public Trainer create(ServerPlayerEntity player, String group) {
        List<Identifier> identifiers = new ArrayList<>(
                CobblemonTrainerBattle.trainerFiles.keySet().stream()
                .filter(identifier -> identifier.getNamespace().equals(group)).toList());
        Collections.shuffle(identifiers);

        return new Trainer(identifiers.get(0).toString(), new TrainerFileParser(player)
                .parse(CobblemonTrainerBattle.trainerFiles.get(identifiers.get(0))));
    }
}
