package kiwiapollo.cobblemontrainerbattle.predicates;

import com.cobblemon.mod.common.Cobblemon;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.function.Function;

public class PlayerNotBusyPredicate<T> implements MessagePredicate<T> {
    private final Function<T, ServerPlayerEntity> toServerPlayerEntity;

    public PlayerNotBusyPredicate(Function<T, ServerPlayerEntity> toServerPlayerEntity) {
        this.toServerPlayerEntity = toServerPlayerEntity;
    }

    @Override
    public MutableText getMessage() {
        return Text.translatable("command.cobblemontrainerbattle.trainerbattle.busy_with_pokemon_battle");
    }

    @Override
    public boolean test(T player) {
        return Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(toServerPlayerEntity.apply(player)) == null;
    }
}
