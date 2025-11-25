package kiwiapollo.cobblemontrainerbattle.item.vsseeker;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
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
