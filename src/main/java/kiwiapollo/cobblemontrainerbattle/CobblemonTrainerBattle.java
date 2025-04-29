package kiwiapollo.cobblemontrainerbattle;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.pokemon.aspect.AspectProvider;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.command.*;
import kiwiapollo.cobblemontrainerbattle.entity.HostileTrainerEntity;
import kiwiapollo.cobblemontrainerbattle.entity.NormalTrainerEntity;
import kiwiapollo.cobblemontrainerbattle.entity.StaticTrainerEntity;
import kiwiapollo.cobblemontrainerbattle.global.config.ConfigLoader;
import kiwiapollo.cobblemontrainerbattle.global.config.ConfigStorage;
import kiwiapollo.cobblemontrainerbattle.sound.CustomSoundEvent;
import kiwiapollo.cobblemontrainerbattle.entity.EntityType;
import kiwiapollo.cobblemontrainerbattle.event.*;
import kiwiapollo.cobblemontrainerbattle.item.CustomItemGroup;
import kiwiapollo.cobblemontrainerbattle.item.CustomItem;
import kiwiapollo.cobblemontrainerbattle.loot.CustomLootConditionType;
import kiwiapollo.cobblemontrainerbattle.global.history.PlayerHistoryGenerator;
import kiwiapollo.cobblemontrainerbattle.global.history.PlayerHistoryLoader;
import kiwiapollo.cobblemontrainerbattle.global.history.PlayerHistorySaver;
import kiwiapollo.cobblemontrainerbattle.pokemon.FormAspectProvider;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextGenerator;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextRemover;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
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

    @Override
	public void onInitialize() {
		ConfigStorage.getInstance().update(new ConfigLoader().load());
		CobblemonTrainerBattle.LOGGER.info("Loaded configuration");

		Criteria.register(CustomCriteria.DEFEAT_TRAINER_CRITERION);
		Criteria.register(CustomCriteria.KILL_TRAINER_CRITERION);

		Registry.register(Registries.LOOT_CONDITION_TYPE, Identifier.of(MOD_ID, "defeated_in_battle"), CustomLootConditionType.DEFEATED_IN_BATTLE);

		AspectProvider.Companion.register(new FormAspectProvider());

		CustomItem.register();
		CustomItemGroup.register();
		CustomSoundEvent.register();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(new TrainerBattleCommand());
			dispatcher.register(new TrainerBattleOtherCommand());
			dispatcher.register(new RentalBattleCommand());
			dispatcher.register(new RentalBattleOtherCommand());
			dispatcher.register(new RentalPokemonCommand());
			dispatcher.register(new CobblemonTrainerBattleCommand());
		});

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(TrainerStorage.getInstance());

		Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "normal_trainer"), EntityType.NORMAL_TRAINER);
		FabricDefaultAttributeRegistry.register(EntityType.NORMAL_TRAINER, NormalTrainerEntity.createMobAttributes());
		Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "hostile_trainer"), EntityType.HOSTILE_TRAINER);
		FabricDefaultAttributeRegistry.register(EntityType.HOSTILE_TRAINER, HostileTrainerEntity.createMobAttributes());
		Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "static_trainer"), EntityType.STATIC_TRAINER);
		FabricDefaultAttributeRegistry.register(EntityType.STATIC_TRAINER, StaticTrainerEntity.createMobAttributes());

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
	}
}