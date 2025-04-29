package kiwiapollo.cobblemontrainerbattle.loot;

import net.minecraft.loot.condition.LootConditionType;

public class CustomLootConditionType {
    public static final LootConditionType DEFEATED_IN_BATTLE = new LootConditionType(new DefeatedInBattleLootCondition.Serializer());
}
