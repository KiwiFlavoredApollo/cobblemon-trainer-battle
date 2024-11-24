package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import kiwiapollo.cobblemontrainerbattle.battle.session.PokemonTradeFeature;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class PlayerNotTradedPokemonPredicate<T extends PokemonTradeFeature> implements MessagePredicate<T> {
    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.player_not_traded_pokemon");
    }

    @Override
    public boolean test(T session) {
        return !session.isPokemonTraded();
    }
}
