package kiwiapollo.cobblemontrainerbattle.villager;

import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.block.PokeBallBoxBlockEntity;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.item.misc.FilledPokeBall;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.template.PokemonLevelPair;
import kiwiapollo.cobblemontrainerbattle.template.PokemonType;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplate;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PokeBallEngineer {
    public static final String NAME = "pokeballengineer";

    public static class TrainerTemplateFactory {
        private final VillagerEntity villager;

        public TrainerTemplateFactory(VillagerEntity villager) {
            this.villager = villager;
        }

        public TrainerTemplate create() {
            return new TrainerTemplate(
                    createTeam(),

                    createIdentifier(),
                    createDisplayName(),
                    createLevelMode(),
                    createBattleFormat(),
                    createBattleAI(),
                    createBattleTheme(),
                    createTexture(),
                    createEntityUuid(),

                    createOnVictoryCommands(),
                    createOnDefeatCommands(),

                    createCooldownInSeconds(),
                    createIsSpawningAllowed(),
                    createIsRematchAllowed(),

                    createMaximumPartySize(),
                    createMinimumPartySize(),
                    createMaximumPartyLevel(),
                    createMinimumPartyLevel(),

                    createRequiredType(),
                    createRequiredLabel(),
                    createRequiredPokemon(),
                    createRequiredHeldItem(),
                    createRequiredAbility(),
                    createRequiredMove(),

                    createForbiddenType(),
                    createForbiddenLabel(),
                    createForbiddenPokemon(),
                    createForbiddenHeldItem(),
                    createForbiddenAbility(),
                    createForbiddenMove(),

                    createAllowedType(),
                    createAllowedLabel(),
                    createAllowedPokemon(),
                    createAllowedHeldItem(),
                    createAllowedAbility(),
                    createAllowedMove()
            );
        }

        private List<PokemonLevelPair> createTeam() {
            List<PokemonLevelPair> pair = new ArrayList<>();

            for (Pokemon pokemon : getTeam(villager)) {
                pair.add(new PokemonLevelPair(pokemon, pokemon.getLevel()));
            }

            return pair;
        }

        private List<Pokemon> getTeam(VillagerEntity villager) {
            return getFirstSix(getPokemon(getPokeBallBoxBlockEntity(villager)));
        }

        // TODO better name
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

        private Identifier createIdentifier() {
            return Identifier.of(CobblemonTrainerBattle.MOD_ID, "poke_ball_engineer");
        }

        private String createDisplayName() {
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

        private UUID createEntityUuid() {
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

        private List<PokemonType> createRequiredType() {
            return List.of();
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

        private List<PokemonType> createForbiddenType() {
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

        private List<PokemonType> createAllowedType() {
            return List.of();
        }

        private List<String> createAllowedLabel() {
            return List.of();
        }

        private List<ShowdownPokemon> createAllowedPokemon() {
            return List.of();

        }

        private List<String> createAllowedHeldItem() {
            return List.of();

        }

        private List<String> createAllowedAbility() {
            return List.of();

        }

        private List<String> createAllowedMove() {
            return List.of();
        }
    }
}
