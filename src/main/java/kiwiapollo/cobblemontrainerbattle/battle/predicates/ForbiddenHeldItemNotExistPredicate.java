package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

public class ForbiddenHeldItemNotExistPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final List<ItemStack> forbidden;

    public ForbiddenHeldItemNotExistPredicate(List<ItemStack> forbidden) {
        this.forbidden = forbidden.stream().filter(Objects::nonNull).toList();
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.literal("");
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        if (forbidden.isEmpty()) {
            return true;
        }

        for (ItemStack f : forbidden) {
            for (ItemStack p : player.getParty().toGappyList().stream().filter(Objects::nonNull).map(Pokemon::heldItem).toList()) {
                boolean isItemEqual = p.getItem().equals(f.getItem());

                if (isItemEqual) {
                    return false;
                }
            }
        }
        return true;
    }
}
