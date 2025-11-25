package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.block.CustomBlock;
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
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public class CustomItem {
    public static void register() {
        Arrays.stream(MiscItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(CustomBlock.values()).forEach(block -> {
            Registry.register(Registries.BLOCK, block.getIdentifier(), block.getBlock());
            Registry.register(Registries.ITEM, block.getIdentifier(), block.getItem());
        });

        Arrays.stream(InclementEmeraldTicketItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(InclementEmeraldTokenItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(RadicalRedTicketItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(RadicalRedTokenItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(XyTicketItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(XyTokenItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(BdspTicketItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(BdspTokenItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(VsSeekerItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });
    }
}
