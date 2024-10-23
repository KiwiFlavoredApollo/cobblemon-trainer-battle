package kiwiapollo.cobblemontrainerbattle.postbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.parser.PlayerHistory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class RecordedBattleResultHandler implements BattleResultHandler {
    private final ServerPlayerEntity player;
    private final Identifier identifier;
    private final BattleResultHandler battleResultHandler;

    public RecordedBattleResultHandler(
            ServerPlayerEntity player,
            Identifier identifier,
            PostBattleActionSet victory,
            PostBattleActionSet defeat
    ) {
        this.player = player;
        this.identifier = identifier;
        this.battleResultHandler = new PostBattleActionSetHandler(player, victory, defeat);
    }

    @Override
    public void onVictory() {
        battleResultHandler.onVictory();

        if (!CobblemonTrainerBattle.playerHistoryRegistry.containsKey(player.getUuid())) {
            CobblemonTrainerBattle.playerHistoryRegistry.put(player.getUuid(), new PlayerHistory());
        }

        CobblemonTrainerBattle.playerHistoryRegistry.get(player.getUuid()).addPlayerVictory(identifier);
    }

    @Override
    public void onDefeat() {
        battleResultHandler.onDefeat();

        if (!CobblemonTrainerBattle.playerHistoryRegistry.containsKey(player.getUuid())) {
            CobblemonTrainerBattle.playerHistoryRegistry.put(player.getUuid(), new PlayerHistory());
        }

        CobblemonTrainerBattle.playerHistoryRegistry.get(player.getUuid()).addPlayerDefeat(identifier);
    }
}
