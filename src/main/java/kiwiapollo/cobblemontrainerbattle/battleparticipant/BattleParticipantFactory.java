package kiwiapollo.cobblemontrainerbattle.battleparticipant;

import kiwiapollo.cobblemontrainerbattle.common.Trainer;
import net.minecraft.server.network.ServerPlayerEntity;

public interface BattleParticipantFactory {
    PlayerBattleParticipant createPlayer(ServerPlayerEntity player);
    TrainerBattleParticipant createTrainer(Trainer trainer, ServerPlayerEntity player);
}
