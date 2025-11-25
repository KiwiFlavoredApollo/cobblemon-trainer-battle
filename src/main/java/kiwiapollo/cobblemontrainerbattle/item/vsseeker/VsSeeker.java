package kiwiapollo.cobblemontrainerbattle.item.vsseeker;

import kiwiapollo.cobblemontrainerbattle.entity.NeutralTrainerEntity;
import kiwiapollo.cobblemontrainerbattle.entity.CustomEntityType;
import kiwiapollo.cobblemontrainerbattle.item.ticket.TrainerTicket;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class VsSeeker extends Item implements Predicate<Identifier> {
    public static final int MAX_COUNT = 1;
    private final Predicate<Identifier> predicate;
    private final String description;

    public VsSeeker(String description, Predicate<Identifier> predicate) {
        super(new Item.Settings().maxCount(MAX_COUNT));
        this.predicate = predicate;
        this.description = description;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(description).formatted(Formatting.YELLOW));
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

        NeutralTrainerEntity entity = new NeutralTrainerEntity(CustomEntityType.NEUTRAL_TRAINER, world);
        entity.setTrainer(ticket.getTrainer());

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
    public boolean test(Identifier trainer) {
        return predicate.test(trainer);
    }

    public static List<VsSeeker> getVsSeekers(PlayerInventory inventory) {
        return inventory.combinedInventory.stream()
                .flatMap(DefaultedList::stream)
                .filter(stack -> !stack.isEmpty())
                .map(ItemStack::getItem)
                .filter(item -> item instanceof VsSeeker)
                .map(item -> (VsSeeker) item).toList();
    }
}
