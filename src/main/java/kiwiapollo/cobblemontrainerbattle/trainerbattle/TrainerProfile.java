package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import kiwiapollo.cobblemontrainerbattle.parser.SmogonPokemon;
import kiwiapollo.cobblemontrainerbattle.postbattle.PostBattleActionSet;

import java.util.List;

public record TrainerProfile(
        String name,
        List<SmogonPokemon> team,
        boolean isSpawningAllowed,
        BattleCondition condition,
        PostBattleActionSet onVictory,
        PostBattleActionSet onDefeat
) {}
