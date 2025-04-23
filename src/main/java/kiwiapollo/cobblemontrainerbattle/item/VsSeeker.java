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

import java.util.function.Predicate;

public class VsSeeker extends Item implements Predicate<String> {
    public static final int MAX_COUNT = 1;
    private final Predicate<String> predicate;

    public VsSeeker(Predicate<String> predicate) {
        super(new Item.Settings().maxCount(MAX_COUNT));
        this.predicate = predicate;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!hasTicketInOtherHand(user, hand)) {
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        if (!isHitBlock(user)) {
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        BlockPos clickedPos = ((BlockHitResult) getHitResult(user)).getBlockPos();
        if (!itSolidBlock(world, clickedPos)) {
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        BlockPos spawnPos = clickedPos.up();
        if (!isEmptyPosition(world, spawnPos)) {
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        ItemStack ticketStack = user.getStackInHand(getOtherHand(hand));
        TrainerTicket ticket = (TrainerTicket) ticketStack.getItem();

        TrainerEntity entity = new TrainerEntity(EntityTypes.TRAINER, world, ticket.getTrainer());

        entity.refreshPositionAndAngles(spawnPos, user.getYaw(), user.getPitch());
        world.spawnEntity(entity);

        if (!user.getAbilities().creativeMode) {
            ticketStack.decrement(1);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    private HitResult getHitResult(PlayerEntity user) {
        return user.raycast(5.0D, 0.0F, false);
    }

    private boolean isHitBlock(PlayerEntity user) {
        return getHitResult(user).getType().equals(HitResult.Type.BLOCK);
    }

    private boolean itSolidBlock(World world, BlockPos clickedPos) {
        return world.getBlockState(clickedPos).isSolidBlock(world, clickedPos.down());
    }

    private boolean isEmptyPosition(World world, BlockPos spawnPos) {
        return world.getBlockState(spawnPos).isAir();
    }

    private boolean hasTicketInOtherHand(PlayerEntity user, Hand hand) {
        return user.getStackInHand(getOtherHand(hand)).getItem() instanceof TrainerTicket;
    }

    private Hand getOtherHand(Hand hand) {
        return hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
    }

    @Override
    public boolean test(String trainer) {
        return predicate.test(trainer);
    }
}
