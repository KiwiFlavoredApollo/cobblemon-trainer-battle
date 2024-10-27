package kiwiapollo.cobblemontrainerbattle;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.pokemon.aspect.AspectProvider;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.advancement.KillTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.advancement.InteractTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import kiwiapollo.cobblemontrainerbattle.command.*;
import kiwiapollo.cobblemontrainerbattle.economy.Economy;
import kiwiapollo.cobblemontrainerbattle.economy.EconomyFactory;
import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.events.*;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupBattle;
import kiwiapollo.cobblemontrainerbattle.item.ItemRegistry;
import kiwiapollo.cobblemontrainerbattle.loot.DefeatedInBattleLootCondition;
import kiwiapollo.cobblemontrainerbattle.parser.*;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CobblemonTrainerBattle implements ModInitializer {
	public static final String NAMESPACE = "cobblemontrainerbattle";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

	public static final EntityType<TrainerEntity> TRAINER_ENTITY_TYPE = EntityType.Builder.create(TrainerEntity::new, SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("trainer");

	public static final DefeatTrainerCriterion DEFEAT_TRAINER_CRITERION = new DefeatTrainerCriterion();
	public static final KillTrainerCriterion KILL_TRAINER_CRITERION = new KillTrainerCriterion();
	public static final InteractTrainerCriterion INTERACT_TRAINER_CRITERION = new InteractTrainerCriterion();

	public static final LootConditionType DEFEATED_IN_BATTLE = new LootConditionType(new DefeatedInBattleLootCondition.Serializer());

	public static Config config = ConfigLoader.load();
	public static Economy economy = EconomyFactory.create(config.economy);

	public static Map<UUID, TrainerBattle> trainerBattleRegistry = new HashMap<>();
	public static Map<UUID, PlayerHistory> playerHistoryRegistry = new HashMap<>();

    @Override
	public void onInitialize() {
		Criteria.register(DEFEAT_TRAINER_CRITERION);
		Criteria.register(KILL_TRAINER_CRITERION);
		Criteria.register(INTERACT_TRAINER_CRITERION);

		Registry.register(Registries.LOOT_CONDITION_TYPE, Identifier.of(NAMESPACE, "defeated_in_battle"), DEFEATED_IN_BATTLE);

		new ItemRegistry().register();

		AspectProvider.Companion.register(new FormAspectProvider());

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(new TrainerBattleCommand());
			dispatcher.register(new TrainerBattleFlatCommand());
			dispatcher.register(new GroupBattleCommand());
			dispatcher.register(new GroupBattleFlatCommand());
			dispatcher.register(new BattleFactoryCommand());
			dispatcher.register(new CobblemonTrainerBattleCommand());
		});

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ProfileRegistries());

		Registry.register(Registries.ENTITY_TYPE, Identifier.of(NAMESPACE, "trainer"), TRAINER_ENTITY_TYPE);
		FabricDefaultAttributeRegistry.register(TRAINER_ENTITY_TYPE, TrainerEntity.createMobAttributes());

		CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, BattleVictoryEventHandler::onBattleVictory);
		CobblemonEvents.LOOT_DROPPED.subscribe(Priority.HIGHEST, LootDroppedEventHandler::onLootDropped);

		ServerPlayConnectionEvents.JOIN.register(this::initializePlayerHistory);

		ServerPlayConnectionEvents.DISCONNECT.register(this::removeDisconnectedPlayerTrainerBattle);
		ServerPlayConnectionEvents.DISCONNECT.register(GroupBattle::removeDisconnectedPlayerSession);
		ServerPlayConnectionEvents.DISCONNECT.register(BattleFactory::removeDisconnectedPlayerSession);

		ServerLifecycleEvents.SERVER_STARTED.register(PlayerHistoryRegistryParser::loadFromNbt);
		ServerLifecycleEvents.SERVER_STOPPED.register(PlayerHistoryRegistryParser::saveToNbt);
		ServerTickEvents.END_SERVER_TICK.register(PlayerHistoryRegistryParser::onEndServerTick);

		ServerTickEvents.END_WORLD_TICK.register(TrainerEntitySpawnEventHandler::onEndWorldTick);
		ServerEntityEvents.ENTITY_LOAD.register(TrainerEntityLoadEventHandler::onEntityLoad);

		ServerTickEvents.END_WORLD_TICK.register(TrainerBattleFledEventHandler::onEndWorldTick);
	}

	private void initializePlayerHistory(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
		UUID playerUuid = handler.getPlayer().getUuid();

		if (CobblemonTrainerBattle.playerHistoryRegistry.containsKey(playerUuid)) {
			return;
		}

		CobblemonTrainerBattle.playerHistoryRegistry.put(playerUuid, new PlayerHistory());
	}

	private void removeDisconnectedPlayerTrainerBattle(ServerPlayNetworkHandler handler, MinecraftServer server) {
		ServerPlayerEntity player = handler.getPlayer();

		if (!CobblemonTrainerBattle.trainerBattleRegistry.containsKey(player.getUuid())) {
			return;
		}

		CobblemonTrainerBattle.trainerBattleRegistry.get(player.getUuid()).onPlayerDefeat();
		CobblemonTrainerBattle.trainerBattleRegistry.remove(player.getUuid());
	}
}