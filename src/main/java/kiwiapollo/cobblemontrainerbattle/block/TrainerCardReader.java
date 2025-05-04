package kiwiapollo.cobblemontrainerbattle.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class TrainerCardReader extends BlockWithEntity {
    public TrainerCardReader() {
        super(AbstractBlock.Settings.copy(Blocks.COBBLESTONE));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TrainerCardReaderBlockEntity(pos, state);
    }
}
