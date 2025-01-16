package kiwiapollo.cobblemontrainerbattle.battle.predicate;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ForbiddenHeldItemPredicate extends HeldItemPredicate {
    private final List<ItemStack> forbidden;
    private Text error;

    public ForbiddenHeldItemPredicate(List<String> forbidden) {
        this.forbidden = forbidden.stream()
                .map(item -> Registries.ITEM.get(Identifier.tryParse(item)))
                .map(Item::getDefaultStack)
                .filter(stack -> !stack.isEmpty())
                .toList();
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.forbidden_held_item", error);
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        Set<ItemStack> party = player.getParty().toGappyList().stream()
                .filter(Objects::nonNull)
                .map(Pokemon::heldItem)
                .collect(Collectors.toSet());

        for (ItemStack f : forbidden) {
            if (containsItemStack(party, f)) {
                error = f.getName();
                return false;
            }
        }
        return true;
    }
}
