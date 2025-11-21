package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicate.*;
import kiwiapollo.cobblemontrainerbattle.battle.preset.RelativeLevelBattlePreset;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.global.preset.PokemonLevelPair;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplate;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemonParser;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerPreset;

import java.util.*;

public class NormalLevelTrainer extends AbstractTrainerBattleParticipant {
    private final List<MessagePredicate<PlayerBattleParticipant>> predicates;

    public NormalLevelTrainer(TrainerTemplate template) {
        super(template, toPartyStore(template.getTeam()));
        this.predicates = List.of(
                template.getRematchAllowedPredicate(),
                template.getCooldownElapsedPredicate(),

                template.getMaximumPartySizePredicate(),
                template.getMinimumPartySizePredicate(),

                template.getMaximumPartyLevelPredicate(),
                template.getMinimumPartyLevelPredicate(),

                template.getRequiredLabelPredicate(),
                template.getRequiredPokemonPredicate(),
                template.getRequiredHeldItemPredicate(),
                template.getRequiredAbilityPredicate(),
                template.getRequiredMovePredicate(),

                template.getForbiddenLabelPredicate(),
                template.getForbiddenPokemonPredicate(),
                template.getForbiddenHeldItemPredicate(),
                template.getForbiddenAbilityPredicate(),
                template.getForbiddenMovePredicate()
        );
    }

    private static PartyStore toPartyStore(List<PokemonLevelPair> team) {
        PartyStore party = new PartyStore(UUID.randomUUID());

        for (PokemonLevelPair pair : team) {
            Pokemon pokemon = pair.getPokemon().clone(true, true);
            int level = pair.getLevel();
            pokemon.setLevel(level);
            party.add(pokemon);
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
