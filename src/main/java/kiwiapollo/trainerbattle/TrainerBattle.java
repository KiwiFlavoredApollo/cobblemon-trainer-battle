package kiwiapollo.trainerbattle;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kiwiapollo.trainerbattle.commands.CreateTrainerBattleCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class TrainerBattle implements ModInitializer {
	public static String NAMESPACE = "trainerbattle";

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			LiteralArgumentBuilder<ServerCommandSource> rootCommand = CommandManager.literal(NAMESPACE);

			rootCommand.then(new CreateTrainerBattleCommand());

			dispatcher.register(rootCommand);
		});
	}
}