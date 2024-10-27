package kiwiapollo.cobblemontrainerbattle.postbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.parser.PlayerHistory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class RecordedBattleResultHandler implements BattleResultHandler {
    private final PlayerHistory history;
    private final Identifier trainer;

    public RecordedBattleResultHandler(ServerPlayerEntity player, Identifier trainer) {
        this.history = CobblemonTrainerBattle.playerHistoryRegistry.get(player.getUuid());
        this.trainer = trainer;
    }

    @Override
    public void onVictory() {
        history.addPlayerVictory(trainer);
    }

    @Override
    public void onDefeat() {
        history.addPlayerDefeat(trainer);
    }
}
