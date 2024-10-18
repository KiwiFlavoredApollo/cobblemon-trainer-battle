package kiwiapollo.cobblemontrainerbattle.resulthandler;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.common.ResultAction;
import kiwiapollo.cobblemontrainerbattle.parser.TrainerBattleHistory;

public class RecordedResultHandler implements ResultHandler {
    private final PlayerBattleParticipant player;
    private final TrainerBattleParticipant trainer;
    private final GenericResultHandler resultHandler;

    public RecordedResultHandler(
            PlayerBattleParticipant player,
            TrainerBattleParticipant trainer,
            ResultAction victory,
            ResultAction defeat
    ) {
        this.player = player;
        this.trainer = trainer;
        this.resultHandler = new GenericResultHandler(player.getPlayerEntity(), victory, defeat);
    }

    @Override
    public void onVictory() {
        resultHandler.onVictory();

        if (!CobblemonTrainerBattle.trainerBattleHistoryRegistry.containsKey(player.getUuid())) {
            CobblemonTrainerBattle.trainerBattleHistoryRegistry.put(player.getUuid(), new TrainerBattleHistory());
        }

        CobblemonTrainerBattle.trainerBattleHistoryRegistry.get(player.getUuid()).addPlayerVictory(trainer.getIdentifier());
    }

    @Override
    public void onDefeat() {
        resultHandler.onDefeat();

        if (!CobblemonTrainerBattle.trainerBattleHistoryRegistry.containsKey(player.getUuid())) {
            CobblemonTrainerBattle.trainerBattleHistoryRegistry.put(player.getUuid(), new TrainerBattleHistory());
        }

        CobblemonTrainerBattle.trainerBattleHistoryRegistry.get(player.getUuid()).addPlayerDefeat(trainer.getIdentifier());
    }
}
