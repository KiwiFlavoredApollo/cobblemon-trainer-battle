package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.NoSuchElementException;

public class SpecificTrainerFactory {
    public Trainer create(Identifier identifier) {
        return CobblemonTrainerBattle.trainers.get(identifier);
    }
}
