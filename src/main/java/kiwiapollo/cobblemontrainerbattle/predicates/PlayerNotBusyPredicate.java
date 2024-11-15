package kiwiapollo.cobblemontrainerbattle.predicates;

import com.cobblemon.mod.common.Cobblemon;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.function.Function;

public class PlayerNotBusyPredicate<T> implements MessagePredicate<T> {
    private final Function<T, ServerPlayerEntity> toServerPlayerEntity;

    public PlayerNotBusyPredicate() {
        this(player -> {
            try {
                return (ServerPlayerEntity) player;
            } catch (ClassCastException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public PlayerNotBusyPredicate(Function<T, ServerPlayerEntity> toServerPlayerEntity) {
        this.toServerPlayerEntity = toServerPlayerEntity;
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.player_not_busy");
    }

    @Override
    public boolean test(T player) {
        return Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(toServerPlayerEntity.apply(player)) == null;
    }
}
