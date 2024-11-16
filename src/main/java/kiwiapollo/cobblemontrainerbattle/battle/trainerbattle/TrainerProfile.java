package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.battle.postbattle.PostBattleActionSet;
import net.minecraft.item.ItemStack;

import java.util.List;

public record TrainerProfile(
        String name,
        List<ShowdownPokemon> team,
        boolean isSpawningAllowed,
        boolean isRematchAllowed,
        int maximumPartyLevel,
        int minimumPartyLevel,
        List<String> requiredLabel,
        List<ShowdownPokemon> requiredPokemon,
        List<ItemStack> requiredHeldItem,
        List<String> requiredAbility,
        List<String> requiredMove,
        List<String> forbiddenLabel,
        List<ShowdownPokemon> forbiddenPokemon,
        List<ItemStack> forbiddenHeldItem,
        List<String> forbiddenAbility,
        List<String> forbiddenMove,
        PostBattleActionSet onVictory,
        PostBattleActionSet onDefeat
) {}
