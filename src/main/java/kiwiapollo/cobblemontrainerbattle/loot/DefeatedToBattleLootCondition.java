package kiwiapollo.cobblemontrainerbattle.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.JsonSerializer;

public class DefeatedToBattleLootCondition implements LootCondition {
    static final DefeatedToBattleLootCondition INSTANCE = new DefeatedToBattleLootCondition();

    @Override
    public LootConditionType getType() {
        return CobblemonTrainerBattle.DEFEATED_TO_BATTLE;
    }

    @Override
    public boolean test(LootContext lootContext) {
        try {
            DamageSource generic = lootContext.getWorld().getDamageSources().generic();
            boolean isGeneric = lootContext.get(LootContextParameters.DAMAGE_SOURCE).getType().equals(generic.getType());
            boolean isTrainer = lootContext.get(LootContextParameters.THIS_ENTITY) instanceof TrainerEntity;

            return isGeneric && isTrainer;

        } catch (NullPointerException e) {
            return false;
        }
    }

    public static class Serializer implements JsonSerializer<DefeatedToBattleLootCondition> {
        public Serializer() {

        }

        public void toJson(
                JsonObject jsonObject,
                DefeatedToBattleLootCondition survivesExplosionLootCondition,
                JsonSerializationContext jsonSerializationContext
        ) {

        }

        public DefeatedToBattleLootCondition fromJson(
                JsonObject jsonObject,
                JsonDeserializationContext jsonDeserializationContext
        ) {
            return DefeatedToBattleLootCondition.INSTANCE;
        }
    }
}
