package kiwiapollo.cobblemontrainerbattle.mixin;

import com.cobblemon.mod.common.Cobblemon;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntityBehavior;
import kiwiapollo.cobblemontrainerbattle.villager.PokeBallEngineerVillager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.VillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (!isPokeBallEngineer()) {
            return;
        }

        if (isBusyWithPokemonBattle()) {
            callbackInfo.setReturnValue(false);
            callbackInfo.cancel();
        }
    }

    private boolean isPokeBallEngineer() {
        try {
            VillagerEntity villager = (VillagerEntity) (Object) this;
            return villager.getVillagerData().getProfession().equals(PokeBallEngineerVillager.PROFESSION);

        } catch (ClassCastException e) {
            return false;
        }
    }

    private boolean isBusyWithPokemonBattle() {
        try {
            TrainerEntityBehavior villager = (TrainerEntityBehavior) this;
            return Objects.nonNull(Cobblemon.INSTANCE.getBattleRegistry().getBattle(villager.getBattleId()));

        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }
}
