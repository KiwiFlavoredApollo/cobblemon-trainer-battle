package kiwiapollo.cobblemontrainerbattle.mixin;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import kiwiapollo.cobblemontrainerbattle.entity.BattleEntityBehavior;
import kiwiapollo.cobblemontrainerbattle.villager.PokeBallEngineerVillager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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

    @Unique
    private boolean isPokeBallEngineer() {
        try {
            VillagerEntity villager = (VillagerEntity) (Object) this;
            return villager.getVillagerData().getProfession().equals(PokeBallEngineerVillager.PROFESSION);

        } catch (ClassCastException e) {
            return false;
        }
    }

    @Unique
    private boolean isBusyWithPokemonBattle() {
        try {
            BattleEntityBehavior villager = (BattleEntityBehavior) this;
            return Objects.nonNull(Cobblemon.INSTANCE.getBattleRegistry().getBattle(villager.getBattleId()));

        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    public void onDeath(DamageSource source, CallbackInfo callbackInfo) {
        if (!isPokeBallEngineer()) {
            return;
        }

        stopBattle();
    }

    @Unique
    private void stopBattle() {
        try {
            BattleEntityBehavior villager = (BattleEntityBehavior) this;
            PokemonBattle battle = Cobblemon.INSTANCE.getBattleRegistry().getBattle(villager.getBattleId());
            ServerPlayerEntity player = battle.getPlayers().get(0);
            battle.writeShowdownAction(String.format(">forcelose %s", battle.getActor(player).showdownId));
            battle.end();

        } catch (ClassCastException | NullPointerException ignored) {

        }
    }
}
