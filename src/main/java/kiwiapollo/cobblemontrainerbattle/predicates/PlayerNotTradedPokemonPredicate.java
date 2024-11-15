package kiwiapollo.cobblemontrainerbattle.predicates;

import kiwiapollo.cobblemontrainerbattle.session.PokemonTradeFeature;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class PlayerNotTradedPokemonPredicate<T extends PokemonTradeFeature> implements MessagePredicate<T> {
    @Override
    public MutableText getMessage() {
        return Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.already_traded_pokemon");
    }

    @Override
    public boolean test(T session) {
        return !session.isTradedPokemon();
    }
}
