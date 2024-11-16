package kiwiapollo.cobblemontrainerbattle.battle.predicates;

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

public class RequiredMoveExistPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final List<String> required;

    public RequiredMoveExistPredicate(List<String> required) {
        this.required = required.stream().filter(Objects::nonNull).toList();
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.literal("");
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        if (required.isEmpty()) {
            return true;
        }

        Set<String> moves = player.getParty().toGappyList().stream()
                .filter(Objects::nonNull)
                .map(Pokemon::getMoveSet)
                .map(MoveSet::getMoves)
                .flatMap(List::stream)
                .map(Move::getName)
                .collect(Collectors.toSet());

        for (String p : moves) {
            for (String r : required) {
                boolean isMoveEqual = p.equals(r);

                if (isMoveEqual) {
                    return true;
                }
            }
        }
        return false;
    }
}
