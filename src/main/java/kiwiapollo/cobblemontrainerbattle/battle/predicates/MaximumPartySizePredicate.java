package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.battles.BattleFormat;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class MaximumPartySizePredicate implements MessagePredicate<Integer> {
    private final int required;

    private MaximumPartySizePredicate(int required) {
        this.required = required;
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.maximum_party_size", required);
    }

    @Override
    public boolean test(Integer size) {
        return size <= required;
    }

    public static class PlayerPredicate implements MessagePredicate<PlayerBattleParticipant> {
        private final MaximumPartySizePredicate predicate;

        public PlayerPredicate(BattleFormat format) {
            this(format.getBattleType().getSlotsPerActor());
        }

        public PlayerPredicate(int required) {
            this.predicate = new MaximumPartySizePredicate(required);
        }

        @Override
        public boolean test(PlayerBattleParticipant player) {
            return predicate.test(player.getParty().occupied());
        }

        @Override
        public MutableText getErrorMessage() {
            return predicate.getErrorMessage();
        }
    }
}
