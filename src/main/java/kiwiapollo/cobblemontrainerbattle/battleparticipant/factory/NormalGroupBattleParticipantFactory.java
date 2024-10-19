package kiwiapollo.cobblemontrainerbattle.battleparticipant.factory;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.NormalBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.NormalGroupBattleTrainer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class NormalGroupBattleParticipantFactory implements BattleParticipantFactory {
    @Override
    public PlayerBattleParticipant createPlayer(ServerPlayerEntity player) {
        return new NormalBattlePlayer(player);
    }

    @Override
    public TrainerBattleParticipant createTrainer(Identifier trainer, ServerPlayerEntity player, BattleCondition condition) {
        return new NormalGroupBattleTrainer(trainer, player, condition);
    }
}
