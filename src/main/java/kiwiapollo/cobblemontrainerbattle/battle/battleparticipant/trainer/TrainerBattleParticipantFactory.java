package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.parser.preset.TrainerStorage;

public class TrainerBattleParticipantFactory implements SimpleFactory<TrainerBattleParticipant> {
    private final String trainer;

    public TrainerBattleParticipantFactory(String trainer) {
        this.trainer = trainer;
    }

    @Override
    public TrainerBattleParticipant create() {
        return TrainerStorage.getInstance().get(trainer);
    }
}
