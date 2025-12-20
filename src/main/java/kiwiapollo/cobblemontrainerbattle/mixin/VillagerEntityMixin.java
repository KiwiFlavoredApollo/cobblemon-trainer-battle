package kiwiapollo.cobblemontrainerbattle.mixin;

import com.cobblemon.mod.common.Cobblemon;
import kiwiapollo.cobblemontrainerbattle.battle.PokeBallEngineerBattle;
import kiwiapollo.cobblemontrainerbattle.entity.BattleEntityBehavior;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplate;
import kiwiapollo.cobblemontrainerbattle.villager.CustomVillagerProfession;
import kiwiapollo.cobblemontrainerbattle.villager.PokeBallEngineer;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@SuppressWarnings("unused")
@Mixin(VillagerEntity.class)
public class VillagerEntityMixin implements BattleEntityBehavior {
    private UUID battleId;

    public VillagerEntityMixin() {
        battleId = null;
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo) {
        if (!isPokeBallEngineer()) {
            return;
        }

        if (isBusyWithPokemonBattle()) {
            return;
        }

        startPokeBallEngineerBattle(player, hand, callbackInfo);
    }

    @Unique
    private boolean isPokeBallEngineer() {
        try {
            VillagerEntity villager = (VillagerEntity) (Object) this;
            return villager.getVillagerData().getProfession().equals(CustomVillagerProfession.POKE_BALL_ENGINEER);

        } catch (ClassCastException e) {
            return false;
        }
    }

    @Unique
    private boolean isBusyWithPokemonBattle() {
        try {
            return Objects.nonNull(Cobblemon.INSTANCE.getBattleRegistry().getBattle(battleId));

        } catch (NullPointerException e) {
            return false;
        }
    }

    @Unique
    private void startPokeBallEngineerBattle(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo) {
        try {
            VillagerEntity villager = (VillagerEntity) (Object) this;
            
            TrainerTemplate trainer = new PokeBallEngineer.TrainerTemplateFactory(villager).create();
            PokeBallEngineerBattle battle = new PokeBallEngineerBattle((ServerPlayerEntity) player, trainer);
            battle.start();
            this.battleId = battle.getBattleId();

            villager.setVelocity(0, 0, 0);
            villager.setAiDisabled(true);
            villager.velocityDirty = true;

            callbackInfo.setReturnValue(ActionResult.SUCCESS);
            callbackInfo.cancel();

        } catch (ClassCastException | NoSuchElementException | IllegalStateException | BattleStartException ignored) {

        }
    }

    @Override
    public UUID getBattleId() {
        return battleId;
    }

    @Override
    public void setTrainer(Identifier trainer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Identifier getTexture() {
        throw new NullPointerException();
    }

    @Override
    public void onPlayerVictory() {
        VillagerEntity villager = (VillagerEntity) (Object) this;
        villager.setAiDisabled(false);
    }

    @Override
    public void onPlayerDefeat() {
        VillagerEntity villager = (VillagerEntity) (Object) this;
        villager.setAiDisabled(false);
    }
}
