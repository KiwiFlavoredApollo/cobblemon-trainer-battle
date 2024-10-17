package kiwiapollo.cobblemontrainerbattle.battleparticipant;

import kiwiapollo.cobblemontrainerbattle.common.Trainer;
import net.minecraft.server.network.ServerPlayerEntity;

public class FlatBattleParticipantFactory implements BattleParticipantFactory {
    private final int level;

    public FlatBattleParticipantFactory(int level) {
        this.level = level;
    }

    @Override
    public PlayerBattleParticipant createPlayer(ServerPlayerEntity player) {
        return new FlatBattlePlayer(player, level);
    }

    @Override
    public TrainerBattleParticipant createTrainer(Trainer trainer, ServerPlayerEntity player) {
        return new FlatBattleTrainer(trainer, player, level);
    }
}
