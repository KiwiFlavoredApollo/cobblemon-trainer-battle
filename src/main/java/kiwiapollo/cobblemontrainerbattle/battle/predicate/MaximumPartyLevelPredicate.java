package kiwiapollo.cobblemontrainerbattle.battle.predicate;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Objects;

public class MaximumPartyLevelPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final int maximum;

    public MaximumPartyLevelPredicate(int maximum) {
        this.maximum = maximum;
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.maximum_party_level", maximum);
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        return player.getParty().toGappyList().stream()
                .filter(Objects::nonNull)
                .map(Pokemon::getLevel)
                .allMatch(level -> level <= maximum);
    }
}
