package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.battles.BattleFormat;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class PlayerPartyNotEmptyPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final MessagePredicate<PlayerBattleParticipant> predicate;

    public PlayerPartyNotEmptyPredicate() {
        int required = BattleFormat.Companion.getGEN_9_SINGLES().getBattleType().getSlotsPerActor();
        this.predicate = new MinimumPartySizePredicate.PlayerPredicate(required);
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.player_party_not_empty");
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        return predicate.test(player);
    }
}
