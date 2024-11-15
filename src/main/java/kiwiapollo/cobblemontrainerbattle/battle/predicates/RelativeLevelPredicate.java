package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemonParser;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class RelativeLevelPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final MinimumPartyLevelPredicate predicate;

    public RelativeLevelPredicate() {
        this.predicate = new MinimumPartyLevelPredicate(ShowdownPokemonParser.RELATIVE_LEVEL_THRESHOLD);
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.relative_level");
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        return this.predicate.test(player);
    }
}
