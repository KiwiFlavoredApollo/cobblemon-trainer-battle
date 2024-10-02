package kiwiapollo.cobblemontrainerbattle;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupFile;
import kiwiapollo.cobblemontrainerbattle.commands.*;
import kiwiapollo.cobblemontrainerbattle.common.Config;
import kiwiapollo.cobblemontrainerbattle.common.ConfigLoader;
import kiwiapollo.cobblemontrainerbattle.common.EconomyFactory;
import kiwiapollo.cobblemontrainerbattle.economies.Economy;
import kiwiapollo.cobblemontrainerbattle.events.BattleVictoryEventHandler;
import kiwiapollo.cobblemontrainerbattle.events.LootDroppedEventHandler;
import kiwiapollo.cobblemontrainerbattle.exceptions.LoadingResourceFailedException;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CobblemonTrainerBattle implements ModInitializer {
	public static final String NAMESPACE = "cobblemontrainerbattle";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
	public static final Config CONFIG = ConfigLoader.load();
	public static final Economy ECONOMY = EconomyFactory.create(CONFIG.economy);

	public static Map<UUID, PokemonBattle> trainerBattles = new HashMap<>();
	public static JsonObject defaultTrainerConfiguration = new JsonObject();
	public static Map<String, TrainerFile> trainerFiles = new HashMap<>();
	public static Map<String, GroupFile> groupFiles = new HashMap<>();

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(new TrainerBattleCommand());
			dispatcher.register(new TrainerBattleFlatCommand());
			dispatcher.register(new BattleFrontierCommand());
			dispatcher.register(new BattleGroupCommand());
			dispatcher.register(new BattleGroupFlatCommand());
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
				defaultTrainerConfiguration = loadDefaultTrainerConfiguration(resourceManager);
				trainerFiles = loadTrainerFiles(resourceManager);
				groupFiles = loadGroupFiles(resourceManager);
			}
		});
	}

	private JsonObject loadDefaultTrainerConfiguration(ResourceManager resourceManager) {
		try (InputStream inputStream = resourceManager.getResourceOrThrow(
				Identifier.of(NAMESPACE, "battletrainer/defaults.json")).getInputStream()) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			return new Gson().fromJson(bufferedReader, JsonObject.class);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Map<String, TrainerFile> loadTrainerFiles(ResourceManager resourceManager) {
		List<String> namespaces = List.of(
				"radicalred",
				"inclementemerald",
				"custom"
		);

		Map<String, TrainerFile> trainerFiles = new HashMap<>();
		for (String namespace : namespaces) {
			resourceManager.findResources(namespace, this::isJsonFile)
					.forEach((identifier, resource) -> {
						try {
							// identifier: cobblemontrainerbattle:custom/custom_trainer.json
							JsonArray pokemons = loadTrainerPokemons(resource);
							JsonObject configuration = loadTrainerConfiguration(resourceManager, identifier);
							trainerFiles.put(identifier.getPath(), new TrainerFile(pokemons, configuration));

						} catch (LoadingResourceFailedException e) {
							LOGGER.error(String.format("Error occurred while loading trainer file %s", identifier.toString()));
						}
					});
			LOGGER.info(String.format("Loaded %s trainer files", namespace));
		}
		return trainerFiles;
	}

	private Map<String, GroupFile> loadGroupFiles(ResourceManager resourceManager) {
		Map<String, GroupFile> groupFiles = new HashMap<>();
		String startingPath = "battlegroup";
		resourceManager.findResources(startingPath, this::isJsonFile)
				.forEach((identifier, resource) -> {
					try {
						// identifier: cobblemontrainerbattle:battlegroup/custom_trainer_group.json
						JsonObject configuration = loadGroupConfiguration(resourceManager, identifier);
						String path = identifier.getPath().replace(String.format("%s/", startingPath), "");
						groupFiles.put(path, new GroupFile(configuration));

					} catch (LoadingResourceFailedException e) {
						LOGGER.error(String.format("Error occurred while loading group file %s", identifier.toString()));
					}
				});
		LOGGER.info("Loaded group files");
		return groupFiles;
	}


	private JsonArray loadTrainerPokemons(Resource resource) throws LoadingResourceFailedException {
		try (InputStream inputStream = resource.getInputStream()) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			return new Gson().fromJson(bufferedReader, JsonArray.class);

		} catch (IOException | JsonSyntaxException e) {
			throw new LoadingResourceFailedException();
		}
	}

	private JsonObject loadTrainerConfiguration(ResourceManager resourceManager, Identifier identifier) {
		try (InputStream inputStream = resourceManager
				.getResourceOrThrow(new Identifier(String.format("battletrainer/%s", identifier.getPath())))
				.getInputStream()) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			return new Gson().fromJson(bufferedReader, JsonObject.class);

		} catch (IOException e) {
			return defaultTrainerConfiguration;
		}
	}

	private JsonObject loadGroupConfiguration(ResourceManager resourceManager, Identifier identifier)
			throws LoadingResourceFailedException {
		try (InputStream inputStream = resourceManager
				.getResourceOrThrow(identifier)
				.getInputStream()) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			return new Gson().fromJson(bufferedReader, JsonObject.class);

		} catch (IOException e) {
			throw new LoadingResourceFailedException();
		}
	}

	private boolean isJsonFile(Identifier identifier) {
		return identifier.toString().endsWith(".json");
	}
}