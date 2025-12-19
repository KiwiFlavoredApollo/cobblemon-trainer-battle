package kiwiapollo.cobblemontrainerbattle.event;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.item.misc.DeprecatedItem;
import kiwiapollo.cobblemontrainerbattle.item.misc.MiscItem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;

public class LegacyItemMigrator implements Command<ServerCommandSource> {
    private static final Map<Item, Item> ITEM = Map.ofEntries(
            Map.entry(DeprecatedItem.STATIC_TRAINER_SPAWN_EGG.getItem(), MiscItem.MANNEQUIN_SPAWN_EGG.getItem()),
            Map.entry(DeprecatedItem.NEUTRAL_TRAINER_SPAWN_EGG.getItem(), MiscItem.TRAINER_SPAWN_EGG.getItem())
    );

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        PlayerInventory inventory = player.getInventory();

        for (Map.Entry<Item, Item> entry : ITEM.entrySet()) {
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = inventory.getStack(i);
                Item oldItem = entry.getKey();
                Item newItem = entry.getValue();

                if (isOldItem(stack, oldItem)) {
                    inventory.setStack(i, toNewStack(stack, newItem));
                }
            }
        }

        player.playerScreenHandler.sendContentUpdates();

        return SINGLE_SUCCESS;
    }

    private boolean isOldItem(ItemStack stack, Item oldItem) {
        return !stack.isEmpty() && stack.getItem() == oldItem;
    }

    private ItemStack toNewStack(ItemStack stack, Item newItem) {
        ItemStack newStack = new ItemStack(newItem, stack.getCount());

        if (stack.hasNbt()) {
            newStack.setNbt(stack.getNbt().copy());
        }

        return newStack;
    }
}
