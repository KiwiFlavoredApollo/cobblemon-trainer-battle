package kiwiapollo.cobblemontrainerbattle.battleparticipant.factory;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.FlatBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.FlatGroupBattleTrainer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class FlatGroupBattleParticipantFactory implements BattleParticipantFactory {
    private final int level;

    public FlatGroupBattleParticipantFactory(int level) {
        this.level = level;
    }

    @Override
    public PlayerBattleParticipant createPlayer(ServerPlayerEntity player) {
        return new FlatBattlePlayer(player, level);
    }

    @Override
    public TrainerBattleParticipant createTrainer(Identifier trainer, ServerPlayerEntity player, BattleCondition condition) {
        return new FlatGroupBattleTrainer(trainer, player, condition, level);
    }
}
