package kiwiapollo.cobblemontrainerbattle;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import kiwiapollo.cobblemontrainerbattle.commands.BattleFrontierCommand;
import kiwiapollo.cobblemontrainerbattle.commands.TrainerBattleCommand;
import kiwiapollo.cobblemontrainerbattle.commands.TrainerBattleFlatCommand;
import kiwiapollo.cobblemontrainerbattle.common.Config;
import kiwiapollo.cobblemontrainerbattle.common.ConfigLoader;
import kiwiapollo.cobblemontrainerbattle.common.EconomyFactory;
import kiwiapollo.cobblemontrainerbattle.economies.Economy;
import kiwiapollo.cobblemontrainerbattle.events.BattleVictoryEventHandler;
import kiwiapollo.cobblemontrainerbattle.events.LootDroppedEventHandler;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerFile;
import kotlin.Unit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class CobblemonTrainerBattle implements ModInitializer {
	public static final String NAMESPACE = "cobblemontrainerbattle";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
	public static final Config CONFIG = ConfigLoader.load();
	public static final Economy ECONOMY = EconomyFactory.create(CONFIG.economy);
	public static final Map<UUID, PokemonBattle> TRAINER_BATTLES = new HashMap<>();
	public static final List<TrainerFile> RADICAL_RED_TRAINERS = new ArrayList<>();
	public static final List<TrainerFile> INCLEMENT_EMERALD_TRAINERS = new ArrayList<>();
	public static final List<TrainerFile> CUSTOM_TRAINERS = new ArrayList<>();

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
			TRAINER_BATTLES.get(handler.getPlayer().getUuid()).end();
		});

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return Identifier.of(NAMESPACE, "resourceloader");
			}

			@Override
			public void reload(ResourceManager manager) {
				RADICAL_RED_TRAINERS.clear();
				manager.findResources("radicalred", path -> path.toString().endsWith(".json"))
						.forEach((identifier, resource) -> {
							try (InputStream stream = resource.getInputStream()) {
								BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
								JsonArray jsonArray = new Gson().fromJson(bufferedReader, JsonArray.class);
								RADICAL_RED_TRAINERS.add(new TrainerFile(identifier, jsonArray));

							} catch (IOException e) {
								LOGGER.error(String.format(
										"Error occurred while loading trainer file %s", identifier.toString()));
							}
						});
				LOGGER.info(String.format("Loaded Redical Red trainers"));

				INCLEMENT_EMERALD_TRAINERS.clear();
				manager.findResources("inclementemerald", path -> path.toString().endsWith(".json"))
						.forEach((identifier, resource) -> {
							try (InputStream stream = resource.getInputStream()) {
								BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
								JsonArray jsonArray = new Gson().fromJson(bufferedReader, JsonArray.class);
								INCLEMENT_EMERALD_TRAINERS.add(new TrainerFile(identifier, jsonArray));

							} catch (IOException e) {
								LOGGER.error(String.format(
										"Error occurred while loading trainer file %s", identifier.toString()));
							}
						});
				LOGGER.info(String.format("Loaded Inclement Emerald trainers"));

				CUSTOM_TRAINERS.clear();
				manager.findResources("custom", path -> path.toString().endsWith(".json"))
						.forEach((identifier, resource) -> {
							try (InputStream stream = resource.getInputStream()) {
								BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
								JsonArray jsonArray = new Gson().fromJson(bufferedReader, JsonArray.class);
								CUSTOM_TRAINERS.add(new TrainerFile(identifier, jsonArray));

							} catch (IOException e) {
								LOGGER.error(String.format(
										"Error occurred while loading trainer file %s", identifier.toString()));
							}
						});
				LOGGER.info(String.format("Loaded custom trainers"));
			}
		});
	}
}