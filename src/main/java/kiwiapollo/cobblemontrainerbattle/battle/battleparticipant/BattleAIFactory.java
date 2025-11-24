package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant;

import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.battles.ai.RandomBattleAI;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.common.StrongBattleAI;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplate;

import java.util.Objects;

public class BattleAIFactory implements SimpleFactory<BattleAI> {
    private final TrainerTemplate trainer;

    public BattleAIFactory(TrainerTemplate trainer) {
        this.trainer = trainer;
    }

    @Override
    public BattleAI create() {
        // StrongBattleAI have issues with Double/Triple battles
        if (Objects.equals(trainer.getBattleAI(), "single")) {
            return new SingleBattleAIFactory().create();
        } else {
            return new NonSingleBattleAIFactory().create();
        }
    }

    private class SingleBattleAIFactory implements SimpleFactory<BattleAI> {
        @Override
        public BattleAI create() {
            return switch (trainer.getBattleAI()) {
                case "random" -> new RandomBattleAI();
                case "generation5" -> new Generation5AI();
                case "strong0" -> new StrongBattleAI(0);
                case "strong1" -> new StrongBattleAI(1);
                case "strong2" -> new StrongBattleAI(2);
                case "strong3" -> new StrongBattleAI(3);
                case "strong4" -> new StrongBattleAI(4);
                case "strong5" -> new StrongBattleAI(5);
                default -> new Generation5AI();
            };
        }
    }

    private class NonSingleBattleAIFactory implements SimpleFactory<BattleAI> {
        @Override
        public BattleAI create() {
            return switch (trainer.getBattleAI()) {
                case "random" -> new RandomBattleAI();
                case "generation5" -> new Generation5AI();
                default -> new Generation5AI();
            };
        }
    }
}
