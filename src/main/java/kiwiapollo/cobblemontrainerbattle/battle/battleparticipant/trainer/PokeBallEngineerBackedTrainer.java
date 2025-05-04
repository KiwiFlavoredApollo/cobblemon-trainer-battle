package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.EntityBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.SafeCopyBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.Generation5AI;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicate.MessagePredicate;
import kiwiapollo.cobblemontrainerbattle.block.PokeBallBox;
import kiwiapollo.cobblemontrainerbattle.block.PokeBallBoxBlockEntity;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerTexture;
import kiwiapollo.cobblemontrainerbattle.item.FilledPokeBall;
import kiwiapollo.cobblemontrainerbattle.villager.PokeBallEngineerVillager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

import java.util.*;

public class PokeBallEngineerBackedTrainer implements TrainerBattleParticipant {
    private final PartyStore party;
    private final UUID uuid;
    private final VillagerEntity villager;

    public PokeBallEngineerBackedTrainer(VillagerEntity villager) {
        this.villager = villager;
        this.party = toPartyStore(getPokemon(getPokeBallBox(villager)));
        this.uuid = UUID.randomUUID();

        if (isPartyEmpty(this.party)){
            throw new IllegalStateException();
        }

        if (!isPokeBallEngineer(this.villager)) {
            throw new IllegalStateException();
        }
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public BattleAI getBattleAI() {
        return new Generation5AI();
    }

    @Override
    public BattleFormat getBattleFormat() {
        return BattleFormat.Companion.getGEN_9_SINGLES();
    }

    @Override
    public Optional<SoundEvent> getBattleTheme() {
        return Optional.empty();
    }

    @Override
    public LevelMode getLevelMode() {
        return LevelMode.NORMAL;
    }

    @Override
    public List<BattlePokemon> getBattleTeam(ServerPlayerEntity player) {
        List<BattlePokemon> team = getParty().toGappyList().stream().filter(Objects::nonNull).map(new SafeCopyBattlePokemonFactory()).toList();
        team.forEach(pokemon -> pokemon.getEffectedPokemon().heal());
        return team;
    }

    @Override
    public boolean isSpawningAllowed() {
        return false;
    }

    @Override
    public List<MessagePredicate<PlayerBattleParticipant>> getPredicates() {
        return List.of();
    }

    @Override
    public AIBattleActor createBattleActor(ServerPlayerEntity player) {
        return new EntityBackedTrainerBattleActor(
                getName(),
                getUuid(),
                getBattleTeam(player),
                getBattleAI(),
                getNearAttachedLivingEntity(player)
        );
    }

    @Override
    public LivingEntity getNearAttachedLivingEntity(ServerPlayerEntity player) {
        throw new NullPointerException();
    }

    @Override
    public void onPlayerDefeat(ServerPlayerEntity player) {

    }

    @Override
    public void onPlayerVictory(ServerPlayerEntity player) {
        emitRedstonePulse();
    }

    private void emitRedstonePulse() {
        try {
            final int DURATION = 20;
            World world = villager.getWorld();
            BlockPos pos = villager.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE).map(GlobalPos::getPos).get();
            BlockState state = world.getBlockState(pos);

            world.setBlockState(pos, state.with(PokeBallBox.POWERED, true), 3);
            world.updateNeighbor(pos, state.getBlock(), pos);

            world.scheduleBlockTick(pos, state.getBlock(), DURATION);

        } catch (NullPointerException | IllegalArgumentException ignored) {

        }
    }

    @Override
    public Identifier getTexture() {
        return TrainerTexture.RED.getIdentifier();
    }

    @Override
    public String getName() {
        return villager.getDisplayName().getString();
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public PartyStore getParty() {
        return party;
    }

    private static boolean isPartyEmpty(PartyStore party) {
        return party.occupied() == 0;
    }

    private static boolean isPokeBallEngineer(VillagerEntity villager) {
        try {
            return villager.getVillagerData().getProfession().equals(PokeBallEngineerVillager.PROFESSION);

        } catch (ClassCastException e) {
            return false;
        }
    }

    private static PokeBallBoxBlockEntity getPokeBallBox(VillagerEntity villager) {
        BlockPos pos = villager.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE).map(GlobalPos::getPos).get();
        BlockEntity block = villager.getWorld().getBlockEntity(pos);

        return (PokeBallBoxBlockEntity) block;
    }

    private static PartyStore toPartyStore(List<Pokemon> pokemon) {
        PartyStore party = new PartyStore(UUID.randomUUID());

        List<Pokemon> random = new ArrayList<>(pokemon);
        Collections.shuffle(random);
        getFirstSix(random).forEach(party::add);

        return party;
    }

    private static List<Pokemon> getPokemon(PokeBallBoxBlockEntity block) {
        List<Pokemon> pokemon = new ArrayList<>();

        for (ItemStack stack : getFilledPokeBalls(block)) {
            try {
                pokemon.add(FilledPokeBall.getPokemon(stack));

            } catch (NullPointerException | IllegalStateException ignored) {

            }
        }

        return pokemon;
    }

    private static List<ItemStack> getFilledPokeBalls(PokeBallBoxBlockEntity block) {
        return getItemStacks(block).stream().filter(stack -> stack.getItem() instanceof FilledPokeBall).toList();
    }

    private static List<ItemStack> getItemStacks(PokeBallBoxBlockEntity block) {
        List<ItemStack> itemStacks = new ArrayList<>();

        for (int i = 0; i < block.size(); i++) {
            itemStacks.add(block.getStack(i));
        }

        return itemStacks;
    }

    private static List<Pokemon> getFirstSix(List<Pokemon> pokemon) {
        final int MAXIMUM = 6;

        if (pokemon.size() > MAXIMUM) {
            return pokemon.subList(0, MAXIMUM);

        } else {
            return pokemon;
        }
    }
}
