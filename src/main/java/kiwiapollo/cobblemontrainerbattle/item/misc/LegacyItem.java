package kiwiapollo.cobblemontrainerbattle.item.misc;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entity.CustomEntityType;
import kiwiapollo.cobblemontrainerbattle.item.token.TrainerToken;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;

public enum LegacyItem {
    NEUTRAL_TRAINER_SPAWN_EGG("neutral_trainer_spawn_egg", new SpawnEggItem(CustomEntityType.NEUTRAL_TRAINER, 0xAAAAAA, 0xFF5555, new Item.Settings().maxCount(64))),
    STATIC_TRAINER_SPAWN_EGG("static_trainer_spawn_egg", new SpawnEggItem(CustomEntityType.STATIC_TRAINER, 0xAAAAAA, 0x55FF55, new Item.Settings().maxCount(64))),
    EMPTY_POKE_BALL("empty_poke_ball", new EmptyPokeBall()),
    FILLED_POKE_BALL("filled_poke_ball", new FilledPokeBall());

    private final Identifier identifier;
    private final Item item;

    LegacyItem(String path, Item item) {
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
