package kiwiapollo.cobblemontrainerbattle.loot;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomLootConditionType {
    public static final LootConditionType DEFEATED_IN_BATTLE = register("defeated_in_battle", new LootConditionType(new DefeatedInBattleLootCondition.Serializer()));

    public static void initialize() {

    }

    private static LootConditionType register(String name, LootConditionType type) {
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, name);
        return Registry.register(Registries.LOOT_CONDITION_TYPE, identifier, type);
    }
}
