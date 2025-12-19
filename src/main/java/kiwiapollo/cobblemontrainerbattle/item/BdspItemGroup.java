package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.item.ticket.BdspTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.token.BdspTokenItem;
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

public class BdspItemGroup {
    private static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(CobblemonTrainerBattle.MOD_ID, "bdsp"));
    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder().icon(() -> new ItemStack(VsSeekerItem.YELLOW_VS_SEEKER.getItem())).displayName(Text.translatable("item_group.cobblemontrainerbattle.bdsp")).build();

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY, ITEM_GROUP);
        
        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register(group -> {
            Arrays.stream(BdspTicketItem.values()).map(BdspTicketItem::getItem).forEach(group::add);
            Arrays.stream(BdspTokenItem.values()).map(BdspTokenItem::getItem).forEach(group::add);
        });
    }
}
