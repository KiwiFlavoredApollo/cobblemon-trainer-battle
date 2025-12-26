package kiwiapollo.cobblemontrainerbattle.item.ticket;

import kiwiapollo.cobblemontrainerbattle.entity.CustomEntityType;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TrainerTicket extends Item {
    private final Identifier trainer;

    public TrainerTicket(Identifier trainer) {
        super(new Item.Settings());
        this.trainer = trainer;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        ItemStack itemStack = context.getStack();
        BlockPos pos = context.getBlockPos().up();

        TrainerEntity entity = new TrainerEntity(CustomEntityType.TRAINER, world);
        entity.setTrainer(trainer);

        entity.refreshPositionAndAngles(pos, player.getYaw(), player.getPitch());
        world.spawnEntity(entity);

        if (!player.isCreative()) {
            itemStack.decrement(1);
        }

        return ActionResult.SUCCESS;
    }
}
