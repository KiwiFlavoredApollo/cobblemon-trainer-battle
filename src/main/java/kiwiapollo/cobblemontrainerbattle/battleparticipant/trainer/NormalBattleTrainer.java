package kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.battleactor.DisposableBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.battleactor.PlayerBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import kiwiapollo.cobblemontrainerbattle.common.Generation5AI;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemonParser;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerProfileStorage;
import kiwiapollo.cobblemontrainerbattle.postbattle.DefeatActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.postbattle.VictoryActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.predicates.MaximumPartyLevelPredicate;
import kiwiapollo.cobblemontrainerbattle.predicates.MessagePredicate;
import kiwiapollo.cobblemontrainerbattle.predicates.MinimumPartyLevelPredicate;
import kiwiapollo.cobblemontrainerbattle.predicates.RematchAllowedPredicate;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerProfile;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NormalBattleTrainer implements TrainerBattleParticipant {
    private final Identifier identifier;
    private final UUID uuid;
    private final ServerPlayerEntity player;
    private final VictoryActionSetHandler onVictory;
    private final DefeatActionSetHandler onDefeat;
    private final List<MessagePredicate<PlayerBattleParticipant>> predicates;

    private PartyStore party;

    public NormalBattleTrainer(Identifier identifier, ServerPlayerEntity player) {
        this.identifier = identifier;
        this.uuid = UUID.randomUUID();
        this.player = player;

        TrainerProfile profile = TrainerProfileStorage.getProfileRegistry().get(identifier);
        this.party = showdownTeamToParty(profile.team(), player);
        this.onVictory = new VictoryActionSetHandler(player, profile.onVictory());
        this.onDefeat = new DefeatActionSetHandler(player, profile.onDefeat());
        this.predicates = List.of(
                new RematchAllowedPredicate(identifier, profile.isRematchAllowed()),
                new MaximumPartyLevelPredicate(profile.maximumPartyLevel()),
                new MinimumPartyLevelPredicate(profile.minimumPartyLevel())
        );
    }

    private static PartyStore showdownTeamToParty(List<ShowdownPokemon> pokemons, ServerPlayerEntity player) {
        ShowdownPokemonParser parser = new ShowdownPokemonParser(player);

        PartyStore party = new PartyStore(UUID.randomUUID());
        for (ShowdownPokemon showdownPokemon : pokemons) {
            try {
                party.add(parser.toCobblemonPokemon(showdownPokemon));
            } catch (PokemonParseException ignored) {

            }
        }

        return party;
    }

    @Override
    public String getName() {
        return TrainerProfileStorage.getProfileRegistry().get(identifier).name();
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
    public SoundEvent getBattleTheme() {
        return TrainerProfileStorage.getProfileRegistry().get(identifier).battleTheme();
    }

    @Override
    public List<MessagePredicate<PlayerBattleParticipant>> getPredicates() {
        return predicates;
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
        onDefeat.run();
    }

    @Override
    public void onDefeat() {
        onVictory.run();
    }

    @Override
    public PartyStore getParty() {
        return party;
    }

    @Override
    public List<BattlePokemon> getBattleTeam() {
        return party.toGappyList().stream().filter(Objects::nonNull).map(DisposableBattlePokemonFactory::create).toList();
    }
}
