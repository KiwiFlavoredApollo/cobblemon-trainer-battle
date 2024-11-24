package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class PlayerPartyNotFaintedPredicate<T> implements MessagePredicate<T> {
    private final Function<T, PartyStore> getPlayerParty;

    public PlayerPartyNotFaintedPredicate() {
        this(player -> Cobblemon.INSTANCE.getStorage().getParty((ServerPlayerEntity) player));
    }

    public PlayerPartyNotFaintedPredicate(Function<T, PartyStore> getPlayerParty) {
        this.getPlayerParty = getPlayerParty;
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.player_party_not_fainted");
    }

    @Override
    public boolean test(T player) {
        return getPlayerParty.apply(player).toGappyList().stream().filter(Objects::nonNull).anyMatch(Predicate.not(Pokemon::isFainted));
    }
}
