package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.parser.SmogonPokemon;

import java.util.List;

public record Trainer(
        String name,
        List<SmogonPokemon> pokemons,
        ResultAction onVictory,
        ResultAction onDefeat,
        BattleCondition condition
) {}
