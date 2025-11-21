package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicate.*;
import kiwiapollo.cobblemontrainerbattle.battle.preset.FlatLevelBattlePreset;
import kiwiapollo.cobblemontrainerbattle.global.preset.PokemonLevelPair;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplate;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;

import java.util.List;
import java.util.UUID;

public class FlatLevelTrainer extends AbstractTrainerBattleParticipant {
    private final List<MessagePredicate<PlayerBattleParticipant>> predicates;

    public FlatLevelTrainer(TrainerTemplate template) {
        super(template, toPartyStore(template.getTeam()));
        this.predicates = List.of(
                template.getRematchAllowedPredicate(),
                template.getCooldownElapsedPredicate(),

                template.getMaximumPartySizePredicate(),
                template.getMinimumPartySizePredicate(),

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
            pokemon.setLevel(FlatLevelBattlePreset.LEVEL);
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
        return LevelMode.FLAT;
    }
}
