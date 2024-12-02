package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.battle.postbattle.PostBattleActionSet;
import kiwiapollo.cobblemontrainerbattle.parser.pokemon.ShowdownPokemon;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;

import java.util.List;

public class TrainerOption {
    public String displayName;

    public boolean isSpawningAllowed;
    public boolean isRematchAllowed;

    public int maximumPartySize;
    public int minimumPartySize;

    public int maximumPartyLevel;
    public int minimumPartyLevel;

    public List<String> requiredLabel;
    public List<ShowdownPokemon> requiredPokemon;
    public List<ItemStack> requiredHeldItem;
    public List<String> requiredAbility;
    public List<String> requiredMove;

    public List<String> forbiddenLabel;
    public List<ShowdownPokemon> forbiddenPokemon;
    public List<ItemStack> forbiddenHeldItem;
    public List<String> forbiddenAbility;
    public List<String> forbiddenMove;

    public SoundEvent battleTheme;

    public PostBattleActionSet onVictory;
    public PostBattleActionSet onDefeat;

    public TrainerOption() {
        this.displayName = null;

        this.isSpawningAllowed = true;
        this.isRematchAllowed = true;

        this.maximumPartySize = 6;
        this.minimumPartySize = 1;

        this.maximumPartyLevel = 100;
        this.minimumPartyLevel = 1;

        this.requiredLabel = List.of();
        this.requiredPokemon = List.of();
        this.requiredHeldItem = List.of();
        this.requiredAbility = List.of();
        this.requiredMove = List.of();

        this.forbiddenLabel = List.of();
        this.forbiddenPokemon = List.of();
        this.forbiddenHeldItem = List.of();
        this.forbiddenAbility = List.of();
        this.forbiddenMove = List.of();

        this.battleTheme = null;

        this.onVictory = new PostBattleActionSet();
        this.onDefeat = new PostBattleActionSet();
    }
}
