package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ItemTagRegistry {
    public static final TagKey<Item> VS_SEEKERS = TagKey.of(RegistryKeys.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "vs_seekers"));
}
