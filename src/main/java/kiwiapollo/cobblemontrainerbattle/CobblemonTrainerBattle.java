package kiwiapollo.cobblemontrainerbattle;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.pokemon.aspect.AspectProvider;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.command.*;
import kiwiapollo.cobblemontrainerbattle.common.CustomSoundEvents;
import kiwiapollo.cobblemontrainerbattle.entity.EntityTypes;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.event.*;
import kiwiapollo.cobblemontrainerbattle.item.CustomItemGroup;
import kiwiapollo.cobblemontrainerbattle.item.CustomItems;
import kiwiapollo.cobblemontrainerbattle.loot.CustomLootConditionTypes;
import kiwiapollo.cobblemontrainerbattle.parser.config.Config;
import kiwiapollo.cobblemontrainerbattle.parser.config.ConfigLoader;
import kiwiapollo.cobblemontrainerbattle.parser.history.PlayerHistoryGenerator;
import kiwiapollo.cobblemontrainerbattle.parser.history.PlayerHistoryLoader;
import kiwiapollo.cobblemontrainerbattle.parser.history.PlayerHistorySaver;
import kiwiapollo.cobblemontrainerbattle.parser.pokemon.FormAspectProvider;
import kiwiapollo.cobblemontrainerbattle.parser.player.BattleContextGenerator;
import kiwiapollo.cobblemontrainerbattle.parser.player.BattleContextRemover;
import kiwiapollo.cobblemontrainerbattle.parser.preset.TrainerStorage;
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

	public static Config config = new ConfigLoader().load();

    @Override
	public void onInitialize() {
		Criteria.register(CustomCriteria.DEFEAT_TRAINER_CRITERION);
		Criteria.register(CustomCriteria.KILL_TRAINER_CRITERION);

		Registry.register(Registries.LOOT_CONDITION_TYPE, Identifier.of(MOD_ID, "defeated_in_battle"), CustomLootConditionTypes.DEFEATED_IN_BATTLE);

		AspectProvider.Companion.register(new FormAspectProvider());

		CustomItems.register();
		CustomItemGroup.register();
		CustomSoundEvents.register();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(new TrainerBattleCommand());
			dispatcher.register(new TrainerBattleOtherCommand());
			dispatcher.register(new RentalBattleCommand());
			dispatcher.register(new RentalBattleOtherCommand());
			dispatcher.register(new RentalPokemonCommand());
			dispatcher.register(new CobblemonTrainerBattleCommand());
		});

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(TrainerStorage.getInstance());

		Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "trainer"), EntityTypes.TRAINER);
		FabricDefaultAttributeRegistry.register(EntityTypes.TRAINER, TrainerEntity.createMobAttributes());

		CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, new BattleVictoryEventHandler());
		CobblemonEvents.LOOT_DROPPED.subscribe(Priority.HIGHEST, new LootDroppedEventHandler());
		ServerTickEvents.END_WORLD_TICK.register(new TrainerBattleFledEventHandler());

		ServerPlayConnectionEvents.JOIN.register(new BattleContextGenerator());
		ServerPlayConnectionEvents.DISCONNECT.register(new BattleContextRemover());

		ServerPlayConnectionEvents.JOIN.register(new PlayerHistoryGenerator());
		ServerLifecycleEvents.SERVER_STARTED.register(new PlayerHistoryLoader());
		ServerLifecycleEvents.SERVER_STOPPED.register(new PlayerHistorySaver());
		ServerTickEvents.END_SERVER_TICK.register(new PlayerHistorySaver());

		ServerTickEvents.END_WORLD_TICK.register(new TrainerEntitySpawner());
		ServerEntityEvents.ENTITY_LOAD.register(new TrainerEntitySynchronizer());
	}
}