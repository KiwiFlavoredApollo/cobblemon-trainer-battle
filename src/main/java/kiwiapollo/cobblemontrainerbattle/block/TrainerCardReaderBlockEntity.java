package kiwiapollo.cobblemontrainerbattle.block;

import kiwiapollo.cobblemontrainerbattle.entity.CustomEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class TrainerCardReaderBlockEntity extends LootableContainerBlockEntity {
    private static final int SIZE = 9;

    private final DefaultedList<ItemStack> inventory;

    public TrainerCardReaderBlockEntity(BlockPos pos, BlockState state) {
        super(CustomEntityType.POKE_BALL_BOX, pos, state);
        this.inventory = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);;
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return null;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {

    }

    @Override
    protected Text getContainerName() {
        return null;
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
