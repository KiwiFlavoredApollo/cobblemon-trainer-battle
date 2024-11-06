package kiwiapollo.cobblemontrainerbattle.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.JsonSerializer;

public class DefeatedInBattleLootCondition implements LootCondition {
    @Override
    public LootConditionType getType() {
        return CustomLootConditionTypes.DEFEATED_IN_BATTLE;
    }

    @Override
    public boolean test(LootContext lootContext) {
        try {
            DamageSource generic = lootContext.getWorld().getDamageSources().generic();
            boolean isGenericDamageSource = lootContext.get(LootContextParameters.DAMAGE_SOURCE).getType().equals(generic.getType());
            boolean isTrainerEntity = lootContext.get(LootContextParameters.THIS_ENTITY) instanceof TrainerEntity;

            return isGenericDamageSource && isTrainerEntity;

        } catch (NullPointerException e) {
            return false;
        }
    }

    public static class Serializer implements JsonSerializer<DefeatedInBattleLootCondition> {
        public Serializer() {

        }

        public void toJson(
                JsonObject jsonObject,
                DefeatedInBattleLootCondition survivesExplosionLootCondition,
                JsonSerializationContext jsonSerializationContext
        ) {

        }

        public DefeatedInBattleLootCondition fromJson(
                JsonObject jsonObject,
                JsonDeserializationContext jsonDeserializationContext
        ) {
            return new DefeatedInBattleLootCondition();
        }
    }
}
