package kiwiapollo.cobblemontrainerbattle.battle.predicate;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class RequiredMovePredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final List<String> required;

    private String error;

    public RequiredMovePredicate(List<String> required) {
        this.required = required.stream().filter(Objects::nonNull).toList();
        this.error = null;
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.required_move", error);
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        Set<String> party = player.getParty().toGappyList().stream()
                .filter(Objects::nonNull)
                .map(Pokemon::getMoveSet)
                .map(MoveSet::getMoves)
                .flatMap(List::stream)
                .map(Move::getName)
                .collect(Collectors.toSet());

        for (String r : required) {
            if (!party.contains(r)) {
                error = r;
                return false;
            }
        }
        return true;
    }
}
