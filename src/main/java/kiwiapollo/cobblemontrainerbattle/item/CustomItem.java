package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.block.CustomBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public class CustomItem {
    public static void register() {
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

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "neutral_trainer_spawn_egg"), MiscItem.NEUTRAL_TRAINER_SPAWN_EGG);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "hostile_trainer_spawn_egg"), MiscItem.HOSTILE_TRAINER_SPAWN_EGG);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "static_trainer_spawn_egg"), MiscItem.STATIC_TRAINER_SPAWN_EGG);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "trainer_token"), MiscItem.TRAINER_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "empty_poke_ball"), MiscItem.EMPTY_POKE_BALL);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "filled_poke_ball"), MiscItem.FILLED_POKE_BALL);
    }
}
