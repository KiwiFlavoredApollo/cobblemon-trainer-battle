package kiwiapollo.cobblemontrainerbattle.battleparticipant;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactor.EntityBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import kiwiapollo.cobblemontrainerbattle.common.Generation5AI;
import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.parser.SmogonPokemon;
import kiwiapollo.cobblemontrainerbattle.parser.SmogonPokemonParser;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EntityBackedNormalBattleTrainer implements TrainerBattleParticipant {
    private final Identifier identifier;
    private final UUID uuid;
    private final TrainerEntity entity;
    private final ServerPlayerEntity player;

    private PartyStore party;

    public EntityBackedNormalBattleTrainer(Identifier identifier, TrainerEntity entity, ServerPlayerEntity player) {
        this.identifier = identifier;
        this.uuid = UUID.randomUUID();
        this.entity = entity;
        this.player = player;
        this.party = toParty(CobblemonTrainerBattle.trainerProfileRegistry.get(identifier).pokemons(), player);
    }

    @Override
    public String getName() {
        return CobblemonTrainerBattle.trainerProfileRegistry.get(identifier).name();
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public BattleAI getBattleAI() {
        return new Generation5AI();
    }

    @Override
    public BattleCondition getBattleCondition() {
        return CobblemonTrainerBattle.trainerProfileRegistry.get(identifier).condition();
    }

    @Override
    public AIBattleActor createBattleActor() {
        return new EntityBackedTrainerBattleActor(
                getName(),
                getUuid(),
                getBattleTeam(),
                getBattleAI(),
                entity
        );
    }

    @Override
    public PartyStore getParty() {
        return party;
    }

    @Override
    public void setParty(PartyStore party) {
        this.party = party;
    }

    @Override
    public List<BattlePokemon> getBattleTeam() {
        UUID leadingPokemon = party.toGappyList().stream()
                .filter(Objects::nonNull)
                .filter(pokemon -> !pokemon.isFainted())
                .findFirst().get().getUuid();

        return party.toBattleTeam(false, false, leadingPokemon);
    }

    private static PartyStore toParty(List<SmogonPokemon> pokemons, ServerPlayerEntity player) {
        SmogonPokemonParser parser = new SmogonPokemonParser(player);

        PartyStore party = new PartyStore(UUID.randomUUID());
        for (SmogonPokemon smogonPokemon : pokemons) {
            try {
                party.add(parser.toCobblemonPokemon(smogonPokemon));
            } catch (PokemonParseException ignored) {

            }
        }

        return party;
    }
}
