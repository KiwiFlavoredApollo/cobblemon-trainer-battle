package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.TrainerIdentifierNotExistException;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SpecificTrainerFactory {
    public Trainer create(ServerPlayerEntity player, Identifier identifier) throws TrainerIdentifierNotExistException {
        if (!CobblemonTrainerBattle.trainerFiles.containsKey(identifier)) {
            throw new TrainerIdentifierNotExistException();
        }

        TrainerFile trainerFile = CobblemonTrainerBattle.trainerFiles.get(identifier);
        return new Trainer(identifier.toString(), new TrainerFileParser(player).parse(trainerFile));
    }
}
