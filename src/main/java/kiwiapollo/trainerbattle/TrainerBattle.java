package kiwiapollo.trainerbattle;

import kiwiapollo.trainerbattle.commands.BattleFrontierCommand;
import kiwiapollo.trainerbattle.commands.TrainerBattleCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrainerBattle implements ModInitializer {
	public static final String NAMESPACE = "trainerbattle";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(new TrainerBattleCommand());
			dispatcher.register(new BattleFrontierCommand());
		});
	}
}