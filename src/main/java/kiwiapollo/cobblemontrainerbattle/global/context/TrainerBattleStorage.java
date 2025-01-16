package kiwiapollo.cobblemontrainerbattle.global.context;

import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;

public interface TrainerBattleStorage {
    TrainerBattle getTrainerBattle();

    void setTrainerBattle(TrainerBattle battle);

    void clearTrainerBattle();
}
