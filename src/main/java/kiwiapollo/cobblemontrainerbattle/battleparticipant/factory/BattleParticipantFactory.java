package kiwiapollo.cobblemontrainerbattle.battleparticipant.factory;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public interface BattleParticipantFactory {
    PlayerBattleParticipant createPlayer(ServerPlayerEntity player);
    TrainerBattleParticipant createTrainer(Identifier trainer, ServerPlayerEntity player, BattleCondition condition);
}
