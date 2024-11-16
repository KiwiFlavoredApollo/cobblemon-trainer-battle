package kiwiapollo.cobblemontrainerbattle.battle.battlefactory;

import kiwiapollo.cobblemontrainerbattle.battle.postbattle.PostBattleActionSet;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemon;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;

import java.util.List;

public class BattleFactoryProfile {
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
