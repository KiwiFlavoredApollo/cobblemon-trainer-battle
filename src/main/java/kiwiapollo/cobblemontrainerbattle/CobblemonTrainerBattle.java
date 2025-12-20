package kiwiapollo.cobblemontrainerbattle;

import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.block.CustomScreenHandlerType;
import kiwiapollo.cobblemontrainerbattle.command.CustomCommand;
import kiwiapollo.cobblemontrainerbattle.entity.*;
import kiwiapollo.cobblemontrainerbattle.gamerule.CustomGameRule;
import kiwiapollo.cobblemontrainerbattle.item.*;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplateStorage;
import kiwiapollo.cobblemontrainerbattle.event.*;
import kiwiapollo.cobblemontrainerbattle.loot.CustomLootConditionType;
import kiwiapollo.cobblemontrainerbattle.pokemon.FormAspectProvider;
import kiwiapollo.cobblemontrainerbattle.sound.*;
import kiwiapollo.cobblemontrainerbattle.villager.CustomVillagerProfession;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CobblemonTrainerBattle implements ModInitializer {
	public static final String MOD_ID = "cobblemontrainerbattle";
	public static final Logger LOGGER = LoggerFactory.getLogger("CobblemonTrainerBattle");

    @Override
	public void onInitialize() {
        CustomItem.initialize();

        CustomItemGroup.initialize();

        CustomGameRule.initialize();

        CustomCriteria.initialize();

        CustomLootConditionType.initialize();

        CustomEntityType.initialize();

        CustomVillagerProfession.initialize();

        CustomSoundEvent.initialize();

        CustomCommand.initialize();

        CustomScreenHandlerType.initialize();

        CustomEventHandler.initialize();

        FormAspectProvider.initialize();

        TrainerTemplateStorage.initialize();
	}
}