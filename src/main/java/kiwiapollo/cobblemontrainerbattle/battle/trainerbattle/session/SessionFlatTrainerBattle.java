package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.session;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.FlatBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.FlatBattleTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.session.Session;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SessionFlatTrainerBattle extends SessionTrainerBattle {
    private static final int LEVEL = 100;

    public SessionFlatTrainerBattle(ServerPlayerEntity player, Identifier trainer, Session session) {
        super(new FlatBattlePlayer(player, LEVEL), new FlatBattleTrainer(trainer, player, LEVEL), session);
    }
}
