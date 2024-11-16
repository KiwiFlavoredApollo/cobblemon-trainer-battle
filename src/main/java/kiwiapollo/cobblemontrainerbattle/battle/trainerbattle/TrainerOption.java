package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.postbattle.PostBattleActionSet;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemon;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;

import java.util.List;

public class TrainerOption {
    public boolean isSpawningAllowed;
    public boolean isRematchAllowed;
    public int maximumPartyLevel;
    public int minimumPartyLevel;
    public List<ShowdownPokemon> requiredPokemon;
    public List<ItemStack> requiredHeldItem;
    public List<String> requiredAbility;
    public List<String> requiredMove;
    public List<ShowdownPokemon> forbiddenPokemon;
    public List<ItemStack> forbiddenHeldItem;
    public List<String> forbiddenAbility;
    public List<String> forbiddenMove;
    public PostBattleActionSet onVictory;
    public PostBattleActionSet onDefeat;

    public TrainerOption() {
        this.isSpawningAllowed = true;
        this.isRematchAllowed = true;
        this.maximumPartyLevel = 100;
        this.minimumPartyLevel = 1;
        this.requiredPokemon = List.of();
        this.requiredHeldItem = List.of();
        this.requiredAbility = List.of();
        this.requiredMove = List.of();
        this.forbiddenPokemon = List.of();
        this.forbiddenHeldItem = List.of();
        this.forbiddenAbility = List.of();
        this.forbiddenMove = List.of();
        this.onVictory = new PostBattleActionSet();
        this.onDefeat = new PostBattleActionSet();
    }
}
