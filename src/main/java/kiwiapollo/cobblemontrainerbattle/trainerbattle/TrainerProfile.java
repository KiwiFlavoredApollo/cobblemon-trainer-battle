package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.postbattle.PostBattleActionSet;

import java.util.List;

public record TrainerProfile(
        String name,
        List<ShowdownPokemon> team,
        boolean isSpawningAllowed,
        BattleCondition condition,
        PostBattleActionSet onVictory,
        PostBattleActionSet onDefeat
) {}
