package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.PlayerBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.SafeCopyBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.*;
import kiwiapollo.cobblemontrainerbattle.common.RelativeLevelBattle;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.parser.pokemon.RelativeLevelShowdownPokemonParser;
import kiwiapollo.cobblemontrainerbattle.parser.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.parser.preset.TrainerPreset;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RelativeLevelTrainer extends AbstractTrainerBattleParticipant {
    private final List<MessagePredicate<PlayerBattleParticipant>> predicates;

    public RelativeLevelTrainer(String id, TrainerPreset preset, List<ShowdownPokemon> team) {
        super(id, preset, toPartyStore(team));
        this.predicates = List.of(
                new RematchAllowedPredicate(id, preset.isRematchAllowed),

                new MaximumPartySizePredicate.PlayerPredicate(preset.maximumPartySize),
                new MinimumPartySizePredicate.PlayerPredicate(preset.minimumPartySize),

                new MaximumPartyLevelPredicate(preset.maximumPartyLevel),
                new MinimumPartyLevelPredicate(preset.minimumPartyLevel),

                new RequiredLabelExistPredicate(preset.requiredLabel),
                new RequiredPokemonExistPredicate(preset.requiredPokemon),
                new RequiredHeldItemExistPredicate(preset.requiredHeldItem),
                new RequiredAbilityExistPredicate(preset.requiredAbility),
                new RequiredMoveExistPredicate(preset.requiredMove),

                new ForbiddenLabelNotExistPredicate(preset.forbiddenLabel),
                new ForbiddenPokemonNotExistPredicate(preset.forbiddenPokemon),
                new ForbiddenHeldItemNotExistPredicate(preset.forbiddenHeldItem),
                new ForbiddenAbilityNotExistPredicate(preset.forbiddenAbility),
                new ForbiddenMoveNotExistPredicate(preset.forbiddenMove)
        );
    }

    private static PartyStore toPartyStore(List<ShowdownPokemon> team) {
        PartyStore party = new PartyStore(UUID.randomUUID());
        for (ShowdownPokemon showdownPokemon : team) {
            try {
                party.add(new RelativeLevelShowdownPokemonParser().toCobblemonPokemon(showdownPokemon));
            } catch (PokemonParseException ignored) {

            }
        }

        return party;
    }

    @Override
    public List<MessagePredicate<PlayerBattleParticipant>> getPredicates() {
        return predicates;
    }

    @Override
    public LevelMode getLevelMode() {
        return LevelMode.NORMAL;
    }

    @Override
    public AIBattleActor createBattleActor(ServerPlayerEntity player) {
        return new PlayerBackedTrainerBattleActor(
                getName(),
                getUuid(),
                getBattleTeam(player),
                getBattleAI(),
                player
        );
    }

    @Override
    public List<BattlePokemon> getBattleTeam(ServerPlayerEntity player) {
        int pivot = getMaximumPlayerLevel(player);

        List<BattlePokemon> team = new ArrayList<>();
        getParty().forEach(pokemon -> {
            Pokemon clone = pokemon.clone(true, true);
            clone.setLevel(toAbsoluteLevel(pivot, toRelativeLevel(pokemon.getLevel())));
            team.add(new SafeCopyBattlePokemonFactory().apply(clone));
        });

        team.forEach(pokemon -> pokemon.getEffectedPokemon().heal());

        return team;
    }

    private int toAbsoluteLevel(int pivot, int relativeLevel) {
        return pivot + relativeLevel;
    }

    private int toRelativeLevel(int level) {
        return level - RelativeLevelBattle.PIVOT;
    }

    private int getMaximumPlayerLevel(ServerPlayerEntity player) {
        return Cobblemon.INSTANCE.getStorage().getParty(player).toGappyList().stream().filter(Objects::nonNull).map(Pokemon::getLevel).max(Integer::compare).orElseThrow();
    }
}
