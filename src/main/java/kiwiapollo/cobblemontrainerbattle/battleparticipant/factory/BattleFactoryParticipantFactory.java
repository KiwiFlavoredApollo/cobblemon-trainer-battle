package kiwiapollo.cobblemontrainerbattle.battleparticipant.factory;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.BattleFactoryPlayer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.BattleFactoryTrainer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.TrainerBattleParticipant;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class BattleFactoryParticipantFactory implements BattleParticipantFactory {
    private final int level;

    public BattleFactoryParticipantFactory(int level) {
        this.level = level;
    }

    @Override
    public PlayerBattleParticipant createPlayer(ServerPlayerEntity player) {
        return new BattleFactoryPlayer(player, level);
    }

    @Override
    public TrainerBattleParticipant createTrainer(Identifier trainer, ServerPlayerEntity player) {
        return new BattleFactoryTrainer(trainer, player, level);
    }
}
