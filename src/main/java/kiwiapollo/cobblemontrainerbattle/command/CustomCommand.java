package kiwiapollo.cobblemontrainerbattle.command;

import kiwiapollo.cobblemontrainerbattle.command.cobblemontrainerbattle.CobblemonTrainerBattleCommand;
import kiwiapollo.cobblemontrainerbattle.command.exportpokemon.ExportPokemonCommand;
import kiwiapollo.cobblemontrainerbattle.command.exportpokemon.ExportPokemonOtherCommand;
import kiwiapollo.cobblemontrainerbattle.command.rentalbattle.RentalBattleCommand;
import kiwiapollo.cobblemontrainerbattle.command.rentalbattle.RentalBattleOtherCommand;
import kiwiapollo.cobblemontrainerbattle.command.rentalpokemon.RentalPokemonCommand;
import kiwiapollo.cobblemontrainerbattle.command.trainerbattle.TrainerBattleCommand;
import kiwiapollo.cobblemontrainerbattle.command.trainerbattle.TrainerBattleOtherCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class CustomCommand {
    public static void initialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, environment) -> {
            dispatcher.register(new TrainerBattleCommand());
            dispatcher.register(new TrainerBattleOtherCommand());
            dispatcher.register(new RentalBattleCommand());
            dispatcher.register(new RentalBattleOtherCommand());
            dispatcher.register(new RentalPokemonCommand());
            dispatcher.register(new ExportPokemonCommand());
            dispatcher.register(new ExportPokemonOtherCommand());
            dispatcher.register(new CobblemonTrainerBattleCommand());
        });
    }
}
