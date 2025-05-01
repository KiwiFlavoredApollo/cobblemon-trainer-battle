package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.ai.RandomBattleAI;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.EntityBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.SafeCopyBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicate.MessagePredicate;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class QuickTrainer implements TrainerBattleParticipant {
    private final PartyStore party;
    private final UUID uuid;

    public QuickTrainer(PartyStore party) {
        this.party = party;
        this.uuid = UUID.randomUUID();
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public BattleAI getBattleAI() {
        return new RandomBattleAI();
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
