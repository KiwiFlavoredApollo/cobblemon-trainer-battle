package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.SafeCopyBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicate.*;
import kiwiapollo.cobblemontrainerbattle.battle.preset.RelativeLevelBattlePreset;
import kiwiapollo.cobblemontrainerbattle.global.preset.PokemonLevelPair;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplate;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RelativeLevelTrainer extends AbstractTrainerBattleParticipant {
    private final List<MessagePredicate<PlayerBattleParticipant>> predicates;

    public RelativeLevelTrainer(TrainerTemplate template) {
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
            pokemon.setLevel(RelativeLevelBattlePreset.PIVOT + level);
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
        return level - RelativeLevelBattlePreset.PIVOT;
    }

    private int getMaximumPlayerLevel(ServerPlayerEntity player) {
        return Cobblemon.INSTANCE.getStorage().getParty(player).toGappyList().stream().filter(Objects::nonNull).map(Pokemon::getLevel).max(Integer::compare).orElseThrow();
    }
}
