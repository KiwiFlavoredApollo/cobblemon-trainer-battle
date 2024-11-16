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

public class ForbiddenMoveNotExistPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final List<String> forbidden;
    private String error;

    public ForbiddenMoveNotExistPredicate(List<String> forbidden) {
        this.forbidden = forbidden.stream().filter(Objects::nonNull).toList();
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.forbidden_move_not_exist", error);
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

        for (String f : forbidden) {
            if (party.contains(f)) {
                error = f;
                return false;
            }
        }
        return true;
    }
}
