package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class RequiredHeldItemExistPredicate extends HeldItemPredicate {
    private final List<ItemStack> required;
    private ItemStack error;

    public RequiredHeldItemExistPredicate(List<ItemStack> required) {
        this.required = required.stream().filter(Objects::nonNull).toList();
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.required_held_item_exist", error);
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        Set<ItemStack> party = player.getParty().toGappyList().stream()
                .filter(Objects::nonNull)
                .map(Pokemon::heldItem)
                .collect(Collectors.toSet());

        for (ItemStack r : required) {
            if (!containsItemStack(party, r)) {
                error = r;
                return false;
            }
        }
        return true;
    }
}
