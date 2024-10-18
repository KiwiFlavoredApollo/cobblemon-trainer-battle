package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.parser.SmogonPokemon;
import kiwiapollo.cobblemontrainerbattle.resulthandler.ResultAction;

import java.util.List;

public record TrainerProfile(
        String name,
        List<SmogonPokemon> team,
        ResultAction onVictory,
        ResultAction onDefeat,
        BattleCondition condition
) {}
