package kiwiapollo.cobblemontrainerbattle.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.Objects;

public class TrainerTableScreenHandler extends ScreenHandler {
    private static final int SIZE = 9;
    private static final int SLOT_SIZE = 18;

    private final Inventory inventory;

    public TrainerTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(SIZE));
    }

    public TrainerTableScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(CustomScreenHandlerType.TRAINER_TABLE, syncId);
        checkSize(inventory, SIZE);

        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);

        addThisInventorySlots(inventory);
        addPlayerInventorySlots(playerInventory);
        addPlayerHotbarSlots(playerInventory);
    }

    private void addThisInventorySlots(Inventory inventory) {
        final int TOP_OFFSET = 17;
        final int LEFT_OFFSET = 62;
        final int ROW_COUNT = 3;
        final int COL_COUNT = 3;

        for (int row = 0; row < ROW_COUNT; ++row) {
            for (int col = 0; col < COL_COUNT; ++col) {
                int index = getIndex(row, col, 0, COL_COUNT);
                int x = getX(col, LEFT_OFFSET);
                int y = getY(row, TOP_OFFSET);

                this.addSlot(new Slot(inventory, index, x, y));
            }
        }
    }

    private void addPlayerInventorySlots(Inventory inventory) {
        final int TOP_OFFSET = 84;
        final int LEFT_OFFSET = 8;
        final int ROW_COUNT = 3;
        final int COL_COUNT = 9;

        for (int row = 0; row < ROW_COUNT; ++row) {
            for (int col = 0; col < COL_COUNT; ++col) {
                int index = getIndex(row, col, COL_COUNT, COL_COUNT);
                int x = getX(col, LEFT_OFFSET);
                int y = getY(row, TOP_OFFSET);

                this.addSlot(new Slot(inventory, index, x, y));
            }
        }
    }

    private void addPlayerHotbarSlots(Inventory inventory) {
        final int TOP_OFFSET = 142;
        final int LEFT_OFFSET = 8;
        final int COL_COUNT = 9;

        for (int col = 0; col < COL_COUNT; ++col) {
            int index = getIndex(0, col, 0, 0);
            int x = getX(col, LEFT_OFFSET);
            int y = getY(0, TOP_OFFSET);

            this.addSlot(new Slot(inventory, index, x, y));
        }
    }

    private int getIndex(int row, int col, int offset, int width) {
        return col + row * width + offset;
    }

    private int getX(int col, int offset) {
        return col * SLOT_SIZE + offset;
    }

    private int getY(int row, int offset) {
        return row * SLOT_SIZE + offset;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (!slot.hasStack()) {
            return newStack;
        }

        ItemStack oldStack = slot.getStack();
        newStack = oldStack.copy();

        if (isThisToPlayer(index)) {
            if (!this.insertItem(oldStack, this.inventory.size(), this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }

        } else {
            if (!this.insertItem(oldStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }
        }

        if (oldStack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);

        } else {
            slot.markDirty();
        }

        return newStack;
    }

    private boolean isThisToPlayer(int index) {
        return index < this.inventory.size();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
