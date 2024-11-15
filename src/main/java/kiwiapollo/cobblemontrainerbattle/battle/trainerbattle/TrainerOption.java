package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.postbattle.PostBattleActionSet;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;

import java.util.List;

public class TrainerOption {
    public boolean isSpawningAllowed;
    public boolean isRematchAllowed;
    public int maximumPartyLevel;
    public int minimumPartyLevel;
    public List<Pokemon> requiredPokemon;
    public List<ItemStack> requiredHeldItem;
    public List<Ability> requiredAbility;
    public List<Move> requiredMove;
    public List<Pokemon> forbiddenPokemon;
    public List<ItemStack> forbiddenHeldItem;
    public List<Ability> forbiddenAbility;
    public List<Move> forbiddenMove;
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
