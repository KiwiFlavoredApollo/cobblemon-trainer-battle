package kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battleactor.DisposableBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.battleactor.PlayerBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import kiwiapollo.cobblemontrainerbattle.common.Generation5AI;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.parser.ProfileRegistries;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemonParser;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;

public class BattleFactoryTrainer implements TrainerBattleParticipant {
    private final Identifier identifier;
    private final UUID uuid;
    private final ServerPlayerEntity player;

    private PartyStore party;

    public BattleFactoryTrainer(Identifier identifier, ServerPlayerEntity player, int level) {
        this.identifier = identifier;
        this.uuid = UUID.randomUUID();
        this.player = player;
        this.party = toParty(ProfileRegistries.trainer.get(identifier).team(), player, level);
    }

    @Override
    public String getName() {
        return ProfileRegistries.trainer.get(identifier).name();
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
        return ProfileRegistries.trainer.get(identifier).condition();
    }

    @Override
    public AIBattleActor createBattleActor() {
        return new PlayerBackedTrainerBattleActor(
                getName(),
                getUuid(),
                getBattleTeam(),
                getBattleAI(),
                player
        );
    }

    @Override
    public void onVictory() {

    }

    @Override
    public void onDefeat() {

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
        return party.toGappyList().stream().filter(Objects::nonNull).map(DisposableBattlePokemonFactory::create).toList();
    }

    private static PartyStore toParty(List<ShowdownPokemon> pokemons, ServerPlayerEntity player, int level) {
        ShowdownPokemonParser parser = new ShowdownPokemonParser(player);

        List<ShowdownPokemon> randomParty = new ArrayList<>(pokemons);
        Collections.shuffle(randomParty);

        PartyStore party = new PartyStore(UUID.randomUUID());
        for (ShowdownPokemon showdownPokemon : randomParty.subList(0, 3)) {
            try {
                party.add(parser.toCobblemonPokemon(showdownPokemon));
            } catch (PokemonParseException ignored) {

            }
        }

        party.toGappyList().stream().filter(Objects::nonNull).forEach(Pokemon::heal);
        party.toGappyList().stream().filter(Objects::nonNull).forEach(pokemon -> pokemon.setLevel(level));

        return party;
    }
}
