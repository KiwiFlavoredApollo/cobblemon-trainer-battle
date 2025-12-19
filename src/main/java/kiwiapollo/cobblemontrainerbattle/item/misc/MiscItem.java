package kiwiapollo.cobblemontrainerbattle.item.misc;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entity.CustomEntityType;
import kiwiapollo.cobblemontrainerbattle.item.token.TrainerToken;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;

public enum MiscItem {
    TRAINER_SPAWN_EGG("trainer_spawn_egg", new SpawnEggItem(CustomEntityType.TRAINER, 0xAAAAAA, 0xFF5555, new Item.Settings().maxCount(64))),
    MANNEQUIN_SPAWN_EGG("mannequin_spawn_egg", new SpawnEggItem(CustomEntityType.MANNEQUIN, 0xAAAAAA, 0x55FF55, new Item.Settings().maxCount(64))),
    TRAINER_TOKEN("trainer_token", new TrainerToken());

    private final Identifier identifier;
    private final Item item;

    MiscItem(String path, Item item) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
        this.item = item;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public Item getItem() {
        return item;
    }
}
