package kiwiapollo.cobblemontrainerbattle.item.vsseeker;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VsSeeker extends Item implements SimpleFactory<Identifier> {
    private final String description;
    private final SimpleFactory<Identifier> factory;

    public VsSeeker(String description, SimpleFactory<Identifier> factory) {
        super(new Item.Settings());

        this.description = description;
        this.factory = factory;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(description).formatted(Formatting.YELLOW));
    }

    @Override
    public Identifier create() {
        return factory.create();
    }
}
