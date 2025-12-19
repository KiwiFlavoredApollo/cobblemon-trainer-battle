package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.item.ticket.XyTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.token.XyTokenItem;
import kiwiapollo.cobblemontrainerbattle.item.vsseeker.VsSeekerItem;
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

public class XyItemGroup {
    private static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(CobblemonTrainerBattle.MOD_ID, "xy"));
    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder().icon(() -> new ItemStack(VsSeekerItem.PINK_VS_SEEKER.getItem())).displayName(Text.translatable("item_group.cobblemontrainerbattle.xy")).build();

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY, ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register(group -> {
            Arrays.stream(XyTicketItem.values()).map(XyTicketItem::getItem).forEach(group::add);
            Arrays.stream(XyTokenItem.values()).map(XyTokenItem::getItem).forEach(group::add);
        });
    }
}
