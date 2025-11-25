package kiwiapollo.cobblemontrainerbattle.datagen.tag;

import kiwiapollo.cobblemontrainerbattle.item.vsseeker.VsSeekerItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class TagProvider extends FabricTagProvider<Item> {
    public TagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ITEM, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        Arrays.stream(VsSeekerItem.values()).map(VsSeekerItem::getItem)
                .forEach(item -> getOrCreateTagBuilder(ItemTagRegistry.VS_SEEKERS).add(item));
    }
}
