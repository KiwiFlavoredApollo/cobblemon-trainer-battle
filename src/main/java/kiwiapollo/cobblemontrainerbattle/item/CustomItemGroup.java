package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public class CustomItemGroup {
    public static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(CobblemonTrainerBattle.MOD_ID, "item_group"));
    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder().icon(() -> new ItemStack(VsSeekerItems.BLUE_VS_SEEKER.getItem())).displayName(Text.literal("Trainers")).build();

    public static void register() {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY, ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(MiscItems.TRAINER_SPAWN_EGG);
            itemGroup.add(MiscItems.TRAINER_TOKEN);

            Arrays.stream(VsSeekerItems.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(InclementEmeraldTicketItems.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(InclementEmeraldTokenItems.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(RadicalRedTicketItems.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(RadicalRedTokenItems.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(XyTicketItems.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(XyTokenItems.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(BdspTicketItems.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(BdspTokenItems.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });
        });
    }
}
