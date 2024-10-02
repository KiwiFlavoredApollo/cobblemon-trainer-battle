package kiwiapollo.cobblemontrainerbattle;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import kiwiapollo.cobblemontrainerbattle.commands.BattleFrontierCommand;
import kiwiapollo.cobblemontrainerbattle.commands.TrainerBattleCommand;
import kiwiapollo.cobblemontrainerbattle.commands.TrainerBattleFlatCommand;
import kiwiapollo.cobblemontrainerbattle.common.Config;
import kiwiapollo.cobblemontrainerbattle.common.ConfigLoader;
import kiwiapollo.cobblemontrainerbattle.common.EconomyFactory;
import kiwiapollo.cobblemontrainerbattle.economies.Economy;
import kiwiapollo.cobblemontrainerbattle.events.BattleVictoryEventHandler;
import kiwiapollo.cobblemontrainerbattle.events.LootDroppedEventHandler;
import kiwiapollo.cobblemontrainerbattle.exceptions.LoadingTrainerFileFailedException;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerFile;
import kotlin.Unit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CobblemonTrainerBattle implements ModInitializer {
	public static final String NAMESPACE = "cobblemontrainerbattle";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
	public static final Config CONFIG = ConfigLoader.load();
	public static final Economy ECONOMY = EconomyFactory.create(CONFIG.economy);

	public static Map<UUID, PokemonBattle> trainerBattles = new HashMap<>();
	public static Map<Identifier, TrainerFile> trainerFiles = new HashMap<>();
	public static JsonObject defaultTrainerConfiguration = new JsonObject();
	public static Map<Identifier, JsonObject> battleStreaks = new HashMap<>();

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(new TrainerBattleCommand());
			dispatcher.register(new TrainerBattleFlatCommand());
			dispatcher.register(new BattleFrontierCommand());
		});

		CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, battleVictoryEvent -> {
			// BATTLE_VICTORY event fires even if the player loses
			LOGGER.info("BATTLE_VICTORY event");
			new BattleVictoryEventHandler().run(battleVictoryEvent);
			return Unit.INSTANCE;
        });

		CobblemonEvents.LOOT_DROPPED.subscribe(Priority.HIGHEST, lootDroppedEvent -> {
			// LOOT_DROPPED event fires before BATTLE_VICTORY event
			// Cobblemon Discord, Hiroku: It's only used if the player kills the pokemon by hand, not by battle
			// However Pokemons drop loot when defeated in battles, at least on 1.5.1
			LOGGER.info("LOOT_DROPPED event");
			new LootDroppedEventHandler().run(lootDroppedEvent);
			return Unit.INSTANCE;
        });

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			trainerBattles.get(handler.getPlayer().getUuid()).end();
		});

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return Identifier.of(NAMESPACE, "resourceloader");
			}

			@Override
			public void reload(ResourceManager resourceManager) {
				try (InputStream inputStream = resourceManager.getResourceOrThrow(
						Identifier.of(NAMESPACE, "configuration/defaults.json")).getInputStream()) {
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					defaultTrainerConfiguration = new Gson().fromJson(bufferedReader, JsonObject.class);

				} catch (IOException e) {
                    throw new RuntimeException(e);
                }

                List<String> trainerGroups = List.of(
						"radicalred",
						"inclementemerald",
						"custom"
				);

				trainerFiles.clear();
				for (String trainerGroup : trainerGroups) {
					resourceManager.findResources(trainerGroup, path -> path.toString().endsWith(".json"))
							.forEach((identifier, resource) -> {
								try {
									JsonArray pokemons = loadTrainerPokemons(resource);
									JsonObject configuration = loadTrainerConfiguration(resourceManager, identifier);
									trainerFiles.put(toTrainerIdentifier(identifier), new TrainerFile(pokemons, configuration));

								} catch (LoadingTrainerFileFailedException e) {
									LOGGER.error(String.format(
											"Error occurred while loading trainer file %s", identifier.toString()));
								}
							});
					LOGGER.info(String.format("Loaded %s trainers", trainerGroup));
				}
			}
		});
	}

	private JsonObject loadTrainerConfiguration(ResourceManager resourceManager, Identifier identifier) {
		try (InputStream inputStream = resourceManager
				.getResourceOrThrow(toTrainerConfigurationFileIdentifier(identifier))
				.getInputStream()) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			return new Gson().fromJson(bufferedReader, JsonObject.class);

		} catch (IOException e) {
			return defaultTrainerConfiguration;
		}
	}

	private JsonArray loadTrainerPokemons(Resource resource) throws LoadingTrainerFileFailedException {
		try (InputStream inputStream = resource.getInputStream()) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			return new Gson().fromJson(bufferedReader, JsonArray.class);

		} catch (IOException | JsonSyntaxException e) {
			throw new LoadingTrainerFileFailedException();
		}
	}

	private Identifier toTrainerIdentifier(Identifier resourceIdentifier) {
		String namespace = Paths.get(resourceIdentifier.getPath()).getParent().toString();
		String path = Paths.get(resourceIdentifier.getPath()).getFileName().toString();
		return Identifier.of(namespace, path);
	}

	private Identifier toTrainerConfigurationFileIdentifier(Identifier trainerFileIdentifier) {
		Path path = Paths.get("configuration", trainerFileIdentifier.getPath());
		return  Identifier.of(NAMESPACE, path.toString().replace("\\", "/"));
	}
}