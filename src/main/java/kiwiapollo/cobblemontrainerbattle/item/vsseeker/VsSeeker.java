package kiwiapollo.cobblemontrainerbattle.item.vsseeker;

import kiwiapollo.cobblemontrainerbattle.common.TrainerSelector;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VsSeeker extends Item implements TrainerSelector {
    private final MutableText group;
    private final TrainerSelector selector;

    public VsSeeker(MutableText group, TrainerSelector selector) {
        super(new Item.Settings());

        this.group = group;
        this.selector = selector;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(group.formatted(Formatting.YELLOW));
    }

    @Override
    public Identifier select() {
        return selector.select();
    }
}
