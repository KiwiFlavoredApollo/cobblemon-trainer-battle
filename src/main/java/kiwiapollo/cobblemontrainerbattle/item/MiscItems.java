package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.entity.EntityTypes;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;

public class MiscItems {
    public static final Item TRAINER_SPAWN_EGG = new SpawnEggItem(EntityTypes.TRAINER, 0xAAAAAA, 0xFF5555, new FabricItemSettings().maxCount(64));
    public static final Item STATIC_TRAINER_SPAWN_EGG = new SpawnEggItem(EntityTypes.STATIC_TRAINER, 0xAAAAAA, 0x55FF55, new FabricItemSettings().maxCount(64));
    public static final Item TRAINER_TOKEN = new Item(new Item.Settings());
}
