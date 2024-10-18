package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.parser.SmogonPokemon;

import java.util.List;

public record TrainerProfile(
        String name,
        List<SmogonPokemon> pokemons,
        ResultAction onVictory,
        ResultAction onDefeat,
        BattleCondition condition
) {}
