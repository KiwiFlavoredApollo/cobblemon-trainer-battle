package kiwiapollo.cobblemontrainerbattle.battle.predicate;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.item.ItemStack;

import java.util.Set;

public abstract class HeldItemPredicate implements MessagePredicate<PlayerBattleParticipant> {
    protected boolean containsItemStack(Set<ItemStack> party, ItemStack required) {
        for (ItemStack p : party) {
            if (p.getItem().equals(required.getItem())) {
                return true;
            }
        }
        return false;
    }
}
