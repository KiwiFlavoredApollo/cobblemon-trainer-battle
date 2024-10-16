package kiwiapollo.cobblemontrainerbattle.battleparticipants;

import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import kiwiapollo.cobblemontrainerbattle.common.Generation5AI;
import kiwiapollo.cobblemontrainerbattle.common.Trainer;
import kiwiapollo.cobblemontrainerbattle.exceptions.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.parsers.SmogonPokemon;
import kiwiapollo.cobblemontrainerbattle.parsers.SmogonPokemonParser;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FlatBattleTrainer implements TrainerBattleParticipant {
    private final UUID uuid;
    private final String name;
    private final BattleCondition battleCondition;

    private PartyStore party;


    public FlatBattleTrainer(Trainer trainer, int level) {
        this.uuid = UUID.randomUUID();
        this.name = trainer.name();
        this.battleCondition = trainer.condition();
        this.party = toParty(trainer.pokemons(), level);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public BattleAI getBattleAI() {
        return new Generation5AI();
    }

    @Override
    public BattleCondition getBattleCondition() {
        return battleCondition;
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

    private static PartyStore toParty(List<SmogonPokemon> pokemons, int level) {
        SmogonPokemonParser parser = new SmogonPokemonParser(level);

        PartyStore party = new PartyStore(UUID.randomUUID());
        for (SmogonPokemon smogonPokemon : pokemons) {
            try {
                party.add(parser.toCobblemonPokemon(smogonPokemon));
            } catch (PokemonParseException ignored) {

            }
        }

        party.toGappyList().stream().filter(Objects::nonNull).forEach(Pokemon::heal);
        party.toGappyList().stream().filter(Objects::nonNull).forEach(pokemon -> pokemon.setLevel(level));

        return party;
    }
}
