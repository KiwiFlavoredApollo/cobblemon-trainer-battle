package kiwiapollo.cobblemontrainerbattle.parser.player;

import com.cobblemon.mod.common.api.storage.party.PartyStore;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.NullTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;

import java.util.UUID;

public class BattleContext implements TrainerBattleStorage, RentalPokemonStorage {
    private final UUID uuid;
    private TrainerBattle trainerBattle;
    private PartyStore rentalPokemon;

    public BattleContext(UUID uuid) {
        this.uuid = uuid;
        this.trainerBattle = new NullTrainerBattle();
        this.rentalPokemon = new PartyStore(uuid);
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
    public boolean hasRentalPokemon() {
        return rentalPokemon.occupied() > 0;
    }

    @Override
    public void clearRentalPokemon() {
        this.rentalPokemon = new PartyStore(uuid);
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
}
