package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.storage.party.PartyStore;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicate.*;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemonParser;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerPreset;

import java.util.*;

public class NormalLevelTrainer extends AbstractTrainerBattleParticipant {
    private final List<MessagePredicate<PlayerBattleParticipant>> predicates;

    public NormalLevelTrainer(String id, TrainerPreset preset, List<ShowdownPokemon> team) {
        super(id, preset, toPartyStore(team));
        this.predicates = List.of(
                new RematchAllowedPredicate(id, preset.isRematchAllowed),

                new MaximumPartySizePredicate.PlayerPredicate(preset.maximumPartySize),
                new MinimumPartySizePredicate.PlayerPredicate(preset.minimumPartySize),

                new MaximumPartyLevelPredicate(preset.maximumPartyLevel),
                new MinimumPartyLevelPredicate(preset.minimumPartyLevel),

                new RequiredLabelPredicate(preset.requiredLabel),
                new RequiredPokemonPredicate(preset.requiredPokemon),
                new RequiredHeldItemPredicate(preset.requiredHeldItem),
                new RequiredAbilityPredicate(preset.requiredAbility),
                new RequiredMovePredicate(preset.requiredMove),

                new ForbiddenLabelPredicate(preset.forbiddenLabel),
                new ForbiddenPokemonPredicate(preset.forbiddenPokemon),
                new ForbiddenHeldItemPredicate(preset.forbiddenHeldItem),
                new ForbiddenAbilityPredicate(preset.forbiddenAbility),
                new ForbiddenMovePredicate(preset.forbiddenMove)
        );
    }

    private static PartyStore toPartyStore(List<ShowdownPokemon> team) {
        PartyStore party = new PartyStore(UUID.randomUUID());
        for (ShowdownPokemon showdownPokemon : team) {
            try {
                party.add(new ShowdownPokemonParser().toCobblemonPokemon(showdownPokemon));
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
}
