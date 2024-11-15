package kiwiapollo.cobblemontrainerbattle.predicates;

import kiwiapollo.cobblemontrainerbattle.session.PokemonTradeFeature;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class PlayerNotTradedPokemonPredicate<T extends PokemonTradeFeature> implements MessagePredicate<T> {
    @Override
    public MutableText getMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.player_not_trade_pokemon");
    }

    @Override
    public boolean test(T session) {
        return !session.isTradedPokemon();
    }
}
