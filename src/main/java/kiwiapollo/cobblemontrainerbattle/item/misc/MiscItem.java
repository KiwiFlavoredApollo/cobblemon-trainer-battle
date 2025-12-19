package kiwiapollo.cobblemontrainerbattle.item.misc;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entity.CustomEntityType;
import kiwiapollo.cobblemontrainerbattle.item.token.TrainerToken;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MiscItem {
    public static final List<Item> all = new ArrayList<>();

    public static final Item TRAINER_SPAWN_EGG = register("trainer_spawn_egg", new SpawnEggItem(CustomEntityType.TRAINER, 0xAAAAAA, 0xFF5555, new Item.Settings().maxCount(64)));
    public static final Item MANNEQUIN_SPAWN_EGG = register("mannequin_spawn_egg", new SpawnEggItem(CustomEntityType.MANNEQUIN, 0xAAAAAA, 0x55FF55, new Item.Settings().maxCount(64)));
    public static final Item TRAINER_TOKEN = register("trainer_token", new TrainerToken());

    public static void initialize() {

    }

    private static Item register(String name, Item item) {
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, name);
        Item registered = Registry.register(Registries.ITEM, identifier, item);
        all.add(registered);

        return registered;
    }

    public static List<Item> getAll() {
        return new ArrayList<>(all);
    }
}
