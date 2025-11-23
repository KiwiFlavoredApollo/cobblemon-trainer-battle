package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.CustomTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.SafeCopyBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicate.*;
import kiwiapollo.cobblemontrainerbattle.battle.preset.RentalBattlePreset;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.global.context.TradePokemonStorage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;

public class RentalBattleTrainer implements TrainerBattleParticipant {
    private final TrainerBattleParticipant trainer;

    public RentalBattleTrainer(Identifier trainer) {
        this.trainer = new TrainerBattleParticipantFactory(trainer).create();
    }

    @Override
    public List<MessagePredicate<PlayerBattleParticipant>> getPredicates() {
        return trainer.getPredicates();
    }

    @Override
    public AIBattleActor createBattleActor(ServerPlayerEntity player) {
        return new CustomTrainerBattleActor(
                getName(),
                getBattleTeam(player),
                getBattleAI(),
                getEntityOrPlayer(player),
                () -> onPlayerVictory(player),
                () -> onPlayerDefeat(player)
        );
    }

    private LivingEntity getEntityOrPlayer(ServerPlayerEntity player) {
        try {
            return getEntity(player);

        } catch (NullPointerException e) {
            return player;
        }
    }

    @Override
    public LivingEntity getEntity(ServerPlayerEntity player) {
        return trainer.getEntity(player);
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
        TradePokemonStorage.getInstance().get(player).setFirst(getParty().get(1));
        TradePokemonStorage.getInstance().get(player).setSecond(getParty().get(2));
        TradePokemonStorage.getInstance().get(player).setThird(getParty().get(3));
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
    public Identifier getIdentifier() {
        return trainer.getIdentifier();
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
    public Text getName() {
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
