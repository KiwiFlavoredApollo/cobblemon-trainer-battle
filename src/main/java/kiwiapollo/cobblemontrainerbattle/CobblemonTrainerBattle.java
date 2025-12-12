package kiwiapollo.cobblemontrainerbattle;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.pokemon.aspect.AspectProvider;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.block.CustomBlock;
import kiwiapollo.cobblemontrainerbattle.block.CustomScreenHandlerType;
import kiwiapollo.cobblemontrainerbattle.command.cobblemontrainerbattle.CobblemonTrainerBattleCommand;
import kiwiapollo.cobblemontrainerbattle.command.rentalbattle.RentalBattleCommand;
import kiwiapollo.cobblemontrainerbattle.command.rentalbattle.RentalBattleOtherCommand;
import kiwiapollo.cobblemontrainerbattle.command.rentalpokemon.RentalPokemonCommand;
import kiwiapollo.cobblemontrainerbattle.command.trainerbattle.TrainerBattleCommand;
import kiwiapollo.cobblemontrainerbattle.command.trainerbattle.TrainerBattleOtherCommand;
import kiwiapollo.cobblemontrainerbattle.entity.*;
import kiwiapollo.cobblemontrainerbattle.gamerule.CustomGameRule;
import kiwiapollo.cobblemontrainerbattle.battle.BattleHistoryStorage;
import kiwiapollo.cobblemontrainerbattle.item.CustomItemGroup;
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
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class CobblemonTrainerBattle implements ModInitializer {
	public static final String MOD_ID = "cobblemontrainerbattle";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

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
        CustomGameRule.register();
    }

    private void registerCriteria() {
        Criteria.register(CustomCriteria.DEFEAT_TRAINER_CRITERION);
        Criteria.register(CustomCriteria.KILL_TRAINER_CRITERION);
    }

    private void registerConditionType() {
        Registry.register(Registries.LOOT_CONDITION_TYPE, Identifier.of(MOD_ID, "defeated_in_battle"), CustomLootConditionType.DEFEATED_IN_BATTLE);
    }

    private void registerFormAspect() {
        AspectProvider.Companion.register(new FormAspectProvider());
    }

    private void registerEntity() {
        Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "neutral_trainer"), CustomEntityType.NEUTRAL_TRAINER);
        FabricDefaultAttributeRegistry.register(CustomEntityType.NEUTRAL_TRAINER, NeutralTrainerEntity.createMobAttributes());

        Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "static_trainer"), CustomEntityType.STATIC_TRAINER);
        FabricDefaultAttributeRegistry.register(CustomEntityType.STATIC_TRAINER, StaticTrainerEntity.createMobAttributes());

        Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "drifter"), CustomEntityType.DRIFTER);
        FabricDefaultAttributeRegistry.register(CustomEntityType.DRIFTER, DrifterEntity.createMobAttributes());

        Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "camper"), CustomEntityType.CAMPER);
        FabricDefaultAttributeRegistry.register(CustomEntityType.CAMPER, CamperEntity.createMobAttributes());

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
        ServerLifecycleEvents.SERVER_STARTED.register(BattleHistoryStorage.getInstance());
        ServerLifecycleEvents.SERVER_STOPPED.register(BattleHistoryStorage.getInstance());
        ServerTickEvents.END_SERVER_TICK.register(BattleHistoryStorage.getInstance());
        ServerTickEvents.END_WORLD_TICK.register(new BattleFledEventHandler());
        ServerTickEvents.END_WORLD_TICK.register(new DrifterEntitySpawner());
        ServerEntityEvents.ENTITY_LOAD.register(new DeprecatedEntityMigrator());
    }

    private void registerItem() {
        Arrays.stream(DeprecatedItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(MiscItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(CustomBlock.values()).forEach(block -> {
            Registry.register(Registries.BLOCK, block.getIdentifier(), block.getBlock());
            Registry.register(Registries.ITEM, block.getIdentifier(), block.getItem());
        });

        Arrays.stream(InclementEmeraldTicketItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(InclementEmeraldTokenItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(RadicalRedTicketItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(RadicalRedTokenItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(XyTicketItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(XyTokenItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(BdspTicketItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(BdspTokenItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });

        Arrays.stream(VsSeekerItem.values()).forEach(item -> {
            Registry.register(Registries.ITEM, item.getIdentifier(), item.getItem());
        });
    }

    private void registerItemGroup() {
        Registry.register(Registries.ITEM_GROUP, CustomItemGroup.ITEM_GROUP_KEY, CustomItemGroup.ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(CustomItemGroup.ITEM_GROUP_KEY).register(itemGroup -> {
            Arrays.stream(MiscItem.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(VsSeekerItem.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(InclementEmeraldTicketItem.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(InclementEmeraldTokenItem.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(RadicalRedTicketItem.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(RadicalRedTokenItem.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(XyTicketItem.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(XyTokenItem.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(BdspTicketItem.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });

            Arrays.stream(BdspTokenItem.values()).forEach(item -> {
                itemGroup.add(item.getItem());
            });
        });
    }

    private void registerSoundEvent() {
        Arrays.stream(MiscSoundEvent.values()).forEach(sound -> {
            tryRegister(Registries.SOUND_EVENT, sound.getIdentifier(), sound.getSoundEvent());
        });

        Arrays.stream(KantoSoundEvent.values()).forEach(sound -> {
            tryRegister(Registries.SOUND_EVENT, sound.getIdentifier(), sound.getSoundEvent());
        });

        Arrays.stream(JohtoSoundEvent.values()).forEach(sound -> {
            tryRegister(Registries.SOUND_EVENT, sound.getIdentifier(), sound.getSoundEvent());
        });

        Arrays.stream(HoenSoundEvent.values()).forEach(sound -> {
            tryRegister(Registries.SOUND_EVENT, sound.getIdentifier(), sound.getSoundEvent());
        });

        Arrays.stream(SinohSoundEvent.values()).forEach(sound -> {
            tryRegister(Registries.SOUND_EVENT, sound.getIdentifier(), sound.getSoundEvent());
        });

        Arrays.stream(UnovaBlackSoundEvent.values()).forEach(sound -> {
            tryRegister(Registries.SOUND_EVENT, sound.getIdentifier(), sound.getSoundEvent());
        });

        Arrays.stream(UnovaWhiteSoundEvent.values()).forEach(sound -> {
            tryRegister(Registries.SOUND_EVENT, sound.getIdentifier(), sound.getSoundEvent());
        });

        Arrays.stream(KalosSoundEvent.values()).forEach(sound -> {
            tryRegister(Registries.SOUND_EVENT, sound.getIdentifier(), sound.getSoundEvent());
        });

        Arrays.stream(AlolaSoundEvent.values()).forEach(sound -> {
            tryRegister(Registries.SOUND_EVENT, sound.getIdentifier(), sound.getSoundEvent());
        });

        Arrays.stream(GalarSoundEvent.values()).forEach(sound -> {
            tryRegister(Registries.SOUND_EVENT, sound.getIdentifier(), sound.getSoundEvent());
        });

        Arrays.stream(HisuiSoundEvent.values()).forEach(sound -> {
            tryRegister(Registries.SOUND_EVENT, sound.getIdentifier(), sound.getSoundEvent());
        });

        Arrays.stream(PaldeaSoundEvent.values()).forEach(sound -> {
            tryRegister(Registries.SOUND_EVENT, sound.getIdentifier(), sound.getSoundEvent());
        });
    }

    private <T> void tryRegister(Registry<T> registry, Identifier id, T entry) {
        try {
            Registry.register(registry, id, entry);

        } catch (RuntimeException ignored) {

        }
    }
}