package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public class CustomItems {
    public static void register() {
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

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "normal_trainer_spawn_egg"), MiscItems.NORMAL_TRAINER_SPAWN_EGG);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "hostile_trainer_spawn_egg"), MiscItems.HOSTILE_TRAINER_SPAWN_EGG);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "static_trainer_spawn_egg"), MiscItems.STATIC_TRAINER_SPAWN_EGG);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "trainer_token"), MiscItems.TRAINER_TOKEN);
    }
}
