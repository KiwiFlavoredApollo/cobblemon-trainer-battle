package kiwiapollo.cobblemontrainerbattle.mixin;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.NormalLevelPlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.PokeBallEngineerBackedTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.EntityBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import kiwiapollo.cobblemontrainerbattle.villager.PokeBallEngineerVillager;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Debug(export = true)
@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {
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
            VillagerEntity villager = (VillagerEntity) (Object) this;

            TrainerBattle trainerBattle = new EntityBackedTrainerBattle(
                    new NormalLevelPlayer((ServerPlayerEntity) player),
                    new PokeBallEngineerBackedTrainer(villager),
                    (VillagerEntity) (Object) this
            );
            trainerBattle.start();

            BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setTrainerBattle(trainerBattle);
            callbackInfo.setReturnValue(ActionResult.SUCCESS);
            callbackInfo.cancel();

        } catch (ClassCastException | NoSuchElementException | IllegalStateException | BattleStartException ignored) {

        }
    }
}
