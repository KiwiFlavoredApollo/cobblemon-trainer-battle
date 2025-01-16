package kiwiapollo.cobblemontrainerbattle.battle.predicate;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.function.Predicate;

public class PlayerPartyNotFaintedPredicate implements MessagePredicate<PlayerBattleParticipant> {
    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.player_party_not_fainted");
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        return player.getParty().toGappyList().stream().filter(Objects::nonNull).anyMatch(Predicate.not(Pokemon::isFainted));
    }
}
