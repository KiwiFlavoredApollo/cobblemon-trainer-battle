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
    private final BattleCondition condition;

    public FlatGroupBattleParticipantFactory(int level, BattleCondition condition) {
        this.level = level;
        this.condition = condition;
    }

    @Override
    public PlayerBattleParticipant createPlayer(ServerPlayerEntity player) {
        return new FlatBattlePlayer(player, level);
    }

    @Override
    public TrainerBattleParticipant createTrainer(Identifier trainer, ServerPlayerEntity player) {
        return new FlatGroupBattleTrainer(trainer, player, condition, level);
    }

    public static class Builder implements GroupBattleParticipantFactoryBuilder {
        private final int level;

        private BattleCondition condition;

        public Builder(int level) {
            this.level = level;
            this.condition = new BattleCondition();
        }

        @Override
        public GroupBattleParticipantFactoryBuilder addCondition(BattleCondition condition) {
            this.condition = condition;
            return this;
        }

        @Override
        public BattleParticipantFactory build() {
            return new FlatGroupBattleParticipantFactory(level, condition);
        }
    }
}
