package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.battles.BattleFormat;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerProfile;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerProfileStorage;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

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

    public static class TrainerPredicate implements MessagePredicate<Identifier> {
        private final MinimumPartySizePredicate predicate;

        public TrainerPredicate(int required) {
            this.predicate = new MinimumPartySizePredicate(required);
        }

        @Override
        public boolean test(Identifier trainer) {
            return predicate.test(TrainerProfileStorage.getProfileRegistry().get(trainer).team().size());
        }

        @Override
        public MutableText getErrorMessage() {
            return predicate.getErrorMessage();
        }
    }

    public static class TrainerProfilePredicate implements MessagePredicate<TrainerProfile> {
        private final MinimumPartySizePredicate predicate;

        public TrainerProfilePredicate(BattleFormat format) {
            this(format.getBattleType().getSlotsPerActor());
        }

        public TrainerProfilePredicate(int required) {
            this.predicate = new MinimumPartySizePredicate(required);
        }

        @Override
        public boolean test(TrainerProfile trainer) {
            return predicate.test(trainer.team().size());
        }

        @Override
        public MutableText getErrorMessage() {
            return predicate.getErrorMessage();
        }
    }
}
