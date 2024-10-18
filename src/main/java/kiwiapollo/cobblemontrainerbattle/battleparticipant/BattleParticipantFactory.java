package kiwiapollo.cobblemontrainerbattle.battleparticipant;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public interface BattleParticipantFactory {
    PlayerBattleParticipant createPlayer(ServerPlayerEntity player);
    TrainerBattleParticipant createTrainer(Identifier trainer, ServerPlayerEntity player);
}
