package kiwiapollo.cobblemontrainerbattle.battle.groupbattle;

import kiwiapollo.cobblemontrainerbattle.battle.postbattle.PostBattleActionSet;
import kiwiapollo.cobblemontrainerbattle.parser.pokemon.ShowdownPokemon;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;

import java.util.List;

public class TrainerGroupProfile {
    public List<String> trainers;

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
}
