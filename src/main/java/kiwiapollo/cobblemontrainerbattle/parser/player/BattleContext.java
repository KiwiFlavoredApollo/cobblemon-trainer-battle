package kiwiapollo.cobblemontrainerbattle.parser.player;

import com.cobblemon.mod.common.api.storage.party.PartyStore;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.NullTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;

import java.util.UUID;

public class BattleContext implements TrainerBattleStorage, RentalPokemonStorage {
    private TrainerBattle trainerBattle;
    private PartyStore rentalPokemon;
    private PartyStore tradablePokemon;

    public BattleContext() {
        this.trainerBattle = new NullTrainerBattle();
        this.rentalPokemon = new PartyStore(UUID.randomUUID());
        this.tradablePokemon = new PartyStore(UUID.randomUUID());
    }

    @Override
    public PartyStore getRentalPokemon() {
        return rentalPokemon;
    }

    @Override
    public void setRentalPokemon(PartyStore pokemon) {
        this.rentalPokemon = pokemon;
    }

    @Override
    public void clearRentalPokemon() {
        this.rentalPokemon = new PartyStore(UUID.randomUUID());
    }

    @Override
    public TrainerBattle getTrainerBattle() {
        return trainerBattle;
    }

    @Override
    public void setTrainerBattle(TrainerBattle battle) {
        this.trainerBattle = battle;
    }

    @Override
    public void clearTrainerBattle() {
        this.trainerBattle = new NullTrainerBattle();
    }

    @Override
    public PartyStore getTradablePokemon() {
        return tradablePokemon;
    }

    @Override
    public void setTradablePokemon(PartyStore pokemon) {
        this.tradablePokemon = pokemon;
    }

    @Override
    public void clearTradablePokemon() {
        this.tradablePokemon = new PartyStore(UUID.randomUUID());
    }
}
