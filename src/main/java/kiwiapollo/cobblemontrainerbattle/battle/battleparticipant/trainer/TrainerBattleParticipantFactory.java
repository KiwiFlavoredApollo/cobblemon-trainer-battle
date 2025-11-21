package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplate;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplateStorage;
import net.minecraft.util.Identifier;

public class TrainerBattleParticipantFactory implements SimpleFactory<TrainerBattleParticipant> {
    private final Identifier trainer;

    public TrainerBattleParticipantFactory(Identifier trainer) {
        this.trainer = trainer;
    }

    @Override
    public TrainerBattleParticipant create() {
        TrainerTemplate template = TrainerTemplateStorage.getInstance().get(trainer);
        return switch(template.getLevelMode()) {
            case NORMAL -> new NormalLevelTrainer(template);
            case RELATIVE -> new RelativeLevelTrainer(template);
            case FLAT -> new FlatLevelTrainer(template);
        };
    }
}
