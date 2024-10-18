package kiwiapollo.cobblemontrainerbattle.battleparticipant;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

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
    public TrainerBattleParticipant createTrainer(Identifier trainer, ServerPlayerEntity player) {
        return new FlatBattleTrainer(trainer, player, level);
    }
}
