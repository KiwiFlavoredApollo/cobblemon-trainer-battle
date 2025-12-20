package kiwiapollo.cobblemontrainerbattle;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.pokemon.aspect.AspectProvider;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.block.CustomBlock;
import kiwiapollo.cobblemontrainerbattle.block.CustomScreenHandlerType;
import kiwiapollo.cobblemontrainerbattle.command.cobblemontrainerbattle.CobblemonTrainerBattleCommand;
import kiwiapollo.cobblemontrainerbattle.command.exportpokemon.ExportPokemonCommand;
import kiwiapollo.cobblemontrainerbattle.command.exportpokemon.ExportPokemonOtherCommand;
import kiwiapollo.cobblemontrainerbattle.command.exportpokemon.ShowdownTeamExporter;
import kiwiapollo.cobblemontrainerbattle.command.rentalbattle.RentalBattleCommand;
import kiwiapollo.cobblemontrainerbattle.command.rentalbattle.RentalBattleOtherCommand;
import kiwiapollo.cobblemontrainerbattle.command.rentalpokemon.RentalPokemonCommand;
import kiwiapollo.cobblemontrainerbattle.command.trainerbattle.TrainerBattleCommand;
import kiwiapollo.cobblemontrainerbattle.command.trainerbattle.TrainerBattleOtherCommand;
import kiwiapollo.cobblemontrainerbattle.entity.*;
import kiwiapollo.cobblemontrainerbattle.gamerule.CustomGameRule;
import kiwiapollo.cobblemontrainerbattle.battle.BattleHistoryStorage;
import kiwiapollo.cobblemontrainerbattle.item.*;
import kiwiapollo.cobblemontrainerbattle.item.misc.DeprecatedItem;
import kiwiapollo.cobblemontrainerbattle.item.misc.MiscItem;
import kiwiapollo.cobblemontrainerbattle.item.ticket.BdspTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.ticket.InclementEmeraldTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.ticket.RadicalRedTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.ticket.XyTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.token.BdspTokenItem;
import kiwiapollo.cobblemontrainerbattle.item.token.InclementEmeraldTokenItem;
import kiwiapollo.cobblemontrainerbattle.item.token.RadicalRedTokenItem;
import kiwiapollo.cobblemontrainerbattle.item.token.XyTokenItem;
import kiwiapollo.cobblemontrainerbattle.item.vsseeker.VsSeekerItem;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplateStorage;
import kiwiapollo.cobblemontrainerbattle.event.*;
import kiwiapollo.cobblemontrainerbattle.loot.CustomLootConditionType;
import kiwiapollo.cobblemontrainerbattle.pokemon.FormAspectProvider;
import kiwiapollo.cobblemontrainerbattle.sound.*;
import kiwiapollo.cobblemontrainerbattle.villager.PokeBallEngineerVillager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class CobblemonTrainerBattle implements ModInitializer {
	public static final String MOD_ID = "cobblemontrainerbattle";
	public static final Logger LOGGER = LoggerFactory.getLogger("CobblemonTrainerBattle");

    @Override
	public void onInitialize() {
        registerItem();
        registerItemGroup();
        registerGameRule();
        registerCriteria();
        registerConditionType();
        registerFormAspect();
        registerEntity();
        registerVillagerProfession();
        registerSoundEvent();
        registerCommand();
        registerScreenHandler();
        registerReloadListener();
        registerEvent();
	}

    private void registerGameRule() {
        CustomGameRule.initialize();
    }

    private void registerCriteria() {
        CustomCriteria.initialize();
    }

    private void registerConditionType() {
        CustomLootConditionType.initialize();
    }

    private void registerFormAspect() {
        AspectProvider.Companion.register(new FormAspectProvider());
    }

    private void registerEntity() {
        Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "neutral_trainer"), CustomEntityType.NEUTRAL_TRAINER);
        FabricDefaultAttributeRegistry.register(CustomEntityType.NEUTRAL_TRAINER, NeutralTrainerEntity.createMobAttributes());

        Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "static_trainer"), CustomEntityType.STATIC_TRAINER);
        FabricDefaultAttributeRegistry.register(CustomEntityType.STATIC_TRAINER, StaticTrainerEntity.createMobAttributes());

        Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "trainer"), CustomEntityType.TRAINER);
        FabricDefaultAttributeRegistry.register(CustomEntityType.TRAINER, TrainerEntity.createMobAttributes());

        Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "mannequin"), CustomEntityType.MANNEQUIN);
        FabricDefaultAttributeRegistry.register(CustomEntityType.MANNEQUIN, MannequinEntity.createMobAttributes());

        Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(MOD_ID, "trainer_table"), CustomEntityType.POKE_BALL_BOX);
    }

    private void registerVillagerProfession() {
        Registry.register(Registries.VILLAGER_PROFESSION, Identifier.of(CobblemonTrainerBattle.MOD_ID, PokeBallEngineerVillager.PROFESSION_ID), PokeBallEngineerVillager.PROFESSION);
    }

    private void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, environment) -> {
            dispatcher.register(new TrainerBattleCommand());
            dispatcher.register(new TrainerBattleOtherCommand());
            dispatcher.register(new RentalBattleCommand());
            dispatcher.register(new RentalBattleOtherCommand());
            dispatcher.register(new RentalPokemonCommand());
            dispatcher.register(new ExportPokemonCommand());
            dispatcher.register(new ExportPokemonOtherCommand());
            dispatcher.register(new CobblemonTrainerBattleCommand());
        });
    }

    private void registerScreenHandler() {
        Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MOD_ID, "poke_ball_box"), CustomScreenHandlerType.POKE_BALL_BOX);
    }

    private void registerReloadListener() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(TrainerTemplateStorage.getInstance());
    }

    private void registerEvent() {
        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, new BattleVictoryEventHandler());
        CobblemonEvents.LOOT_DROPPED.subscribe(Priority.HIGHEST, new LootDroppedEventHandler());
        ServerLifecycleEvents.SERVER_STARTED.register(new ShowdownTeamExporter.Renamer());
        ServerLifecycleEvents.SERVER_STARTED.register(new BattleHistoryStorage.Renamer());
        ServerLifecycleEvents.SERVER_STARTED.register(BattleHistoryStorage.getInstance());
        ServerLifecycleEvents.SERVER_STOPPED.register(BattleHistoryStorage.getInstance());
        ServerTickEvents.END_SERVER_TICK.register(BattleHistoryStorage.getInstance());
        ServerTickEvents.END_WORLD_TICK.register(new BattleFledEventHandler());
        ServerTickEvents.END_WORLD_TICK.register(new TrainerEntitySpawner());
        ServerEntityEvents.ENTITY_LOAD.register(new DeprecatedEntityMigrator());
    }

    private void registerItem() {
        DeprecatedItem.initialize();

        MiscItem.initialize();

        CustomBlock.initialize();

        InclementEmeraldTicketItem.initialize();
        InclementEmeraldTokenItem.initialize();

        RadicalRedTicketItem.initialize();
        RadicalRedTokenItem.initialize();

        XyTicketItem.initialize();
        XyTokenItem.initialize();

        BdspTicketItem.initialize();
        BdspTokenItem.initialize();

        VsSeekerItem.initialize();
    }

    private void registerItemGroup() {
        CustomItemGroup.initialize();
    }

    private void registerSoundEvent() {
        MiscSoundEvent.initialize();
        KantoSoundEvent.initialize();
        JohtoSoundEvent.initialize();
        HoenSoundEvent.initialize();
        SinohSoundEvent.initialize();
        UnovaBlackSoundEvent.initialize();
        UnovaWhiteSoundEvent.initialize();
        KalosSoundEvent.initialize();
        AlolaSoundEvent.initialize();
        GalarSoundEvent.initialize();
        HisuiSoundEvent.initialize();
        PaldeaSoundEvent.initialize();
    }
}