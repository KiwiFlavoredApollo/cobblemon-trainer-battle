package kiwiapollo.cobblemontrainerbattle.mixin;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.NormalLevelPlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.PokeBallBoxBackedTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.EntityBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.block.PokeBallBoxBlockEntity;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import kiwiapollo.cobblemontrainerbattle.villager.PokeBallEngineerVillager;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
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
        if (!isTrainerVillager()) {
            return;
        }

        startTrainerBattle(player, hand, callbackInfo);
    }

    private boolean isTrainerVillager() {
        try {
            VillagerEntity villager = (VillagerEntity) (Object) this;
            return villager.getVillagerData().getProfession().equals(PokeBallEngineerVillager.PROFESSION);

        } catch (ClassCastException e) {
            return false;
        }
    }

    private void startTrainerBattle(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo) {
        try {
            PokeBallBoxBlockEntity block = getPokeBallBoxBlockEntity(player.getWorld());

            TrainerBattle trainerBattle = new EntityBackedTrainerBattle(
                    new NormalLevelPlayer((ServerPlayerEntity) player),
                    new PokeBallBoxBackedTrainer(block),
                    (VillagerEntity) (Object) this
            );
            trainerBattle.start();

            BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setTrainerBattle(trainerBattle);
            callbackInfo.setReturnValue(ActionResult.SUCCESS);
            callbackInfo.cancel();

        } catch (ClassCastException | NoSuchElementException | BattleStartException ignored) {

        }
    }

    private PokeBallBoxBlockEntity getPokeBallBoxBlockEntity(World world) {
        VillagerEntity villager = (VillagerEntity) (Object) this;

        BlockPos pos = villager.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE).map(GlobalPos::getPos).get();
        BlockEntity entity = world.getBlockEntity(pos);
        CobblemonTrainerBattle.LOGGER.info("{} at {}", entity, pos);

        return (PokeBallBoxBlockEntity) entity;
    }
}
