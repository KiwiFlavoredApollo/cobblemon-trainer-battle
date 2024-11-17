package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.parser.history.BattleRecord;
import kiwiapollo.cobblemontrainerbattle.parser.history.PlayerHistoryManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RematchAllowedPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final Identifier opponent;
    private final boolean isRematchAllowed;

    public RematchAllowedPredicate(Identifier opponent, boolean isRematchAllowed) {
        this.opponent = opponent;
        this.isRematchAllowed = isRematchAllowed;
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.is_rematch_allowed");
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        try {
            boolean isOpponentDefeated = ((BattleRecord) PlayerHistoryManager.get(player.getUuid()).get(opponent)).getVictoryCount() > 0;
            return isRematchAllowed || !isOpponentDefeated;

        } catch (ClassCastException e) {
            return false;
        }
    }
}
