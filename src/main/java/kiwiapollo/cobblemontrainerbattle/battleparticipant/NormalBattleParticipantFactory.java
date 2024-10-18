package kiwiapollo.cobblemontrainerbattle.battleparticipant;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class NormalBattleParticipantFactory implements BattleParticipantFactory {
    @Override
    public PlayerBattleParticipant createPlayer(ServerPlayerEntity player) {
        return new NormalBattlePlayer(player);
    }

    @Override
    public TrainerBattleParticipant createTrainer(Identifier trainer, ServerPlayerEntity player) {
        return new VirtualNormalBattleTrainer(trainer, player);
    }
}
