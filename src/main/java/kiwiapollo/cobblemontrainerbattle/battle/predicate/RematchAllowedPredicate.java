package kiwiapollo.cobblemontrainerbattle.battle.predicate;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.global.history.BattleRecord;
import kiwiapollo.cobblemontrainerbattle.global.history.PlayerHistoryStorage;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RematchAllowedPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final Identifier trainer;
    private final boolean isRematchAllowed;

    public RematchAllowedPredicate(Identifier trainer, boolean isRematchAllowed) {
        this.trainer = trainer;
        this.isRematchAllowed = isRematchAllowed;
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.is_rematch_allowed");
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        try {
            boolean isOpponentDefeated = ((BattleRecord) PlayerHistoryStorage.getInstance().getOrCreate(player.getUuid()).getOrCreate(trainer)).getVictoryCount() > 0;
            return isRematchAllowed || !isOpponentDefeated;

        } catch (ClassCastException e) {
            return false;
        }
    }
}
