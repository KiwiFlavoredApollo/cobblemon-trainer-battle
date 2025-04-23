package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public class CustomItems {
    public static void register() {
        Arrays.stream(InclementEmeraldTicketItems.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(InclementEmeraldTokenItems.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(RadicalRedTicketItems.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(RadicalRedTokenItems.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(XyTicketItems.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(XyTokenItems.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(BdspTicketItems.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(BdspTokenItems.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(VsSeekerItems.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "trainer_spawn_egg"), MiscItems.TRAINER_SPAWN_EGG);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "trainer_token"), MiscItems.TRAINER_TOKEN);
    }
}
