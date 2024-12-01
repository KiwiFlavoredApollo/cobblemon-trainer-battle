package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.DisposableBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.PlayerBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.*;
import kiwiapollo.cobblemontrainerbattle.common.Generation5AI;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.parser.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.parser.pokemon.ShowdownPokemonParser;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerProfileStorage;
import kiwiapollo.cobblemontrainerbattle.battle.postbattle.DefeatActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.battle.postbattle.VictoryActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerProfile;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class NormalBattleTrainer implements TrainerBattleParticipant {
    private final Identifier identifier;
    private final UUID uuid;
    private final ServerPlayerEntity player;
    private final BattleFormat battleFormat;
    private final BattleAI battleAI;
    private final VictoryActionSetHandler onVictory;
    private final DefeatActionSetHandler onDefeat;
    private final SoundEvent battleTheme;
    private final List<MessagePredicate<PlayerBattleParticipant>> predicates;

    private PartyStore party;

    public NormalBattleTrainer(Identifier identifier, ServerPlayerEntity player) {
        this.identifier = identifier;
        this.uuid = UUID.randomUUID();
        this.player = player;

        TrainerProfile profile = TrainerProfileStorage.getProfileRegistry().get(identifier);
        this.party = showdownTeamToParty(profile.team(), player);
        this.battleFormat = BattleFormat.Companion.getGEN_9_SINGLES();
        this.battleAI = new Generation5AI();
        this.onVictory = new VictoryActionSetHandler(player, profile.onVictory());
        this.onDefeat = new DefeatActionSetHandler(player, profile.onDefeat());
        this.battleTheme = profile.battleTheme();
        this.predicates = List.of(
                new RematchAllowedPredicate(identifier, profile.isRematchAllowed()),
                new MaximumPartySizePredicate.PlayerPredicate(profile.maximumPartySize()),
                new MinimumPartySizePredicate.PlayerPredicate(profile.minimumPartySize()),
                new MaximumPartyLevelPredicate(profile.maximumPartyLevel()),
                new MinimumPartyLevelPredicate(profile.minimumPartyLevel()),
                new RequiredLabelExistPredicate(profile.requiredLabel()),
                new RequiredPokemonExistPredicate(profile.requiredPokemon()),
                new RequiredHeldItemExistPredicate(profile.requiredHeldItem()),
                new RequiredAbilityExistPredicate(profile.requiredAbility()),
                new RequiredMoveExistPredicate(profile.requiredMove()),
                new ForbiddenLabelNotExistPredicate(profile.forbiddenLabel()),
                new ForbiddenPokemonNotExistPredicate(profile.forbiddenPokemon()),
                new ForbiddenHeldItemNotExistPredicate(profile.forbiddenHeldItem()),
                new ForbiddenAbilityNotExistPredicate(profile.forbiddenAbility()),
                new ForbiddenMoveNotExistPredicate(profile.forbiddenMove())
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
    public BattleFormat getBattleFormat() {
        return battleFormat;
    }

    @Override
    public BattleAI getBattleAI() {
        return battleAI;
    }

    @Override
    public List<MessagePredicate<PlayerBattleParticipant>> getPredicates() {
        return predicates;
    }

    @Override
    public Optional<SoundEvent> getBattleTheme() {
        return Optional.ofNullable(battleTheme);
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
