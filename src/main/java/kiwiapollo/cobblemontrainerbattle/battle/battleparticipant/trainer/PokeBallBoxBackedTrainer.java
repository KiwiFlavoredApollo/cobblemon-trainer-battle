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
import kiwiapollo.cobblemontrainerbattle.block.PokeBallBoxBlockEntity;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerTexture;
import kiwiapollo.cobblemontrainerbattle.item.StoredPokeBall;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.*;

public class PokeBallBoxBackedTrainer implements TrainerBattleParticipant {
    private final PartyStore party;
    private final UUID uuid;

    public PokeBallBoxBackedTrainer(PokeBallBoxBlockEntity block) {
        this.party = toPartyStore(getPokemon(block));
        this.uuid = UUID.randomUUID();

        if (party.occupied() == 0){
            throw new IllegalStateException();
        }
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

        for (ItemStack stack : getStoredPokeBallItemStacks(block)) {
            try {
                pokemon.add(StoredPokeBall.getPokemon(stack));

            } catch (NullPointerException | IllegalStateException ignored) {

            }
        }

        return pokemon;
    }

    private static List<ItemStack> getStoredPokeBallItemStacks(PokeBallBoxBlockEntity block) {
        return getItemStacks(block).stream().filter(stack -> stack.getItem() instanceof StoredPokeBall).toList();
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

    }

    @Override
    public Identifier getTexture() {
        return TrainerTexture.RED.getIdentifier();
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public PartyStore getParty() {
        return party;
    }
}
