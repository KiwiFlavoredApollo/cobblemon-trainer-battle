package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.entity.CustomEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;

public class MiscItem {
    public static final Item NEUTRAL_TRAINER_SPAWN_EGG = new SpawnEggItem(CustomEntityType.NEUTRAL_TRAINER, 0xAAAAAA, 0xFF5555, new Item.Settings().maxCount(64));
    public static final Item HOSTILE_TRAINER_SPAWN_EGG = new SpawnEggItem(CustomEntityType.HOSTILE_TRAINER, 0xAAAAAA, 0x5555FF, new Item.Settings().maxCount(64));
    public static final Item STATIC_TRAINER_SPAWN_EGG = new SpawnEggItem(CustomEntityType.STATIC_TRAINER, 0xAAAAAA, 0x55FF55, new Item.Settings().maxCount(64));
    public static final Item TRAINER_TOKEN = new Item(new Item.Settings());
    public static final Item EMPTY_POKE_BALL = new EmptyPokeBall();
    public static final Item FILLED_POKE_BALL = new FilledPokeBall();
}
