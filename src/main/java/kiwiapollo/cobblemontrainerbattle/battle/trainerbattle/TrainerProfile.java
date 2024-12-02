package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.parser.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.battle.postbattle.PostBattleActionSet;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;

import java.util.List;

public record TrainerProfile(
        List<ShowdownPokemon> team,

        String displayName,

        boolean isSpawningAllowed,
        boolean isRematchAllowed,

        int maximumPartySize,
        int minimumPartySize,

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

        SoundEvent battleTheme,

        PostBattleActionSet onVictory,
        PostBattleActionSet onDefeat
) {

}
