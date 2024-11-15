package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

public class RequiredHeldItemExistPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final List<ItemStack> required;

    public RequiredHeldItemExistPredicate(List<ItemStack> required) {
        this.required = required.stream().filter(Objects::nonNull).toList();
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.literal("");
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        if (required.isEmpty()) {
            return true;
        }

        for (ItemStack r : required) {
            for (ItemStack p : player.getParty().toGappyList().stream().filter(Objects::nonNull).map(Pokemon::heldItem).toList()) {
                boolean isItemEqual = p.getItem().equals(r.getItem());

                if (isItemEqual) {
                    return true;
                }
            }
        }
        return false;
    }
}
