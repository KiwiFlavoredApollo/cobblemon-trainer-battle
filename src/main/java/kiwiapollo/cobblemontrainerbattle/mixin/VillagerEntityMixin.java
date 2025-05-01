package kiwiapollo.cobblemontrainerbattle.mixin;

import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.NormalLevelPlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.QuickTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.EntityBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.block.TrainerTableBlockEntity;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.entity.RandomSpawnableTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;
import kiwiapollo.cobblemontrainerbattle.item.OccupiedPokeBall;
import kiwiapollo.cobblemontrainerbattle.villager.TrainerVillager;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
            return villager.getVillagerData().getProfession().equals(TrainerVillager.PROFESSION);

        } catch (ClassCastException e) {
            return false;
        }
    }

    private void startTrainerBattle(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo) {
        try {
            TrainerTableBlockEntity block = getTrainerTableBlockEntity(player.getWorld());
            List<Pokemon> pokemon = getPokemon(block);
            PartyStore party = toPartyStore(pokemon);

            TrainerBattle trainerBattle = new EntityBackedTrainerBattle(
                    new NormalLevelPlayer((ServerPlayerEntity) player),
                    new QuickTrainer(party),
                    (VillagerEntity) (Object) this
            );
            trainerBattle.start();

            BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setTrainerBattle(trainerBattle);
            callbackInfo.setReturnValue(ActionResult.SUCCESS);
            callbackInfo.cancel();

        } catch (ClassCastException | NoSuchElementException | BattleStartException ignored) {

        }
    }

    private List<Pokemon> getPokemon(TrainerTableBlockEntity block) {
        List<Pokemon> pokemon = new ArrayList<>();

        for (ItemStack stack : getOccupiedPokeBallItemStacks(block)) {
            try {
                pokemon.add(toPokemon(stack));

            } catch (NullPointerException | IllegalStateException ignored) {

            }
        }

        return pokemon;
    }

    private List<ItemStack> getOccupiedPokeBallItemStacks(TrainerTableBlockEntity block) {
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < block.size(); i++) {
            list.add(block.getStack(i));
        }

        return list.stream().filter(stack -> stack.getItem() instanceof OccupiedPokeBall).toList();
    }

    private Pokemon toPokemon(ItemStack stack) {
        return new Pokemon().loadFromNBT(Objects.requireNonNull(stack.getSubNbt("Pokemon"))).clone(true, true);
    }

    private PartyStore toPartyStore(List<Pokemon> pokemon) {
        PartyStore party = new PartyStore(UUID.randomUUID());

        List<Pokemon> random = new ArrayList<>(pokemon);
        Collections.shuffle(random);
        getFirstSix(pokemon).forEach(party::add);

        return party;
    }

    private List<Pokemon> getFirstSix(List<Pokemon> pokemon) {
        final int MAXIMUM = 6;

        if (pokemon.size() > MAXIMUM) {
            return pokemon.subList(0, MAXIMUM);

        } else {
            return pokemon;
        }
    }

    private TrainerTableBlockEntity getTrainerTableBlockEntity(World world) {
        VillagerEntity villager = (VillagerEntity) (Object) this;

        BlockPos pos = villager.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE).map(GlobalPos::getPos).get();
        BlockEntity entity = world.getBlockEntity(pos);
        CobblemonTrainerBattle.LOGGER.info("{} at {}", entity, pos);

        return (TrainerTableBlockEntity) entity;
    }

    private LevelMode getLevelMode(String trainer) {
        return TrainerStorage.getInstance().get(trainer).getLevelMode();
    }
}
