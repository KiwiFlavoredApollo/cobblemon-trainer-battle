package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.entity.EntityTypes;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VsSeeker extends Item {
    public static final int MAX_COUNT = 1;

    public VsSeeker() {
        super(new Item.Settings().maxCount(MAX_COUNT));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        try {
            assertPlayerHasTicketInOtherHand(user, hand);
            assertBlockHit(user);

            BlockPos clickedPos = ((BlockHitResult) getHitResult(user)).getBlockPos();
            assertSolidBlock(world, clickedPos);

            BlockPos spawnPos = clickedPos.up();
            assertEmptyPosition(world, spawnPos);

            ItemStack ticketStack = user.getStackInHand(getOtherHand(hand));
            TrainerTicket ticket = (TrainerTicket) ticketStack.getItem();

            TrainerEntity entity = new TrainerEntity(EntityTypes.TRAINER, world, ticket.getTrainerEntityPreset());

            entity.refreshPositionAndAngles(spawnPos, user.getYaw(), user.getPitch());
            world.spawnEntity(entity);

            if (!user.getAbilities().creativeMode) {
                ticketStack.decrement(1);
            }

            return TypedActionResult.success(user.getStackInHand(hand));

        } catch (IllegalStateException ignored) {
            return TypedActionResult.success(user.getStackInHand(hand));
        }
    }

    private HitResult getHitResult(PlayerEntity user) {
        return user.raycast(5.0D, 0.0F, false);
    }

    private void assertBlockHit(PlayerEntity user) {
        if (!getHitResult(user).getType().equals(HitResult.Type.BLOCK)) {
            throw new IllegalStateException();
        }
    }

    private void assertSolidBlock(World world, BlockPos clickedPos) {
        if (!world.getBlockState(clickedPos).isSolidBlock(world, clickedPos.down())) {
            throw new IllegalStateException();
        }
    }

    private void assertEmptyPosition(World world, BlockPos spawnPos) {
        if (!world.getBlockState(spawnPos).isAir()) {
            throw new IllegalStateException();
        }
    }

    private void assertPlayerHasTicketInOtherHand(PlayerEntity user, Hand hand) {
        if (!(user.getStackInHand(getOtherHand(hand)).getItem() instanceof TrainerTicket)) {
            throw new IllegalStateException();
        }
    }

    private Hand getOtherHand(Hand hand) {
        return hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
    }
}
