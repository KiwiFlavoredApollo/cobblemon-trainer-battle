package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.pokemon.Pokemon;
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
        List<Pokemon> requiredPokemon,
        List<ItemStack> requiredHeldItem,
        List<Ability> requiredAbility,
        List<Move> requiredMove,
        List<Pokemon> forbiddenPokemon,
        List<ItemStack> forbiddenHeldItem,
        List<Ability> forbiddenAbility,
        List<Move> forbiddenMove,
        PostBattleActionSet onVictory,
        PostBattleActionSet onDefeat
) {}
