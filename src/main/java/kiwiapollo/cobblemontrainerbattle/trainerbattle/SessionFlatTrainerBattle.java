package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.FlatBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.FlatBattleTrainer;
import kiwiapollo.cobblemontrainerbattle.session.Session;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SessionFlatTrainerBattle extends SessionTrainerBattle {
    private static final int LEVEL = 100;

    public SessionFlatTrainerBattle(ServerPlayerEntity player, Identifier trainer, Session session) {
        super(new FlatBattlePlayer(player, LEVEL), new FlatBattleTrainer(trainer, player, LEVEL), session);
    }
}
