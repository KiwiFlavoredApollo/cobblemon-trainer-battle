package kiwiapollo.cobblemontrainerbattle.battleparticipant.factory;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.NormalBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.NormalGroupBattleTrainer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class NormalGroupBattleParticipantFactory implements BattleParticipantFactory {
    private final BattleCondition condition;

    public NormalGroupBattleParticipantFactory(BattleCondition condition) {
        this.condition = condition;
    }

    @Override
    public PlayerBattleParticipant createPlayer(ServerPlayerEntity player) {
        return new NormalBattlePlayer(player);
    }

    @Override
    public TrainerBattleParticipant createTrainer(Identifier trainer, ServerPlayerEntity player) {
        return new NormalGroupBattleTrainer(trainer, player, condition);
    }

    public static class Builder implements GroupBattleParticipantFactoryBuilder {
        private BattleCondition condition;

        public Builder() {
            this.condition = new BattleCondition();
        }

        @Override
        public GroupBattleParticipantFactoryBuilder addCondition(BattleCondition condition) {
            this.condition = condition;
            return this;
        }

        @Override
        public BattleParticipantFactory build() {
            return new NormalGroupBattleParticipantFactory(condition);
        }
    }
}
