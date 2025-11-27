package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.item.misc.LegacyItem;
import kiwiapollo.cobblemontrainerbattle.item.misc.MiscItem;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public class LegacyItemMigrator implements ServerPlayConnectionEvents.Join, UseBlockCallback {
    private static final Map<Item, Item> ITEM = Map.ofEntries(
            Map.entry(LegacyItem.STATIC_TRAINER_SPAWN_EGG.getItem(), MiscItem.CAMPER_SPAWN_EGG.getItem()),
            Map.entry(LegacyItem.NEUTRAL_TRAINER_SPAWN_EGG.getItem(), MiscItem.DRIFTER_SPAWN_EGG.getItem())
    );

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        ServerPlayerEntity player = handler.getPlayer();

        migrateInventory(player.getInventory());
        migrateInventory(player.getEnderChestInventory());

        player.playerScreenHandler.sendContentUpdates();
    }

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if (world.isClient()) return ActionResult.PASS;

        BlockPos pos = hitResult.getBlockPos();
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof Inventory inventory) {
            migrateInventory(inventory);
        }

        return ActionResult.PASS;
    }

    private void migrateInventory(Inventory inventory) {
        for (Map.Entry<Item, Item> entry : ITEM.entrySet()) {
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = inventory.getStack(i);
                Item oldItem = entry.getKey();
                Item newItem = entry.getValue();

                if (isShulkerBox(stack)) {
                    inventory.setStack(i, toShulkerBoxWithNewItem(stack, oldItem, newItem));

                } else if (isOldItem(stack, oldItem)) {
                    inventory.setStack(i, toNewStack(stack, newItem));
                }
            }
        }
    }

    private ItemStack toShulkerBoxWithNewItem(ItemStack stack, Item oldItem, Item newItem) {
        if (!stack.hasNbt()) {
            return stack;
        }

        NbtCompound blockEntityTag = stack.getSubNbt("BlockEntityTag");

        if (blockEntityTag == null || !blockEntityTag.contains("Items")) {
            return stack;
        }

        NbtList items = blockEntityTag.getList("Items", NbtCompound.COMPOUND_TYPE);

        for (int i = 0; i < items.size(); i++) {
            NbtCompound slotTag = items.getCompound(i);

            Identifier id = Identifier.tryParse(slotTag.getString("id"));
            Identifier oldId = Registries.ITEM.getId(oldItem);
            Identifier newId = Registries.ITEM.getId(newItem);

            if (id != null && id.equals(oldId)) {
                slotTag.putString("id", newId.toString());
            }
        }

        return stack;
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

    private boolean isShulkerBox(ItemStack stack) {
        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            return false;
        }

        return blockItem.getBlock() instanceof ShulkerBoxBlock;
    }
}
