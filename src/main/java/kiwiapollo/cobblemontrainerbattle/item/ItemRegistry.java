package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entities.EntityTypes;
import kiwiapollo.cobblemontrainerbattle.entities.SelectedTrainerEntityFactory;
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
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "trainer_spawn_egg"), TRAINER_SPAWN_EGG);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "blue_vs_seeker"), BLUE_VS_SEEKER);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "red_vs_seeker"), RED_VS_SEEKER);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "green_vs_seeker"), GREEN_VS_SEEKER);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "purple_vs_seeker"), PURPLE_VS_SEEKER);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "trainer_token"), TRAINER_TOKEN);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "radicalred_leader_brock_token"), RADICALRED_LEADER_BROCK_TOKEN);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "radicalred_leader_brock_ticket"), RADICALRED_LEADER_BROCK_TICKET);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "inclementemerald_leader_roxanne_token"), INCLEMENTEMERALD_LEADER_ROXANNE_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "inclementemerald_leader_brawly_token"), INCLEMENTEMERALD_LEADER_BRAWLY_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "inclementemerald_leader_wattson_token"), INCLEMENTEMERALD_LEADER_WATTSON_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "inclementemerald_leader_flannery_token"), INCLEMENTEMERALD_LEADER_FLANNERY_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "inclementemerald_leader_norman_token"), INCLEMENTEMERALD_LEADER_NORMAN_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "inclementemerald_leader_winona_token"), INCLEMENTEMERALD_LEADER_WINONA_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "inclementemerald_leader_tate_and_liza_token"), INCLEMENTEMERALD_LEADER_TATE_AND_LIZA_TOKEN);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "inclementemerald_leader_juan_token"), INCLEMENTEMERALD_LEADER_JUAN_TOKEN);

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "inclementemerald_leader_roxanne_ticket"), INCLEMENTEMERALD_LEADER_ROXANNE_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "inclementemerald_leader_brawly_ticket"), INCLEMENTEMERALD_LEADER_BRAWLY_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "inclementemerald_leader_wattson_ticket"), INCLEMENTEMERALD_LEADER_WATTSON_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "inclementemerald_leader_flannery_ticket"), INCLEMENTEMERALD_LEADER_FLANNERY_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "inclementemerald_leader_norman_ticket"), INCLEMENTEMERALD_LEADER_NORMAN_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "inclementemerald_leader_winona_ticket"), INCLEMENTEMERALD_LEADER_WINONA_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "inclementemerald_leader_tate_and_liza_ticket"), INCLEMENTEMERALD_LEADER_TATE_AND_LIZA_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "inclementemerald_leader_juan_ticket"), INCLEMENTEMERALD_LEADER_JUAN_TICKET);
        
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY, ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(TRAINER_SPAWN_EGG);

            itemGroup.add(BLUE_VS_SEEKER);
            itemGroup.add(RED_VS_SEEKER);
            itemGroup.add(GREEN_VS_SEEKER);
            itemGroup.add(PURPLE_VS_SEEKER);

            itemGroup.add(TRAINER_TOKEN);

            itemGroup.add(RADICALRED_LEADER_BROCK_TOKEN);

            itemGroup.add(RADICALRED_LEADER_BROCK_TICKET);
            
            itemGroup.add(INCLEMENTEMERALD_LEADER_ROXANNE_TOKEN);
            itemGroup.add(INCLEMENTEMERALD_LEADER_BRAWLY_TOKEN);
            itemGroup.add(INCLEMENTEMERALD_LEADER_WATTSON_TOKEN);
            itemGroup.add(INCLEMENTEMERALD_LEADER_FLANNERY_TOKEN);
            itemGroup.add(INCLEMENTEMERALD_LEADER_NORMAN_TOKEN);
            itemGroup.add(INCLEMENTEMERALD_LEADER_WINONA_TOKEN);
            itemGroup.add(INCLEMENTEMERALD_LEADER_TATE_AND_LIZA_TOKEN);
            itemGroup.add(INCLEMENTEMERALD_LEADER_JUAN_TOKEN);

            itemGroup.add(INCLEMENTEMERALD_LEADER_ROXANNE_TICKET);
            itemGroup.add(INCLEMENTEMERALD_LEADER_BRAWLY_TICKET);
            itemGroup.add(INCLEMENTEMERALD_LEADER_WATTSON_TICKET);
            itemGroup.add(INCLEMENTEMERALD_LEADER_FLANNERY_TICKET);
            itemGroup.add(INCLEMENTEMERALD_LEADER_NORMAN_TICKET);
            itemGroup.add(INCLEMENTEMERALD_LEADER_WINONA_TICKET);
            itemGroup.add(INCLEMENTEMERALD_LEADER_TATE_AND_LIZA_TICKET);
            itemGroup.add(INCLEMENTEMERALD_LEADER_JUAN_TICKET);
        });
    }

    public static final int VS_SEEKER_MAX_COUNT = 1;

    public static final Item TRAINER_SPAWN_EGG = new SpawnEggItem(EntityTypes.TRAINER, 0xAAAAAA, 0xFF5555, new FabricItemSettings().maxCount(64));

    public static final Item BLUE_VS_SEEKER = new VsSeeker(new Item.Settings().maxCount(VS_SEEKER_MAX_COUNT));
    public static final Item RED_VS_SEEKER = new VsSeeker(new Item.Settings().maxCount(VS_SEEKER_MAX_COUNT));
    public static final Item GREEN_VS_SEEKER = new VsSeeker(new Item.Settings().maxCount(VS_SEEKER_MAX_COUNT));
    public static final Item PURPLE_VS_SEEKER = new VsSeeker(new Item.Settings().maxCount(VS_SEEKER_MAX_COUNT));

    public static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(CobblemonTrainerBattle.NAMESPACE, "item_group"));
    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder().icon(() -> new ItemStack(BLUE_VS_SEEKER)).displayName(Text.literal("Trainers")).build();

    public static final Item TRAINER_TOKEN = new Item(new Item.Settings());

    public static final Item RADICALRED_LEADER_BROCK_TOKEN = new Item(new Item.Settings());

    public static final Item RADICALRED_LEADER_BROCK_TICKET = new TrainerTicket(
            new Item.Settings(),
            new SelectedTrainerEntityFactory(
                    Identifier.of("trainer", "radicalred/leader_brock"),
                    Identifier.of("minecraft", "textures/entity/player/slim/steve.png")
            )
    );

    public static final Item INCLEMENTEMERALD_LEADER_ROXANNE_TOKEN = new Item(new Item.Settings());
    public static final Item INCLEMENTEMERALD_LEADER_BRAWLY_TOKEN = new Item(new Item.Settings());
    public static final Item INCLEMENTEMERALD_LEADER_WATTSON_TOKEN = new Item(new Item.Settings());
    public static final Item INCLEMENTEMERALD_LEADER_FLANNERY_TOKEN = new Item(new Item.Settings());
    public static final Item INCLEMENTEMERALD_LEADER_NORMAN_TOKEN = new Item(new Item.Settings());
    public static final Item INCLEMENTEMERALD_LEADER_WINONA_TOKEN = new Item(new Item.Settings());
    public static final Item INCLEMENTEMERALD_LEADER_TATE_AND_LIZA_TOKEN = new Item(new Item.Settings());
    public static final Item INCLEMENTEMERALD_LEADER_JUAN_TOKEN = new Item(new Item.Settings());


    public static final Item INCLEMENTEMERALD_LEADER_ROXANNE_TICKET = new TrainerTicket(
            new Item.Settings(),
            new SelectedTrainerEntityFactory(
                    Identifier.of("trainer", "inclementemerald/roxanne_2"),
                    Identifier.of("minecraft", "textures/entity/player/slim/alex.png")
            )
    );
    public static final Item INCLEMENTEMERALD_LEADER_BRAWLY_TICKET = new TrainerTicket(
            new Item.Settings(),
            new SelectedTrainerEntityFactory(
                    Identifier.of("trainer", "inclementemerald/brawly_2"),
                    Identifier.of("minecraft", "textures/entity/player/slim/alex.png")
            )
    );
    public static final Item INCLEMENTEMERALD_LEADER_WATTSON_TICKET = new TrainerTicket(
            new Item.Settings(),
            new SelectedTrainerEntityFactory(
                    Identifier.of("trainer", "inclementemerald/wattson_2"),
                    Identifier.of("minecraft", "textures/entity/player/slim/alex.png")
            )
    );
    public static final Item INCLEMENTEMERALD_LEADER_FLANNERY_TICKET = new TrainerTicket(
            new Item.Settings(),
            new SelectedTrainerEntityFactory(
                    Identifier.of("trainer", "inclementemerald/flannery_2"),
                    Identifier.of("minecraft", "textures/entity/player/slim/alex.png")
            )
    );
    public static final Item INCLEMENTEMERALD_LEADER_NORMAN_TICKET = new TrainerTicket(
            new Item.Settings(),
            new SelectedTrainerEntityFactory(
                    Identifier.of("trainer", "inclementemerald/norman_2"),
                    Identifier.of("minecraft", "textures/entity/player/slim/alex.png")
            )
    );
    public static final Item INCLEMENTEMERALD_LEADER_WINONA_TICKET = new TrainerTicket(
            new Item.Settings(),
            new SelectedTrainerEntityFactory(
                    Identifier.of("trainer", "inclementemerald/winona_2"),
                    Identifier.of("minecraft", "textures/entity/player/slim/alex.png")
            )
    );
    public static final Item INCLEMENTEMERALD_LEADER_TATE_AND_LIZA_TICKET = new TrainerTicket(
            new Item.Settings(),
            new SelectedTrainerEntityFactory(
                    Identifier.of("trainer", "inclementemerald/tate_and_liza_2"),
                    Identifier.of("minecraft", "textures/entity/player/slim/alex.png")
            )
    );
    public static final Item INCLEMENTEMERALD_LEADER_JUAN_TICKET = new TrainerTicket(
            new Item.Settings(),
            new SelectedTrainerEntityFactory(
                    Identifier.of("trainer", "inclementemerald/juan_2"),
                    Identifier.of("minecraft", "textures/entity/player/slim/alex.png")
            )
    );
}
