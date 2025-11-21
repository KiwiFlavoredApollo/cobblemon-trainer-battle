package kiwiapollo.cobblemontrainerbattle;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.pokemon.aspect.AspectProvider;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.block.CustomScreenHandlerType;
import kiwiapollo.cobblemontrainerbattle.command.*;
import kiwiapollo.cobblemontrainerbattle.entity.NeutralTrainerEntity;
import kiwiapollo.cobblemontrainerbattle.entity.StaticTrainerEntity;
import kiwiapollo.cobblemontrainerbattle.gamerule.ModGameRule;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplateStorage;
import kiwiapollo.cobblemontrainerbattle.sound.CustomSoundEvent;
import kiwiapollo.cobblemontrainerbattle.entity.CustomEntityType;
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
import kiwiapollo.cobblemontrainerbattle.villager.PokeBallEngineerVillager;
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
		ModGameRule.register();

		Criteria.register(CustomCriteria.DEFEAT_TRAINER_CRITERION);
		Criteria.register(CustomCriteria.KILL_TRAINER_CRITERION);

		Registry.register(Registries.LOOT_CONDITION_TYPE, Identifier.of(MOD_ID, "defeated_in_battle"), CustomLootConditionType.DEFEATED_IN_BATTLE);

		AspectProvider.Companion.register(new FormAspectProvider());

		CustomItem.register();
		CustomItemGroup.register();
		CustomSoundEvent.register();

		Registry.register(Registries.VILLAGER_PROFESSION, Identifier.of(CobblemonTrainerBattle.MOD_ID, PokeBallEngineerVillager.PROFESSION_ID), PokeBallEngineerVillager.PROFESSION);

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(new TrainerBattleCommand());
			dispatcher.register(new TrainerBattleOtherCommand());
			dispatcher.register(new RentalBattleCommand());
			dispatcher.register(new RentalBattleOtherCommand());
			dispatcher.register(new RentalPokemonCommand());
			dispatcher.register(new CobblemonTrainerBattleCommand());
		});

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(TrainerTemplateStorage.getInstance());

		Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "neutral_trainer"), CustomEntityType.NEUTRAL_TRAINER);
		FabricDefaultAttributeRegistry.register(CustomEntityType.NEUTRAL_TRAINER, NeutralTrainerEntity.createMobAttributes());
		Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "static_trainer"), CustomEntityType.STATIC_TRAINER);
		FabricDefaultAttributeRegistry.register(CustomEntityType.STATIC_TRAINER, StaticTrainerEntity.createMobAttributes());
		Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(MOD_ID, "trainer_table"), CustomEntityType.POKE_BALL_BOX);

		Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MOD_ID, "poke_ball_box"), CustomScreenHandlerType.POKE_BALL_BOX);

		CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, new BattleVictoryEventHandler());
		CobblemonEvents.LOOT_DROPPED.subscribe(Priority.HIGHEST, new LootDroppedEventHandler());
		ServerTickEvents.END_WORLD_TICK.register(new TrainerBattleFledEventHandler());

		ServerPlayConnectionEvents.JOIN.register(new BattleContextGenerator());
		ServerPlayConnectionEvents.DISCONNECT.register(new BattleContextRemover());

		ServerPlayConnectionEvents.JOIN.register(new PlayerHistoryGenerator());
		ServerLifecycleEvents.SERVER_STARTED.register(new PlayerHistoryLoader());
		ServerLifecycleEvents.SERVER_STOPPED.register(new PlayerHistorySaver());
		ServerTickEvents.END_SERVER_TICK.register(new PlayerHistorySaver());

		ServerTickEvents.END_WORLD_TICK.register(new TrainerEntitySpawnerScheduler());
	}
}