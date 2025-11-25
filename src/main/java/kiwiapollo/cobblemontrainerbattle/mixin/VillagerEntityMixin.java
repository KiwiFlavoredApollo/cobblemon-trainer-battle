package kiwiapollo.cobblemontrainerbattle.mixin;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.PokeBallEngineerBattle;
import kiwiapollo.cobblemontrainerbattle.block.PokeBallBoxBlockEntity;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntityBehavior;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.preset.PokemonLevelPair;
import kiwiapollo.cobblemontrainerbattle.preset.TrainerTemplate;
import kiwiapollo.cobblemontrainerbattle.item.misc.FilledPokeBall;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.villager.PokeBallEngineerVillager;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@SuppressWarnings("unused")
@Mixin(VillagerEntity.class)
public class VillagerEntityMixin implements TrainerEntityBehavior {
    private UUID battleId;

    public VillagerEntityMixin() {
        battleId = null;
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    public void interactPokeBallEngineer(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo) {
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
            if (isBusyWithPokemonBattle()) {
                return;
            }

            VillagerEntity villager = (VillagerEntity) (Object) this;
            
            TrainerTemplate trainer = new TrainerTemplateFactory(villager).create();
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

    private boolean isBusyWithPokemonBattle() {
        try {
            return Objects.nonNull(Cobblemon.INSTANCE.getBattleRegistry().getBattle(getBattleId()));

        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public UUID getBattleId() {
        return null;
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
    
    private static class TrainerTemplateFactory implements SimpleFactory<TrainerTemplate> {
        private final VillagerEntity villager;

        public TrainerTemplateFactory(VillagerEntity villager) {
            this.villager = villager;
        }
        
        @Override
        public TrainerTemplate create() {
            return new TrainerTemplate(
                    createPokemonLevelPair(villager),

                    createIdentifier(villager),
                    createDisplayName(villager),
                    createLevelMode(),
                    createBattleFormat(),
                    createBattleAI(),
                    createBattleTheme(),
                    createTexture(),
                    createEntityUuid(villager),

                    createOnVictoryCommands(),
                    createOnDefeatCommands(),

                    createCooldownInSeconds(),
                    createIsSpawningAllowed(),
                    createIsRematchAllowed(),

                    createMaximumPartySize(),
                    createMinimumPartySize(),
                    createMaximumPartyLevel(),
                    createMinimumPartyLevel(),

                    createRequiredLabel(),
                    createRequiredPokemon(),
                    createRequiredHeldItem(),
                    createRequiredAbility(),
                    createRequiredMove(),

                    createForbiddenLabel(),
                    createForbiddenPokemon(),
                    createForbiddenHeldItem(),
                    createForbiddenAbility(),
                    createForbiddenMove()
            );
        }

        private List<PokemonLevelPair> createPokemonLevelPair(VillagerEntity villager) {
            List<PokemonLevelPair> pair = new ArrayList<>();

            for (Pokemon pokemon : getTeam(villager)) {
                pair.add(new PokemonLevelPair(pokemon, pokemon.getLevel()));
            }

            return pair;
        }

        private List<Pokemon> getTeam(VillagerEntity villager) {
            return getFirstSix(getPokemon(getPokeBallBoxBlockEntity(villager)));
        }

        private List<Pokemon> getFirstSix(List<Pokemon> pokemon) {
            final int MAXIMUM = 6;

            if (pokemon.size() > MAXIMUM) {
                return pokemon.subList(0, MAXIMUM);

            } else {
                return pokemon;
            }
        }

        private List<Pokemon> getPokemon(PokeBallBoxBlockEntity block) {
            List<Pokemon> pokemon = new ArrayList<>();

            for (ItemStack stack : getFilledPokeBalls(block)) {
                try {
                    pokemon.add(FilledPokeBall.getPokemon(stack));

                } catch (NullPointerException | IllegalStateException ignored) {

                }
            }

            return pokemon;
        }

        private List<ItemStack> getFilledPokeBalls(PokeBallBoxBlockEntity block) {
            return getItemStacks(block).stream().filter(stack -> stack.getItem() instanceof FilledPokeBall).toList();
        }

        private List<ItemStack> getItemStacks(PokeBallBoxBlockEntity block) {
            List<ItemStack> itemStacks = new ArrayList<>();

            for (int i = 0; i < block.size(); i++) {
                itemStacks.add(block.getStack(i));
            }

            return itemStacks;
        }

        private PokeBallBoxBlockEntity getPokeBallBoxBlockEntity(VillagerEntity villager) {
            BlockPos pos = villager.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE).map(GlobalPos::getPos).get();
            BlockEntity block = villager.getWorld().getBlockEntity(pos);

            return (PokeBallBoxBlockEntity) block;
        }

        private Identifier createIdentifier(VillagerEntity villager) {
            return Identifier.of(CobblemonTrainerBattle.MOD_ID, "poke_ball_engineer");
        }

        private String createDisplayName(VillagerEntity villager) {
            return villager.getDisplayName().getString();
        }

        private LevelMode createLevelMode() {
            return LevelMode.NORMAL;
        }

        private BattleFormat createBattleFormat() {
            return BattleFormat.Companion.getGEN_9_SINGLES();
        }

        private String createBattleAI() {
            return "generation5";
        }

        private SoundEvent createBattleTheme() {
            return CobblemonSounds.PVN_BATTLE;
        }

        private Identifier createTexture() {
            return null;
        }

        private UUID createEntityUuid(VillagerEntity villager) {
            return villager.getUuid();
        }

        private List<String> createOnVictoryCommands() {
            return List.of();
        }

        private List<String> createOnDefeatCommands() {
            return List.of();
        }

        private long createCooldownInSeconds() {
            return 0;
        }

        private boolean createIsSpawningAllowed() {
            return false;
        }

        private boolean createIsRematchAllowed() {
            return true;
        }

        private int createMaximumPartySize() {
            return 6;
        }

        private int createMinimumPartySize() {
            return 1;
        }

        private int createMaximumPartyLevel() {
            return 100;
        }

        private int createMinimumPartyLevel() {
            return 1;
        }

        private List<String> createRequiredLabel() {
            return List.of();
        }

        private List<ShowdownPokemon> createRequiredPokemon() {
            return List.of();

        }

        private List<String> createRequiredHeldItem() {
            return List.of();

        }

        private List<String> createRequiredAbility() {
            return List.of();

        }

        private List<String> createRequiredMove() {
            return List.of();
        }
        
        private List<String> createForbiddenLabel() {
            return List.of();
        }

        private List<ShowdownPokemon> createForbiddenPokemon() {
            return List.of();

        }

        private List<String> createForbiddenHeldItem() {
            return List.of();

        }

        private List<String> createForbiddenAbility() {
            return List.of();

        }

        private List<String> createForbiddenMove() {
            return List.of();
        }
    }
}
