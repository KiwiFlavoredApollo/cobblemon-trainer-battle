package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.parser.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.battle.postbattle.PostBattleActionSet;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;

import java.util.List;

public class TrainerProfile {
    public final List<ShowdownPokemon> team;

    public final String displayName;

    public final String battleFormat;
    public final String battleAI;

    public final boolean isSpawningAllowed;
    public final boolean isRematchAllowed;

    public final int maximumPartySize;
    public final int minimumPartySize;

    public final int maximumPartyLevel;
    public final int minimumPartyLevel;

    public final List<String> requiredLabel;
    public final List<ShowdownPokemon> requiredPokemon;
    public final List<ItemStack> requiredHeldItem;
    public final List<String> requiredAbility;
    public final List<String> requiredMove;

    public final List<String> forbiddenLabel;
    public final List<ShowdownPokemon> forbiddenPokemon;
    public final List<ItemStack> forbiddenHeldItem;
    public final List<String> forbiddenAbility;
    public final List<String> forbiddenMove;

    public final SoundEvent battleTheme;

    public final PostBattleActionSet onVictory;
    public final PostBattleActionSet onDefeat;

    public TrainerProfile(List<ShowdownPokemon> team, TrainerOption option) {
        this.team = team;

        this.displayName = option.displayName;

        this.battleFormat = "single";
        this.battleAI = option.battleAI;

        this.isSpawningAllowed = option.isSpawningAllowed;
        this.isRematchAllowed = option.isRematchAllowed;

        this.maximumPartySize = option.maximumPartySize;
        this.minimumPartySize = option.minimumPartySize;

        this.maximumPartyLevel = option.maximumPartyLevel;
        this.minimumPartyLevel = option.minimumPartyLevel;

        this.requiredLabel = option.requiredLabel;
        this.requiredPokemon = option.requiredPokemon;
        this.requiredHeldItem = option.requiredHeldItem;
        this.requiredAbility = option.requiredAbility;
        this.requiredMove = option.requiredMove;

        this.forbiddenLabel = option.forbiddenLabel;
        this.forbiddenPokemon = option.forbiddenPokemon;
        this.forbiddenHeldItem = option.forbiddenHeldItem;
        this.forbiddenAbility = option.forbiddenAbility;
        this.forbiddenMove = option.forbiddenMove;

        this.battleTheme = option.battleTheme;

        this.onVictory = option.onVictory;
        this.onDefeat = option.onDefeat;
    }
}
