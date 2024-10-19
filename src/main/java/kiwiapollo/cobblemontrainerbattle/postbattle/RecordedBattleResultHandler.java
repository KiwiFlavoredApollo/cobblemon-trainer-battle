package kiwiapollo.cobblemontrainerbattle.postbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.parser.PlayerBattleHistory;
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

        if (!CobblemonTrainerBattle.playerBattleHistoryRegistry.containsKey(player.getUuid())) {
            CobblemonTrainerBattle.playerBattleHistoryRegistry.put(player.getUuid(), new PlayerBattleHistory());
        }

        CobblemonTrainerBattle.playerBattleHistoryRegistry.get(player.getUuid()).addPlayerVictory(identifier);
    }

    @Override
    public void onDefeat() {
        battleResultHandler.onDefeat();

        if (!CobblemonTrainerBattle.playerBattleHistoryRegistry.containsKey(player.getUuid())) {
            CobblemonTrainerBattle.playerBattleHistoryRegistry.put(player.getUuid(), new PlayerBattleHistory());
        }

        CobblemonTrainerBattle.playerBattleHistoryRegistry.get(player.getUuid()).addPlayerDefeat(identifier);
    }
}
