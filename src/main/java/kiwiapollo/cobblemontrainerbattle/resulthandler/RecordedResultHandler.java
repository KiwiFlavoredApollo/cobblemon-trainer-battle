package kiwiapollo.cobblemontrainerbattle.resulthandler;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.common.ResultAction;
import kiwiapollo.cobblemontrainerbattle.common.TrainerBattleHistory;
import net.minecraft.server.network.ServerPlayerEntity;

public class RecordedResultHandler implements ResultHandler {
    private final GenericResultHandler resultHandler;
    private final ServerPlayerEntity player;

    public RecordedResultHandler(ServerPlayerEntity player, ResultAction victory, ResultAction defeat) {
        this.resultHandler = new GenericResultHandler(player, victory, defeat);
        this.player = player;
    }

    @Override
    public void onVictory(TrainerBattleParticipant trainer) {
        resultHandler.onVictory(trainer);

        if (!CobblemonTrainerBattle.trainerBattleHistoryRegistry.containsKey(player.getUuid())) {
            CobblemonTrainerBattle.trainerBattleHistoryRegistry.put(player.getUuid(), new TrainerBattleHistory(player));
        }

        CobblemonTrainerBattle.trainerBattleHistoryRegistry.get(player.getUuid()).addPlayerVictory(trainer.getIdentifier());
    }

    @Override
    public void onDefeat(TrainerBattleParticipant trainer) {
        resultHandler.onDefeat(trainer);

        if (!CobblemonTrainerBattle.trainerBattleHistoryRegistry.containsKey(player.getUuid())) {
            CobblemonTrainerBattle.trainerBattleHistoryRegistry.put(player.getUuid(), new TrainerBattleHistory(player));
        }

        CobblemonTrainerBattle.trainerBattleHistoryRegistry.get(player.getUuid()).addPlayerDefeat(trainer.getIdentifier());
    }
}
