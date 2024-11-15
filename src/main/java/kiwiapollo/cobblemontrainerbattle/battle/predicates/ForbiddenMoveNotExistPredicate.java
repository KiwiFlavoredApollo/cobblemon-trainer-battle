package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ForbiddenMoveNotExistPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final List<Move> forbidden;

    public ForbiddenMoveNotExistPredicate(List<Move> forbidden) {
        this.forbidden = forbidden.stream().filter(Objects::nonNull).toList();
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.literal("");
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        if (forbidden.isEmpty()) {
            return true;
        }

        for (Move f : forbidden) {
            for (Move p : player.getParty().toGappyList().stream().filter(Objects::nonNull).map(Pokemon::getMoveSet).map(MoveSet::getMoves).flatMap(List::stream).collect(Collectors.toSet())) {
                boolean isMoveEqual = p.getName().equals(f.getName());

                if (isMoveEqual) {
                    return false;
                }
            }
        }
        return true;
    }
}
