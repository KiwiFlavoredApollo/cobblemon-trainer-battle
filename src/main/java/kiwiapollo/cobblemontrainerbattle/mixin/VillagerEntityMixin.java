package kiwiapollo.cobblemontrainerbattle.mixin;

import com.cobblemon.mod.common.Cobblemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.NormalLevelPlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.PokeBallEngineerBackedTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.EntityBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.NullTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntityBehavior;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import kiwiapollo.cobblemontrainerbattle.villager.PokeBallEngineerVillager;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin implements TrainerEntityBehavior {
    private TrainerBattle trainerBattle;

    public VillagerEntityMixin() {
        trainerBattle = new NullTrainerBattle();
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    public void interactTrainerVillager(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo) {
        if (!isPokeBallEngineer()) {
            return;
        }

        startTrainerBattle(player, hand, callbackInfo);
    }

    private boolean isPokeBallEngineer() {
        try {
            VillagerEntity villager = (VillagerEntity) (Object) this;
            return villager.getVillagerData().getProfession().equals(PokeBallEngineerVillager.PROFESSION);

        } catch (ClassCastException e) {
            return false;
        }
    }

    private void startTrainerBattle(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo) {
        try {
            if (hasTrainerBattle()) {
                return;
            }

            VillagerEntity villager = (VillagerEntity) (Object) this;

            TrainerBattle trainerBattle = new EntityBackedTrainerBattle(
                    new NormalLevelPlayer((ServerPlayerEntity) player),
                    new PokeBallEngineerBackedTrainer(villager),
                    (VillagerEntity) (Object) this
            );
            trainerBattle.start();

            BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setTrainerBattle(trainerBattle);
            this.trainerBattle = trainerBattle;

            villager.setVelocity(0, 0, 0);
            villager.setAiDisabled(true);
            villager.velocityDirty = true;

            callbackInfo.setReturnValue(ActionResult.SUCCESS);
            callbackInfo.cancel();

        } catch (ClassCastException | NoSuchElementException | IllegalStateException | BattleStartException ignored) {

        }
    }

    private boolean hasTrainerBattle() {
        try {
            UUID battleId = trainerBattle.getBattleId();
            return Objects.nonNull(Cobblemon.INSTANCE.getBattleRegistry().getBattle(battleId));

        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public void setTrainer(String trainer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Identifier getTexture() {
        throw new NullPointerException();
    }

    @Override
    public TrainerBattle getTrainerBattle() {
        UUID battleId = trainerBattle.getBattleId();
        Objects.requireNonNull(Cobblemon.INSTANCE.getBattleRegistry().getBattle(battleId));
        return trainerBattle;
    }

    @Override
    public void onPlayerVictory() {
        setAiEnabled();
    }

    @Override
    public void onPlayerDefeat() {
        setAiEnabled();
    }

    private void setAiEnabled() {
        VillagerEntity villager = (VillagerEntity) (Object) this;
        villager.setAiDisabled(false);
    }
}
