package kiwiapollo.cobblemontrainerbattle.battle.session;

import kiwiapollo.cobblemontrainerbattle.exception.SessionOperationException;

public interface PokemonTradeFeature {
    void tradePokemon(int playerSlot, int trainerSlot) throws SessionOperationException;

    void showTradablePokemon() throws SessionOperationException;

    boolean isPokemonTraded();
}
