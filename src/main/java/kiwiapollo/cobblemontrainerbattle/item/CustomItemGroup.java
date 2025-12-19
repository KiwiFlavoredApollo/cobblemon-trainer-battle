package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.item.misc.MiscItem;
import kiwiapollo.cobblemontrainerbattle.item.ticket.BdspTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.ticket.InclementEmeraldTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.ticket.RadicalRedTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.ticket.XyTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.token.BdspTokenItem;
import kiwiapollo.cobblemontrainerbattle.item.token.InclementEmeraldTokenItem;
import kiwiapollo.cobblemontrainerbattle.item.token.RadicalRedTokenItem;
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

public class CustomItemGroup {
    private static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(CobblemonTrainerBattle.MOD_ID, "item_group"));
    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder().icon(() -> new ItemStack(VsSeekerItem.BLUE_VS_SEEKER)).displayName(Text.translatable("item_group.cobblemontrainerbattle.cobblemontrainerbattle")).build();

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY, ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register(group -> {
            MiscItem.getAll().forEach(group::add);
            VsSeekerItem.getAll().forEach(group::add);

            Arrays.stream(InclementEmeraldTicketItem.values()).map(InclementEmeraldTicketItem::getItem).forEach(group::add);
            Arrays.stream(InclementEmeraldTokenItem.values()).map(InclementEmeraldTokenItem::getItem).forEach(group::add);

            Arrays.stream(RadicalRedTicketItem.values()).map(RadicalRedTicketItem::getItem).forEach(group::add);
            Arrays.stream(RadicalRedTokenItem.values()).map(RadicalRedTokenItem::getItem).forEach(group::add);

            Arrays.stream(XyTicketItem.values()).map(XyTicketItem::getItem).forEach(group::add);
            Arrays.stream(XyTokenItem.values()).map(XyTokenItem::getItem).forEach(group::add);

            Arrays.stream(BdspTicketItem.values()).map(BdspTicketItem::getItem).forEach(group::add);
            Arrays.stream(BdspTokenItem.values()).map(BdspTokenItem::getItem).forEach(group::add);
        });
    }
}
