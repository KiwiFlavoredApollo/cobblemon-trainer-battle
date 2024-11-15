package kiwiapollo.cobblemontrainerbattle.predicates;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.function.Predicate;

public class PlayerPartyNotFaintPredicate implements MessagePredicate<PlayerBattleParticipant> {
    @Override
    public MutableText getMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.fainted_player_party");
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        return player.getParty().toGappyList().stream().filter(Objects::nonNull).anyMatch(Predicate.not(Pokemon::isFainted));
    }
}
