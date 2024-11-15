package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Objects;

public class MinimumPartyLevelPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final int minimum;

    public MinimumPartyLevelPredicate(int minimum) {
        this.minimum = minimum;
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.minimum_party_level", minimum);
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        return player.getParty().toGappyList().stream()
                .filter(Objects::nonNull)
                .map(Pokemon::getLevel)
                .allMatch(level -> level >= minimum);
    }
}
