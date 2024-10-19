package kiwiapollo.cobblemontrainerbattle.resulthandler;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.parser.TrainerBattleHistory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class RecordedSessionResultHandler implements ResultHandler {
    private final ServerPlayerEntity player;
    private final Identifier identifier;
    private final ResultHandler resultHandler;

    public RecordedSessionResultHandler(
            ServerPlayerEntity player,
            Identifier identifier,
            ResultAction victory,
            ResultAction defeat
    ) {
        this.player = player;
        this.identifier = identifier;
        this.resultHandler = new ResultActionHandler(player, victory, defeat);
    }

    @Override
    public void onVictory() {
        resultHandler.onVictory();

        if (!CobblemonTrainerBattle.trainerBattleHistoryRegistry.containsKey(player.getUuid())) {
            CobblemonTrainerBattle.trainerBattleHistoryRegistry.put(player.getUuid(), new TrainerBattleHistory());
        }

        CobblemonTrainerBattle.trainerBattleHistoryRegistry.get(player.getUuid()).addPlayerVictory(identifier);
    }

    @Override
    public void onDefeat() {
        resultHandler.onDefeat();

        if (!CobblemonTrainerBattle.trainerBattleHistoryRegistry.containsKey(player.getUuid())) {
            CobblemonTrainerBattle.trainerBattleHistoryRegistry.put(player.getUuid(), new TrainerBattleHistory());
        }

        CobblemonTrainerBattle.trainerBattleHistoryRegistry.get(player.getUuid()).addPlayerDefeat(identifier);
    }
}
