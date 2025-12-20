package kiwiapollo.cobblemontrainerbattle.datagen.advancement;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;

import java.util.function.Consumer;

public class AdvancementProvider extends FabricAdvancementProvider {
    public AdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        new RootAdvancementProvider(output).generateAdvancement(consumer);
        new MiscAdvancementProvider(output).generateAdvancement(consumer);
        new InclementEmeraldAdvancementProvider(output).generateAdvancement(consumer);
        new RadicalRedAdvancementProvider(output).generateAdvancement(consumer);
        new XyAdvancementProvider(output).generateAdvancement(consumer);
        new BdspAdvancementProvider(output).generateAdvancement(consumer);
    }
}
