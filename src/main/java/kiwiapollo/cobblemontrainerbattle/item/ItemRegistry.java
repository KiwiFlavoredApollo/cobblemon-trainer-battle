package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entities.EntityTypes;
import kiwiapollo.cobblemontrainerbattle.entities.InclementEmeraldTrainerEntityPresetRegistry;
import kiwiapollo.cobblemontrainerbattle.entities.RadicalRedTrainerEntityPresetRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemRegistry {

    public void register() {
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "trainer_spawn_egg"), TRAINER_SPAWN_EGG);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "blue_vs_seeker"), BLUE_VS_SEEKER);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "red_vs_seeker"), RED_VS_SEEKER);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "green_vs_seeker"), GREEN_VS_SEEKER);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "purple_vs_seeker"), PURPLE_VS_SEEKER);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "trainer_token"), TRAINER_TOKEN);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_brock_token"), LEADER_BROCK_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_misty_token"), LEADER_MISTY_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_lt_surge_token"), LEADER_LT_SURGE_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_erika_token"), LEADER_ERIKA_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_koga_token"), LEADER_KOGA_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_sabrina_token"), LEADER_SABRINA_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_blaine_token"), LEADER_BLAINE_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_giovanni_token"), LEADER_GIOVANNI_TOKEN);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_falkner_token"), LEADER_FALKNER_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_bugsy_token"), LEADER_BUGSY_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_whitney_token"), LEADER_WHITNEY_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_morty_token"), LEADER_MORTY_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_chuck_token"), LEADER_CHUCK_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_jasmine_token"), LEADER_JASMINE_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_pryce_token"), LEADER_PRYCE_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_clair_token"), LEADER_CLAIR_TOKEN);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "elite_lorelei_token"), ELITE_LORELEI_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "elite_bruno_token"), ELITE_BRUNO_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "elite_agatha_token"), ELITE_AGATHA_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "elite_lance_token"), ELITE_LANCE_TOKEN);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "champion_terry_token"), CHAMPION_TERRY_TOKEN);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_brock_ticket"), LEADER_BROCK_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_misty_ticket"), LEADER_MISTY_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_lt_surge_ticket"), LEADER_LT_SURGE_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_erika_ticket"), LEADER_ERIKA_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_koga_ticket"), LEADER_KOGA_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_sabrina_ticket"), LEADER_SABRINA_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_blaine_ticket"), LEADER_BLAINE_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_giovanni_ticket"), LEADER_GIOVANNI_TICKET);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_falkner_ticket"), LEADER_FALKNER_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_bugsy_ticket"), LEADER_BUGSY_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_whitney_ticket"), LEADER_WHITNEY_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_morty_ticket"), LEADER_MORTY_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_chuck_ticket"), LEADER_CHUCK_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_jasmine_ticket"), LEADER_JASMINE_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_pryce_ticket"), LEADER_PRYCE_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_clair_ticket"), LEADER_CLAIR_TICKET);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "elite_lorelei_ticket"), ELITE_LORELEI_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "elite_bruno_ticket"), ELITE_BRUNO_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "elite_agatha_ticket"), ELITE_AGATHA_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "elite_lance_ticket"), ELITE_LANCE_TICKET);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "champion_terry_ticket"), CHAMPION_TERRY_TICKET);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_roxanne_token"), LEADER_ROXANNE_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_brawly_token"), LEADER_BRAWLY_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_wattson_token"), LEADER_WATTSON_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_flannery_token"), LEADER_FLANNERY_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_norman_token"), LEADER_NORMAN_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_winona_token"), LEADER_WINONA_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_tate_and_liza_token"), LEADER_TATE_AND_LIZA_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_juan_token"), LEADER_JUAN_TOKEN);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "elite_sidney_token"), ELITE_SIDNEY_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "elite_phoebe_token"), ELITE_PHOEBE_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "elite_glacia_token"), ELITE_GLACIA_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "elite_drake_token"), ELITE_DRAKE_TOKEN);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "champion_wallace_token"), CHAMPION_WALLACE_TOKEN);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_roxanne_ticket"), LEADER_ROXANNE_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_brawly_ticket"), LEADER_BRAWLY_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_wattson_ticket"), LEADER_WATTSON_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_flannery_ticket"), LEADER_FLANNERY_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_norman_ticket"), LEADER_NORMAN_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_winona_ticket"), LEADER_WINONA_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_tate_and_liza_ticket"), LEADER_TATE_AND_LIZA_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "leader_juan_ticket"), LEADER_JUAN_TICKET);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "elite_sidney_ticket"), ELITE_SIDNEY_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "elite_phoebe_ticket"), ELITE_PHOEBE_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "elite_glacia_ticket"), ELITE_GLACIA_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "elite_drake_ticket"), ELITE_DRAKE_TICKET);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.MOD_ID, "champion_wallace_ticket"), CHAMPION_WALLACE_TICKET);

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY, ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(TRAINER_SPAWN_EGG);

            itemGroup.add(BLUE_VS_SEEKER);
            itemGroup.add(RED_VS_SEEKER);
            itemGroup.add(GREEN_VS_SEEKER);
            itemGroup.add(PURPLE_VS_SEEKER);

            itemGroup.add(TRAINER_TOKEN);

            itemGroup.add(LEADER_BROCK_TOKEN);
            itemGroup.add(LEADER_MISTY_TOKEN);
            itemGroup.add(LEADER_LT_SURGE_TOKEN);
            itemGroup.add(LEADER_ERIKA_TOKEN);
            itemGroup.add(LEADER_KOGA_TOKEN);
            itemGroup.add(LEADER_SABRINA_TOKEN);
            itemGroup.add(LEADER_BLAINE_TOKEN);
            itemGroup.add(LEADER_GIOVANNI_TOKEN);

            itemGroup.add(LEADER_FALKNER_TOKEN);
            itemGroup.add(LEADER_BUGSY_TOKEN);
            itemGroup.add(LEADER_WHITNEY_TOKEN);
            itemGroup.add(LEADER_MORTY_TOKEN);
            itemGroup.add(LEADER_CHUCK_TOKEN);
            itemGroup.add(LEADER_JASMINE_TOKEN);
            itemGroup.add(LEADER_PRYCE_TOKEN);
            itemGroup.add(LEADER_CLAIR_TOKEN);

            itemGroup.add(ELITE_LORELEI_TOKEN);
            itemGroup.add(ELITE_BRUNO_TOKEN);
            itemGroup.add(ELITE_AGATHA_TOKEN);
            itemGroup.add(ELITE_LANCE_TOKEN);

            itemGroup.add(CHAMPION_TERRY_TOKEN);

            itemGroup.add(LEADER_BROCK_TICKET);
            itemGroup.add(LEADER_MISTY_TICKET);
            itemGroup.add(LEADER_LT_SURGE_TICKET);
            itemGroup.add(LEADER_ERIKA_TICKET);
            itemGroup.add(LEADER_KOGA_TICKET);
            itemGroup.add(LEADER_SABRINA_TICKET);
            itemGroup.add(LEADER_BLAINE_TICKET);
            itemGroup.add(LEADER_GIOVANNI_TICKET);

            itemGroup.add(LEADER_FALKNER_TICKET);
            itemGroup.add(LEADER_BUGSY_TICKET);
            itemGroup.add(LEADER_WHITNEY_TICKET);
            itemGroup.add(LEADER_MORTY_TICKET);
            itemGroup.add(LEADER_CHUCK_TICKET);
            itemGroup.add(LEADER_JASMINE_TICKET);
            itemGroup.add(LEADER_PRYCE_TICKET);
            itemGroup.add(LEADER_CLAIR_TICKET);

            itemGroup.add(ELITE_LORELEI_TICKET);
            itemGroup.add(ELITE_BRUNO_TICKET);
            itemGroup.add(ELITE_AGATHA_TICKET);
            itemGroup.add(ELITE_LANCE_TICKET);

            itemGroup.add(CHAMPION_TERRY_TICKET);            

            itemGroup.add(LEADER_ROXANNE_TOKEN);
            itemGroup.add(LEADER_BRAWLY_TOKEN);
            itemGroup.add(LEADER_WATTSON_TOKEN);
            itemGroup.add(LEADER_FLANNERY_TOKEN);
            itemGroup.add(LEADER_NORMAN_TOKEN);
            itemGroup.add(LEADER_WINONA_TOKEN);
            itemGroup.add(LEADER_TATE_AND_LIZA_TOKEN);
            itemGroup.add(LEADER_JUAN_TOKEN);

            itemGroup.add(ELITE_SIDNEY_TOKEN);
            itemGroup.add(ELITE_PHOEBE_TOKEN);
            itemGroup.add(ELITE_GLACIA_TOKEN);
            itemGroup.add(ELITE_DRAKE_TOKEN);

            itemGroup.add(CHAMPION_WALLACE_TOKEN);

            itemGroup.add(LEADER_ROXANNE_TICKET);
            itemGroup.add(LEADER_BRAWLY_TICKET);
            itemGroup.add(LEADER_WATTSON_TICKET);
            itemGroup.add(LEADER_FLANNERY_TICKET);
            itemGroup.add(LEADER_NORMAN_TICKET);
            itemGroup.add(LEADER_WINONA_TICKET);
            itemGroup.add(LEADER_TATE_AND_LIZA_TICKET);
            itemGroup.add(LEADER_JUAN_TICKET);

            itemGroup.add(ELITE_SIDNEY_TICKET);
            itemGroup.add(ELITE_PHOEBE_TICKET);
            itemGroup.add(ELITE_GLACIA_TICKET);
            itemGroup.add(ELITE_DRAKE_TICKET);

            itemGroup.add(CHAMPION_WALLACE_TICKET);
        });
    }

    public static final Item TRAINER_SPAWN_EGG = new SpawnEggItem(EntityTypes.TRAINER, 0xAAAAAA, 0xFF5555, new FabricItemSettings().maxCount(64));

    public static final Item BLUE_VS_SEEKER = new VsSeeker();
    public static final Item RED_VS_SEEKER = new VsSeeker();
    public static final Item GREEN_VS_SEEKER = new VsSeeker();
    public static final Item PURPLE_VS_SEEKER = new VsSeeker();

    public static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(CobblemonTrainerBattle.MOD_ID, "item_group"));
    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder().icon(() -> new ItemStack(BLUE_VS_SEEKER)).displayName(Text.literal("Trainers")).build();

    public static final Item TRAINER_TOKEN = new Item(new Item.Settings());

    public static final Item LEADER_BROCK_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_MISTY_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_LT_SURGE_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_ERIKA_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_KOGA_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_SABRINA_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_BLAINE_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_GIOVANNI_TOKEN = new Item(new Item.Settings());

    public static final Item LEADER_FALKNER_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_BUGSY_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_WHITNEY_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_MORTY_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_CHUCK_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_JASMINE_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_PRYCE_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_CLAIR_TOKEN = new Item(new Item.Settings());

    public static final Item ELITE_LORELEI_TOKEN = new Item(new Item.Settings());
    public static final Item ELITE_BRUNO_TOKEN = new Item(new Item.Settings());
    public static final Item ELITE_AGATHA_TOKEN = new Item(new Item.Settings());
    public static final Item ELITE_LANCE_TOKEN = new Item(new Item.Settings());

    public static final Item CHAMPION_TERRY_TOKEN = new Item(new Item.Settings());

    public static final Item LEADER_BROCK_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.LEADER_BROCK);
    public static final Item LEADER_MISTY_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.LEADER_MISTY);
    public static final Item LEADER_LT_SURGE_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.LEADER_LT_SURGE);
    public static final Item LEADER_ERIKA_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.LEADER_ERIKA);
    public static final Item LEADER_KOGA_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.LEADER_KOGA);
    public static final Item LEADER_SABRINA_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.LEADER_SABRINA);
    public static final Item LEADER_BLAINE_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.LEADER_BLAINE);
    public static final Item LEADER_GIOVANNI_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.LEADER_GIOVANNI);

    public static final Item LEADER_FALKNER_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.LEADER_FALKNER);
    public static final Item LEADER_BUGSY_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.LEADER_BUGSY);
    public static final Item LEADER_WHITNEY_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.LEADER_WHITNEY);
    public static final Item LEADER_MORTY_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.LEADER_MORTY);
    public static final Item LEADER_CHUCK_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.LEADER_CHUCK);
    public static final Item LEADER_JASMINE_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.LEADER_JASMINE);
    public static final Item LEADER_PRYCE_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.LEADER_PRYCE);
    public static final Item LEADER_CLAIR_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.LEADER_CLAIR);

    public static final Item ELITE_LORELEI_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.ELITE_LORELEI);
    public static final Item ELITE_BRUNO_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.ELITE_BRUNO);
    public static final Item ELITE_AGATHA_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.ELITE_AGATHA);
    public static final Item ELITE_LANCE_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.ELITE_LANCE);

    public static final Item CHAMPION_TERRY_TICKET = new TrainerTicket(new Item.Settings(), RadicalRedTrainerEntityPresetRegistry.CHAMPION_TERRY);
    
    public static final Item LEADER_ROXANNE_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_BRAWLY_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_WATTSON_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_FLANNERY_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_NORMAN_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_WINONA_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_TATE_AND_LIZA_TOKEN = new Item(new Item.Settings());
    public static final Item LEADER_JUAN_TOKEN = new Item(new Item.Settings());

    public static final Item ELITE_SIDNEY_TOKEN = new Item(new Item.Settings());
    public static final Item ELITE_PHOEBE_TOKEN = new Item(new Item.Settings());
    public static final Item ELITE_GLACIA_TOKEN = new Item(new Item.Settings());
    public static final Item ELITE_DRAKE_TOKEN = new Item(new Item.Settings());

    public static final Item CHAMPION_WALLACE_TOKEN = new Item(new Item.Settings());

    public static final Item LEADER_ROXANNE_TICKET = new TrainerTicket(new Item.Settings(), InclementEmeraldTrainerEntityPresetRegistry.LEADER_ROXANNE);
    public static final Item LEADER_BRAWLY_TICKET = new TrainerTicket(new Item.Settings(), InclementEmeraldTrainerEntityPresetRegistry.LEADER_BRAWLY);
    public static final Item LEADER_WATTSON_TICKET = new TrainerTicket(new Item.Settings(), InclementEmeraldTrainerEntityPresetRegistry.LEADER_WATTSON);
    public static final Item LEADER_FLANNERY_TICKET = new TrainerTicket(new Item.Settings(), InclementEmeraldTrainerEntityPresetRegistry.LEADER_FLANNERY);
    public static final Item LEADER_NORMAN_TICKET = new TrainerTicket(new Item.Settings(), InclementEmeraldTrainerEntityPresetRegistry.LEADER_NORMAN);
    public static final Item LEADER_WINONA_TICKET = new TrainerTicket(new Item.Settings(), InclementEmeraldTrainerEntityPresetRegistry.LEADER_WINONA);
    public static final Item LEADER_TATE_AND_LIZA_TICKET = new TrainerTicket(new Item.Settings(), InclementEmeraldTrainerEntityPresetRegistry.LEADER_TATE_AND_LIZA);
    public static final Item LEADER_JUAN_TICKET = new TrainerTicket(new Item.Settings(), InclementEmeraldTrainerEntityPresetRegistry.LEADER_JUAN);

    public static final Item ELITE_SIDNEY_TICKET = new TrainerTicket(new Item.Settings(), InclementEmeraldTrainerEntityPresetRegistry.ELITE_SIDNEY);
    public static final Item ELITE_PHOEBE_TICKET = new TrainerTicket(new Item.Settings(), InclementEmeraldTrainerEntityPresetRegistry.ELITE_PHOEBE);
    public static final Item ELITE_GLACIA_TICKET = new TrainerTicket(new Item.Settings(), InclementEmeraldTrainerEntityPresetRegistry.ELITE_GLACIA);
    public static final Item ELITE_DRAKE_TICKET = new TrainerTicket(new Item.Settings(), InclementEmeraldTrainerEntityPresetRegistry.ELITE_DRAKE);

    public static final Item CHAMPION_WALLACE_TICKET = new TrainerTicket(new Item.Settings(), InclementEmeraldTrainerEntityPresetRegistry.CHAMPION_WALLACE);
}
