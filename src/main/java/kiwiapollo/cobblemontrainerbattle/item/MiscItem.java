package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.entity.EntityType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;

public class MiscItem {
    public static final Item NORMAL_TRAINER_SPAWN_EGG = new SpawnEggItem(EntityType.NORMAL_TRAINER, 0xAAAAAA, 0xFF5555, new FabricItemSettings().maxCount(64));
    public static final Item HOSTILE_TRAINER_SPAWN_EGG = new SpawnEggItem(EntityType.HOSTILE_TRAINER, 0xAAAAAA, 0x5555FF, new FabricItemSettings().maxCount(64));
    public static final Item STATIC_TRAINER_SPAWN_EGG = new SpawnEggItem(EntityType.STATIC_TRAINER, 0xAAAAAA, 0x55FF55, new FabricItemSettings().maxCount(64));
    public static final Item TRAINER_TOKEN = new Item(new Item.Settings());
}
