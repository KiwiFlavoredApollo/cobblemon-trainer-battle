package kiwiapollo.cobblemontrainerbattle.mixin;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.EntityBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.block.TrainerTableBlock;
import kiwiapollo.cobblemontrainerbattle.block.TrainerTableBlockEntity;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.entity.RandomSpawnableTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;
import kiwiapollo.cobblemontrainerbattle.villager.TrainerVillager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.NoSuchElementException;

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
            return villager.getVillagerData().getProfession().equals(TrainerVillager.PROFESSION);

        } catch (ClassCastException e) {
            return false;
        }
    }

    private void startTrainerBattle(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo) {
        try {
            TrainerTableBlockEntity block = getTrainerTableBlockEntity(player.getWorld());

            String trainer = new RandomSpawnableTrainerFactory(string -> true).create();

            TrainerBattle trainerBattle = new EntityBackedTrainerBattle(
                    new PlayerBattleParticipantFactory((ServerPlayerEntity) player, getLevelMode(trainer)).create(),
                    new TrainerBattleParticipantFactory(trainer).create(),
                    (VillagerEntity) (Object) this
            );
            trainerBattle.start();

            BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setTrainerBattle(trainerBattle);
            callbackInfo.setReturnValue(ActionResult.SUCCESS);
            callbackInfo.cancel();

        } catch (ClassCastException | NoSuchElementException | BattleStartException ignored) {

        }
    }

    private TrainerTableBlockEntity getTrainerTableBlockEntity(World world) {
        VillagerEntity villager = (VillagerEntity) (Object) this;

        BlockPos pos = villager.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE).map(GlobalPos::getPos).get();
        BlockState state = world.getBlockState(pos);
        TrainerTableBlock block = (TrainerTableBlock) state.getBlock();
        CobblemonTrainerBattle.LOGGER.info("{} at {}", block, pos);

        return (TrainerTableBlockEntity) block.createBlockEntity(pos, state);
    }

    private LevelMode getLevelMode(String trainer) {
        return TrainerStorage.getInstance().get(trainer).getLevelMode();
    }
}
