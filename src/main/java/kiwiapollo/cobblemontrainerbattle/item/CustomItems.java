package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public class CustomItems {
    public static void register() {
        Arrays.stream(InclementEmeraldTickets.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(InclementEmeraldTokens.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(RadicalRedTickets.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(RadicalRedTokens.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(XyTickets.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(XyTokens.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(BdspTickets.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(BdspTokens.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(VsSeekerItems.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "trainer_spawn_egg"), MiscItems.TRAINER_SPAWN_EGG);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "trainer_token"), MiscItems.TRAINER_TOKEN);
    }
}
