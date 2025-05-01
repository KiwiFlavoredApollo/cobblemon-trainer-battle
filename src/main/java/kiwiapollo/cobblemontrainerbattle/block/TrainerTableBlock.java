package kiwiapollo.cobblemontrainerbattle.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class TrainerTableBlock extends BlockWithEntity {
    public TrainerTableBlock() {
        super(AbstractBlock.Settings.copy(Blocks.COBBLESTONE));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TrainerTableBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
