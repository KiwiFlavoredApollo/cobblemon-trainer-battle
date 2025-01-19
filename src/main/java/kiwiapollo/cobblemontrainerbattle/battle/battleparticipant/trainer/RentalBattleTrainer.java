package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.EntityBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.PlayerBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.SafeCopyBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicate.*;
import kiwiapollo.cobblemontrainerbattle.battle.preset.RentalBattlePreset;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.*;

public class RentalBattleTrainer implements TrainerBattleParticipant {
    private final TrainerBattleParticipant trainer;

    public RentalBattleTrainer(String trainer) {
        this.trainer = TrainerStorage.getInstance().get(trainer);
    }

    @Override
    public List<MessagePredicate<PlayerBattleParticipant>> getPredicates() {
        return trainer.getPredicates();
    }

    @Override
    public AIBattleActor createBattleActor(ServerPlayerEntity player) {
        try {
            return new EntityBackedTrainerBattleActor(
                    getName(),
                    getUuid(),
                    getBattleTeam(player),
                    getBattleAI(),
                    getNearAttachedLivingEntity(player)
            );

        } catch (ClassCastException | NullPointerException e) {
            return new PlayerBackedTrainerBattleActor(
                    getName(),
                    getUuid(),
                    getBattleTeam(player),
                    getBattleAI(),
                    player
            );
        }
    }

    @Override
    public LivingEntity getNearAttachedLivingEntity(ServerPlayerEntity player) {
        return trainer.getNearAttachedLivingEntity(player);
    }

    @Override
    public List<BattlePokemon> getBattleTeam(ServerPlayerEntity player) {
        List<BattlePokemon> team = new ArrayList<>(getParty().toGappyList().stream().filter(Objects::nonNull).map(new SafeCopyBattlePokemonFactory()).toList());
        Collections.shuffle(team);
        team = team.subList(0, RentalBattlePreset.PARTY_SIZE);
        team.forEach(pokemon -> pokemon.getEffectedPokemon().setLevel(RentalBattlePreset.LEVEL));
        team.forEach(pokemon -> pokemon.getEffectedPokemon().heal());

        return team;
    }

    @Override
    public void onPlayerDefeat(ServerPlayerEntity player) {
        trainer.onPlayerDefeat(player);
    }

    @Override
    public void onPlayerVictory(ServerPlayerEntity player) {
        trainer.onPlayerVictory(player);
        BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setTradablePokemon(toClone(getParty()));
    }

    @Override
    public Identifier getTexture() {
        return trainer.getTexture();
    }

    private PartyStore toClone(PartyStore party) {
        PartyStore clone = new PartyStore(UUID.randomUUID());
        party.forEach(pokemon -> clone.add(pokemon.clone(true, true)));
        return clone;
    }

    @Override
    public String getId() {
        return trainer.getId();
    }

    @Override
    public BattleAI getBattleAI() {
        return trainer.getBattleAI();
    }

    @Override
    public BattleFormat getBattleFormat() {
        return trainer.getBattleFormat();
    }

    @Override
    public Optional<SoundEvent> getBattleTheme() {
        return trainer.getBattleTheme();
    }

    @Override
    public LevelMode getLevelMode() {
        return LevelMode.FLAT;
    }

    @Override
    public boolean isSpawningAllowed() {
        return trainer.isSpawningAllowed();
    }

    @Override
    public String getName() {
        return trainer.getName();
    }

    @Override
    public UUID getUuid() {
        return trainer.getUuid();
    }

    @Override
    public PartyStore getParty() {
        return trainer.getParty();
    }
}
