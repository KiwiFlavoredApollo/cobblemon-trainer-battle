package kiwiapollo.cobblemontrainerbattle.battle.predicate;

import com.cobblemon.mod.common.battles.BattleFormat;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class MinimumPartySizePredicate implements MessagePredicate<Integer> {
    private final int required;

    private MinimumPartySizePredicate(int required) {
        this.required = required;
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.minimum_party_size", required);
    }

    @Override
    public boolean test(Integer size) {
        return size >= required;
    }

    public static class PlayerPredicate implements MessagePredicate<PlayerBattleParticipant> {
        private final MinimumPartySizePredicate predicate;

        public PlayerPredicate(BattleFormat format) {
            this(format.getBattleType().getSlotsPerActor());
        }

        public PlayerPredicate(int required) {
            this.predicate = new MinimumPartySizePredicate(required);
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

    public static class BattleFormatPredicate implements MessagePredicate<Integer> {
        private final MinimumPartySizePredicate predicate;

        public BattleFormatPredicate(BattleFormat format) {
            this(format.getBattleType().getSlotsPerActor());
        }

        public BattleFormatPredicate(int required) {
            this.predicate = new MinimumPartySizePredicate(required);
        }

        @Override
        public boolean test(Integer required) {
            return predicate.test(required);
        }

        @Override
        public MutableText getErrorMessage() {
            return predicate.getErrorMessage();
        }
    }
}
