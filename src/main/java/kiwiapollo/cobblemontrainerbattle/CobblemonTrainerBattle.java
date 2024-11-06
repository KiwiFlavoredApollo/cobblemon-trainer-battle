package kiwiapollo.cobblemontrainerbattle;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.pokemon.aspect.AspectProvider;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactorySessionStorage;
import kiwiapollo.cobblemontrainerbattle.command.*;
import kiwiapollo.cobblemontrainerbattle.economy.Economy;
import kiwiapollo.cobblemontrainerbattle.economy.EconomyFactory;
import kiwiapollo.cobblemontrainerbattle.entities.EntityTypes;
import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.events.*;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupBattle;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupBattleSessionStorage;
import kiwiapollo.cobblemontrainerbattle.item.ItemRegistry;
import kiwiapollo.cobblemontrainerbattle.loot.CustomLootConditionTypes;
import kiwiapollo.cobblemontrainerbattle.parser.*;
import kiwiapollo.cobblemontrainerbattle.parser.history.PlayerHistoryManager;
import kiwiapollo.cobblemontrainerbattle.parser.profile.MiniGameProfileLoader;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerGroupProfileLoader;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerProfileLoader;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattleStorage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CobblemonTrainerBattle implements ModInitializer {
	public static final String MOD_ID = "cobblemontrainerbattle";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Config config = ConfigLoader.load();
	public static Economy economy = EconomyFactory.create(config.economy);

    @Override
	public void onInitialize() {
		Criteria.register(CustomCriteria.DEFEAT_TRAINER_CRITERION);
		Criteria.register(CustomCriteria.KILL_TRAINER_CRITERION);

		Registry.register(Registries.LOOT_CONDITION_TYPE, Identifier.of(MOD_ID, "defeated_in_battle"), CustomLootConditionTypes.DEFEATED_IN_BATTLE);

		AspectProvider.Companion.register(new FormAspectProvider());

		new ItemRegistry().register();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(new TrainerBattleCommand());
			dispatcher.register(new TrainerBattleFlatCommand());
			dispatcher.register(new TrainerBattleOtherCommand());
			dispatcher.register(new TrainerBattleFlatOtherCommand());
			dispatcher.register(new GroupBattleCommand());
			dispatcher.register(new GroupBattleFlatCommand());
			dispatcher.register(new BattleFactoryCommand());
			dispatcher.register(new CobblemonTrainerBattleCommand());
		});

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new TrainerProfileLoader());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new TrainerGroupProfileLoader());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new MiniGameProfileLoader());

		Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "trainer"), EntityTypes.TRAINER);
		FabricDefaultAttributeRegistry.register(EntityTypes.TRAINER, TrainerEntity.createMobAttributes());

		CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, BattleVictoryEventHandler::onBattleVictory);
		CobblemonEvents.LOOT_DROPPED.subscribe(Priority.HIGHEST, LootDroppedEventHandler::onLootDropped);

		ServerPlayConnectionEvents.DISCONNECT.register(TrainerBattleStorage::removeDisconnectedPlayerBattle);
		ServerPlayConnectionEvents.DISCONNECT.register(GroupBattleSessionStorage::removeDisconnectedPlayerSession);
		ServerPlayConnectionEvents.DISCONNECT.register(BattleFactorySessionStorage::removeDisconnectedPlayerSession);

		ServerPlayConnectionEvents.JOIN.register(PlayerHistoryManager::initializePlayerHistory);
		ServerLifecycleEvents.SERVER_STARTED.register(PlayerHistoryManager::loadFromNbt);
		ServerLifecycleEvents.SERVER_STOPPED.register(PlayerHistoryManager::saveToNbt);
		ServerTickEvents.END_SERVER_TICK.register(PlayerHistoryManager::periodicallySavePlayerHistory);

		ServerTickEvents.END_WORLD_TICK.register(TrainerEntitySpawnEventHandler::periodicallySpawnTrainerEntity);
		ServerEntityEvents.ENTITY_LOAD.register(TrainerEntityLoadEventHandler::synchronizeTrainerEntity);

		ServerTickEvents.END_WORLD_TICK.register(TrainerBattleFledEventHandler::endFledTrainerBattle);
	}
}