package kiwiapollo.cobblemontrainerbattle.parser.player;

import com.cobblemon.mod.common.api.storage.party.PartyStore;

public interface RentalPokemonStorage {
    PartyStore getRentalPokemon();

    void setRentalPokemon(PartyStore pokemon);

    boolean hasRentalPokemon();

    void clearRentalPokemon();
}
