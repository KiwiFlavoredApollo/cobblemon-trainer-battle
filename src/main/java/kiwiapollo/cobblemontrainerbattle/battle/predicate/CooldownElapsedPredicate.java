package kiwiapollo.cobblemontrainerbattle.battle.predicate;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.global.history.PlayerHistoryStorage;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.time.Duration;
import java.time.Instant;

public class CooldownElapsedPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final Identifier trainer;
    private final long cooldown;

    private long remains;

    public CooldownElapsedPredicate(Identifier trainer, long cooldown) {
        this.trainer = trainer;
        this.cooldown = cooldown;
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.cooldown_elapsed", remains);
    }

    private long getRemainingCooldownInSeconds(PlayerBattleParticipant player) {
        Instant timestamp = PlayerHistoryStorage.getInstance().getOrCreate(player.getUuid()).getOrCreate(trainer).getTimestamp();
        long remains = cooldown - Duration.between(timestamp, Instant.now()).toSeconds();

        if (remains > 0) {
            return remains;

        } else {
            return 0;
        }
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        remains = getRemainingCooldownInSeconds(player);
        return remains == 0;
    }
}
