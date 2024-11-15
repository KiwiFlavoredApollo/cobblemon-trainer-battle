package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.NormalBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.NormalBattleTrainer;
import kiwiapollo.cobblemontrainerbattle.session.Session;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SessionNormalTrainerBattle extends SessionTrainerBattle {
    public SessionNormalTrainerBattle(ServerPlayerEntity player, Identifier trainer, Session session) {
        super(new NormalBattlePlayer(player), new NormalBattleTrainer(trainer, player), session);
    }
}
