package kiwiapollo.cobblemontrainerbattle.item.misc;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entity.CustomEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class DeprecatedItem {
    public static final List<Item> all = new ArrayList<>();

    public static final Item NEUTRAL_TRAINER_SPAWN_EGG = register("neutral_trainer_spawn_egg", new SpawnEggItem(CustomEntityType.NEUTRAL_TRAINER, 0xAAAAAA, 0xFF5555, new Item.Settings().maxCount(64)));
    public static final Item STATIC_TRAINER_SPAWN_EGG = register("static_trainer_spawn_egg", new SpawnEggItem(CustomEntityType.STATIC_TRAINER, 0xAAAAAA, 0x55FF55, new Item.Settings().maxCount(64)));
    public static final Item EMPTY_POKE_BALL = register("empty_poke_ball", new EmptyPokeBall());
    public static final Item FILLED_POKE_BALL = register("filled_poke_ball", new FilledPokeBall());

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
