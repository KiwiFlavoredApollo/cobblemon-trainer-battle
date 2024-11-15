package kiwiapollo.cobblemontrainerbattle.session;

import kiwiapollo.cobblemontrainerbattle.exception.SessionOperationException;

public interface PokemonTradeFeature {
    void tradePokemon(int playerSlot, int trainerSlot) throws SessionOperationException;

    void showTradeablePokemon() throws SessionOperationException;

    boolean isTradedPokemon();
}
