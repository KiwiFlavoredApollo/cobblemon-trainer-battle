package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.parser.history.BattleRecord;
import kiwiapollo.cobblemontrainerbattle.parser.history.PlayerHistoryStorage;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class RematchAllowedPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final String id;
    private final boolean isRematchAllowed;

    public RematchAllowedPredicate(String id, boolean isRematchAllowed) {
        this.id = id;
        this.isRematchAllowed = isRematchAllowed;
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.is_rematch_allowed");
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        try {
            boolean isOpponentDefeated = ((BattleRecord) PlayerHistoryStorage.getInstance().getOrCreate(player.getUuid()).getOrCreate(id)).getVictoryCount() > 0;
            return isRematchAllowed || !isOpponentDefeated;

        } catch (ClassCastException e) {
            return false;
        }
    }
}
