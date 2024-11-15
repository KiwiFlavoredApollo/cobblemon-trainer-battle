package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.FlatBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.FlatBattleTrainer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class StandaloneFlatTrainerBattle extends StandaloneTrainerBattle {
    private static final int LEVEL = 100;

    public StandaloneFlatTrainerBattle(ServerPlayerEntity player, Identifier trainer) {
        super(new FlatBattlePlayer(player, LEVEL), new FlatBattleTrainer(trainer, player, LEVEL));
    }
}
