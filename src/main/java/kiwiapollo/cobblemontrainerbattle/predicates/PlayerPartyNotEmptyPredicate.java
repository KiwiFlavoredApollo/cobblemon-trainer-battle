package kiwiapollo.cobblemontrainerbattle.predicates;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.function.Function;

public class PlayerPartyNotEmptyPredicate<T> implements MessagePredicate<T> {
    private final Function<T, PlayerPartyStore> getPlayerParty;

    public PlayerPartyNotEmptyPredicate() {
        this(player -> Cobblemon.INSTANCE.getStorage().getParty((ServerPlayerEntity) player));
    }

    public PlayerPartyNotEmptyPredicate(Function<T, PlayerPartyStore> getPlayerParty) {
        this.getPlayerParty = getPlayerParty;
    }

    @Override
    public MutableText getMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.empty_player_party");
    }

    @Override
    public boolean test(T player) {
        return getPlayerParty.apply(player).toGappyList().stream().anyMatch(Objects::nonNull);
    }
}
