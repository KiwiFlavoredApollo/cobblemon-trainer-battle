package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.standalone;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.FlatBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.FlatBattleTrainer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class StandaloneFlatTrainerBattle extends StandaloneTrainerBattle {
    private static final int LEVEL = 100;

    public StandaloneFlatTrainerBattle(ServerPlayerEntity player, Identifier trainer) {
        super(new FlatBattlePlayer(player, LEVEL), new FlatBattleTrainer(trainer, player, LEVEL));
    }
}
