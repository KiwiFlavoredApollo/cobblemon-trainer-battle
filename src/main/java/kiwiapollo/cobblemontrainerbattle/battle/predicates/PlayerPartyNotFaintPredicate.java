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

public class PlayerPartyNotFaintPredicate<T> implements MessagePredicate<T> {
    private final Function<T, PartyStore> getPlayerParty;

    public PlayerPartyNotFaintPredicate() {
        this(player -> Cobblemon.INSTANCE.getStorage().getParty((ServerPlayerEntity) player));
    }

    public PlayerPartyNotFaintPredicate(Function<T, PartyStore> getPlayerParty) {
        this.getPlayerParty = getPlayerParty;
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.fainted_player_party");
    }

    @Override
    public boolean test(T player) {
        return getPlayerParty.apply(player).toGappyList().stream().filter(Objects::nonNull).anyMatch(Predicate.not(Pokemon::isFainted));
    }
}
