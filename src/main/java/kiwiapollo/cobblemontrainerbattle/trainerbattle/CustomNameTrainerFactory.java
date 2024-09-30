package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.TrainerNameNotExistException;
import net.minecraft.server.network.ServerPlayerEntity;

public class CustomNameTrainerFactory extends NameTrainerFactory {
    public CustomNameTrainerFactory() {
        super(CobblemonTrainerBattle.CUSTOM_TRAINERS);
    }
}
