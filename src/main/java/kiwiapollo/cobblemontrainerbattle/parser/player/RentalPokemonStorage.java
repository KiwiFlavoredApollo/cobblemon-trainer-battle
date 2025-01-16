package kiwiapollo.cobblemontrainerbattle.parser.player;

import com.cobblemon.mod.common.api.storage.party.PartyStore;

public interface RentalPokemonStorage {
    PartyStore getRentalPokemon();

    void setRentalPokemon(PartyStore pokemon);

    void clearRentalPokemon();

    PartyStore getTradablePokemon();

    void setTradablePokemon(PartyStore pokemon);

    void clearTradablePokemon();
}
