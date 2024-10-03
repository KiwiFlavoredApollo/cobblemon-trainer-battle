package kiwiapollo.cobblemontrainerbattle;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.google.gson.JsonObject;
import kiwiapollo.cobblemontrainerbattle.common.ResourceReloadListener;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupFile;
import kiwiapollo.cobblemontrainerbattle.commands.*;
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
import net.minecraft.resource.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CobblemonTrainerBattle implements ModInitializer {
	public static final String NAMESPACE = "cobblemontrainerbattle";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
	public static final Config CONFIG = ConfigLoader.load();
	public static final Economy ECONOMY = EconomyFactory.create(CONFIG.economy);
	public static final String GROUP_CONFIG_DIR = "groups";
	public static final String TRAINER_CONFIG_DIR = "trainers";

	public static Map<UUID, PokemonBattle> trainerBattles = new HashMap<>();
	public static JsonObject defaultTrainerConfiguration = new JsonObject();
	public static Map<String, TrainerFile> trainerFiles = new HashMap<>();
	public static Map<String, GroupFile> groupFiles = new HashMap<>();

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(new BattleTrainerCommand());
			dispatcher.register(new BattleTrainerFlatCommand());
			dispatcher.register(new BattleGroupCommand());
			dispatcher.register(new BattleGroupFlatCommand());
			dispatcher.register(new GroupBattleSessionCommand());
			dispatcher.register(new BattleFactoryCommand());
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

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ResourceReloadListener());
	}
}