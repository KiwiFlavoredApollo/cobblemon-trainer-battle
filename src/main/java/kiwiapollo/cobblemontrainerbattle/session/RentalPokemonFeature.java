package kiwiapollo.cobblemontrainerbattle.session;

import kiwiapollo.cobblemontrainerbattle.exception.SessionOperationException;

public interface RentalPokemonFeature {
    void showPartyPokemon();
    void rerollPartyPokemon() throws SessionOperationException;
}
