package kiwiapollo.cobblemontrainerbattle;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.google.gson.JsonObject;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import kiwiapollo.cobblemontrainerbattle.commands.*;
import kiwiapollo.cobblemontrainerbattle.common.Config;
import kiwiapollo.cobblemontrainerbattle.common.ConfigLoader;
import kiwiapollo.cobblemontrainerbattle.common.EconomyFactory;
import kiwiapollo.cobblemontrainerbattle.common.ResourceReloadListener;
import kiwiapollo.cobblemontrainerbattle.economies.Economy;
import kiwiapollo.cobblemontrainerbattle.entities.EntityBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.events.*;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupBattle;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupFile;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerFile;
import kotlin.Unit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CobblemonTrainerBattle implements ModInitializer {
	public static final String NAMESPACE = "cobblemontrainerbattle";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
	public static final String GROUP_CONFIG_DIR = "groups";
	public static final String TRAINER_CONFIG_DIR = "trainers";
	public static final String ARCADE_CONFIG_DIR = "arcades";
	public static final EntityType<TrainerEntity> TRAINER_ENTITY_TYPE =
			EntityType.Builder.create(TrainerEntity::new, SpawnGroup.CREATURE)
					.setDimensions(0.6f, 1.8f)
					.build("trainer");
	public static final Item TRAINER_SPAWN_EGG = new SpawnEggItem(
			TRAINER_ENTITY_TYPE,
			0xAAAAAA,
			0xFF5555,
			new FabricItemSettings().maxCount(64)
	);

	public static Config config = ConfigLoader.load();
	public static Economy economy = EconomyFactory.create(config.economy);
	public static JsonObject defaultTrainerConfiguration = new JsonObject();
	public static Map<String, TrainerFile> trainerFiles = new HashMap<>();
	public static Map<String, GroupFile> groupFiles = new HashMap<>();
	public static JsonObject battleFactoryConfiguration = new JsonObject();

	@Override
	public void onInitialize() {
		Registry.register(Registries.ITEM, new Identifier(NAMESPACE, "trainer_spawn_egg"), TRAINER_SPAWN_EGG);

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
			entries.add(TRAINER_SPAWN_EGG);
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(new TrainerBattleCommand());
			dispatcher.register(new TrainerBattleFlatCommand());
			dispatcher.register(new GroupBattleCommand());
			dispatcher.register(new GroupBattleFlatCommand());
			dispatcher.register(new BattleFactoryCommand());
			dispatcher.register(new CobblemonTrainerBattleCommand());
		});

		CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, battleVictoryEvent -> {
			// BATTLE_VICTORY event fires even if the player loses
			ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);

			if (GroupBattle.trainerBattles.containsKey(player.getUuid())) {
				new GroupBattleVictoryEventHandler().onBattleVictory(battleVictoryEvent);
				GroupBattle.trainerBattles.remove(player.getUuid());
			}

			if (BattleFactory.trainerBattles.containsKey(player.getUuid())) {
				new BattleFactoryVictoryEventHandler().onBattleVictory(battleVictoryEvent);
				BattleFactory.trainerBattles.remove(player.getUuid());
			}

			if (EntityBackedTrainerBattle.trainerBattles.containsKey(player.getUuid())) {
				new EntityBackedTrainerBattleVictoryEventHandler().onBattleVictory(battleVictoryEvent);
				EntityBackedTrainerBattle.trainerBattles.remove(player.getUuid());
			}

			if (TrainerBattle.trainerBattles.containsKey(player.getUuid())) {
				new TrainerBattleVictoryEventHandler().onBattleVictory(battleVictoryEvent);
				TrainerBattle.trainerBattles.remove(player.getUuid());
			}

			return Unit.INSTANCE;
        });

		CobblemonEvents.LOOT_DROPPED.subscribe(Priority.HIGHEST, lootDroppedEvent -> {
			// LOOT_DROPPED event fires before BATTLE_VICTORY event
			// Cobblemon Discord, Hiroku: It's only used if the player kills the pokemon by hand, not by battle
			// However Pokemons drop loot when defeated in battles, at least on 1.5.1
			new LootDroppedEventHandler().onLootDropped(lootDroppedEvent);

			return Unit.INSTANCE;
        });

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			UUID playerUuid = handler.getPlayer().getUuid();

			if (GroupBattle.trainerBattles.containsKey(playerUuid)) {
				GroupBattle.sessions.get(playerUuid).isDefeated = true;
				GroupBattle.trainerBattles.get(playerUuid).end();
				GroupBattle.trainerBattles.remove(playerUuid);
			}

			if (BattleFactory.trainerBattles.containsKey(playerUuid)) {
				BattleFactory.sessions.get(playerUuid).isDefeated = true;
				BattleFactory.trainerBattles.get(playerUuid).end();
				BattleFactory.trainerBattles.remove(playerUuid);
			}

			if (EntityBackedTrainerBattle.trainerBattles.containsKey(playerUuid)) {
				EntityBackedTrainerBattle.trainerBattles.get(playerUuid).end();
				EntityBackedTrainerBattle.trainerBattles.remove(playerUuid);
			}

			if (TrainerBattle.trainerBattles.containsKey(playerUuid)) {
				TrainerBattle.trainerBattles.get(playerUuid).end();
				TrainerBattle.trainerBattles.remove(playerUuid);
			}
		});

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ResourceReloadListener());

		Registry.register(Registries.ENTITY_TYPE, Identifier.of(NAMESPACE, "trainer"), TRAINER_ENTITY_TYPE);
		FabricDefaultAttributeRegistry.register(TRAINER_ENTITY_TYPE, TrainerEntity.createMobAttributes());
		ServerTickEvents.END_WORLD_TICK.register(TrainerSpawnEventHandler::onEndWorldTick);
	}
}