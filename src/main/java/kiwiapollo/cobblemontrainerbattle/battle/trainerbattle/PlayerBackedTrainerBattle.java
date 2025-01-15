package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;

public class PlayerBackedTrainerBattle extends AbstractTrainerBattle {
    public PlayerBackedTrainerBattle(PlayerBattleParticipant player, TrainerBattleParticipant trainer) {
        super(player, trainer);
    }
}
