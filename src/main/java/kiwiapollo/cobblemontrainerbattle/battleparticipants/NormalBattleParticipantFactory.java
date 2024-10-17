package kiwiapollo.cobblemontrainerbattle.battleparticipants;

import kiwiapollo.cobblemontrainerbattle.common.Trainer;
import net.minecraft.server.network.ServerPlayerEntity;

public class NormalBattleParticipantFactory implements BattleParticipantFactory {
    @Override
    public PlayerBattleParticipant createPlayer(ServerPlayerEntity player) {
        return new NormalBattlePlayer(player);
    }

    @Override
    public TrainerBattleParticipant createTrainer(Trainer trainer, ServerPlayerEntity player) {
        return new NormalBattleTrainer(trainer, player);
    }
}
