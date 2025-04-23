package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.entity.EntityTypes;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;

import java.util.List;

public class MiscItems {
    public static final Item TRAINER_SPAWN_EGG = new SpawnEggItem(EntityTypes.TRAINER, 0xAAAAAA, 0xFF5555, new FabricItemSettings().maxCount(64));

    public static final Item TRAINER_TOKEN = new Item(new Item.Settings());

    public static final Item BLUE_VS_SEEKER = new VsSeeker();
    public static final Item RED_VS_SEEKER = new VsSeeker();
    public static final Item GREEN_VS_SEEKER = new VsSeeker();
    public static final Item PURPLE_VS_SEEKER = new VsSeeker();
    public static final Item PINK_VS_SEEKER = new VsSeeker();
    public static final Item YELLOW_VS_SEEKER = new VsSeeker();
}
