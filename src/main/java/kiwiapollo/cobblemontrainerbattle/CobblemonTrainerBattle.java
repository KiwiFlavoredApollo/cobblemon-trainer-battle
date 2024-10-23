package kiwiapollo.cobblemontrainerbattle;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.pokemon.aspect.AspectProvider;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.advancement.KillTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.advancement.InteractTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactoryProfile;
import kiwiapollo.cobblemontrainerbattle.command.*;
import kiwiapollo.cobblemontrainerbattle.economy.Economy;
import kiwiapollo.cobblemontrainerbattle.economy.EconomyFactory;
import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.events.*;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupBattle;
import kiwiapollo.cobblemontrainerbattle.groupbattle.TrainerGroupProfile;
import kiwiapollo.cobblemontrainerbattle.parser.*;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerProfile;
import kotlin.Unit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.advancement.criterion.Criteria;
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

import java.util.*;

public class CobblemonTrainerBattle implements ModInitializer {
	public static final String NAMESPACE = "cobblemontrainerbattle";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

	public static final EntityType<TrainerEntity> TRAINER_ENTITY_TYPE = EntityType.Builder.create(TrainerEntity::new, SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("trainer");
	public static final Item TRAINER_SPAWN_EGG = new SpawnEggItem(TRAINER_ENTITY_TYPE, 0xAAAAAA, 0xFF5555, new FabricItemSettings().maxCount(64));

	public static final int FLEE_DISTANCE = 20;

	public static DefeatTrainerCriterion DEFEAT_TRAINER_CRITERION = new DefeatTrainerCriterion();
	public static KillTrainerCriterion KILL_TRAINER_CRITERION = new KillTrainerCriterion();
	public static InteractTrainerCriterion INTERACT_TRAINER_CRITERION = new InteractTrainerCriterion();

	public static Config config = ConfigLoader.load();
	public static Economy economy = EconomyFactory.create(config.economy);

	public static Map<Identifier, TrainerProfile> trainerProfileRegistry = new HashMap<>();
	public static Map<Identifier, TrainerGroupProfile> trainerGroupProfileRegistry = new HashMap<>();
	public static BattleFactoryProfile battleFactoryProfile;

	public static Map<UUID, TrainerBattle> trainerBattleRegistry = new HashMap<>();
	public static Map<UUID, PlayerHistory> playerHistoryRegistry = new HashMap<>();

    @Override
	public void onInitialize() {
		Criteria.register(DEFEAT_TRAINER_CRITERION);
		Criteria.register(KILL_TRAINER_CRITERION);
		Criteria.register(INTERACT_TRAINER_CRITERION);

		AspectProvider.Companion.register(new FormAspectProvider());

		Registry.register(Registries.ITEM, new Identifier(NAMESPACE, "trainer_spawn_egg"), TRAINER_SPAWN_EGG);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> entries.add(TRAINER_SPAWN_EGG));

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
			List<UUID> battleIds = trainerBattleRegistry.values().stream().map(TrainerBattle::getBattleId).toList();
			if (!battleIds.contains(battleVictoryEvent.getBattle().getBattleId())) {
				return Unit.INSTANCE;
			}

			ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);
			boolean isPlayerVictory = battleVictoryEvent.getWinners().stream()
					.anyMatch(battleActor -> battleActor.isForPlayer(player));

			if (isPlayerVictory) {
				trainerBattleRegistry.get(player.getUuid()).onPlayerVictory();

				DEFEAT_TRAINER_CRITERION.trigger(player);

			} else {
				trainerBattleRegistry.get(player.getUuid()).onPlayerDefeat();
			}

			trainerBattleRegistry.remove(player.getUuid());

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

			if (trainerBattleRegistry.containsKey(playerUuid)) {
				trainerBattleRegistry.get(playerUuid).onPlayerDefeat();
				trainerBattleRegistry.remove(playerUuid);
			}

			if (GroupBattle.sessionRegistry.containsKey(playerUuid)) {
				GroupBattle.sessionRegistry.get(playerUuid).onSessionStop();
				GroupBattle.sessionRegistry.remove(playerUuid);
			}

			if (BattleFactory.sessionRegistry.containsKey(playerUuid)) {
				BattleFactory.sessionRegistry.get(playerUuid).onSessionStop();
				BattleFactory.sessionRegistry.remove(playerUuid);
			}
		});

		ServerLifecycleEvents.SERVER_STARTED.register(PlayerHistoryRegistryParser::loadFromNbt);

		ServerLifecycleEvents.SERVER_STOPPED.register(PlayerHistoryRegistryParser::saveToNbt);
		ServerTickEvents.END_SERVER_TICK.register(PlayerHistoryRegistryParser::onEndServerTick);

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ResourceReloadListener());

		Registry.register(Registries.ENTITY_TYPE, Identifier.of(NAMESPACE, "trainer"), TRAINER_ENTITY_TYPE);
		FabricDefaultAttributeRegistry.register(TRAINER_ENTITY_TYPE, TrainerEntity.createMobAttributes());
		ServerTickEvents.END_WORLD_TICK.register(TrainerEntitySpawnEventHandler::onEndWorldTick);
		ServerEntityEvents.ENTITY_LOAD.register(TrainerEntityLoadEventHandler::onEntityLoad);

		ServerTickEvents.END_WORLD_TICK.register(TrainerBattleFledEventHandler::onEndWorldTick);
	}
}