package kiwiapollo.cobblemontrainerbattle.battle.predicate;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ForbiddenLabelPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final List<String> forbidden;
    private String error;

    public ForbiddenLabelPredicate(List<String> forbidden) {
        this.forbidden = forbidden.stream().filter(Objects::nonNull).toList();
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.forbidden_label", error);
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        Set<String> party = player.getParty().toGappyList().stream()
                .filter(Objects::nonNull)
                .map(Pokemon::getSpecies)
                .map(Species::getLabels)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        for (String f : forbidden) {
            if (party.contains(f)) {
                error = f;
                return false;
            }
        }
        return true;
    }
}
