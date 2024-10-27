package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
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

        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "radicalred_leader_brock_ticket"), RADICALRED_LEADER_BROCK_TICKET);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonTrainerBattle.NAMESPACE, "radicalred_leader_brock_token"), RADICALRED_LEADER_BROCK_TOKEN);

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY, ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(TRAINER_SPAWN_EGG);

            itemGroup.add(BLUE_VS_SEEKER);
            itemGroup.add(RED_VS_SEEKER);
            itemGroup.add(GREEN_VS_SEEKER);
            itemGroup.add(PURPLE_VS_SEEKER);

            itemGroup.add(TRAINER_TOKEN);

            itemGroup.add(RADICALRED_LEADER_BROCK_TICKET);
            itemGroup.add(RADICALRED_LEADER_BROCK_TOKEN);
        });
    }

    public static final Item TRAINER_SPAWN_EGG = new SpawnEggItem(CobblemonTrainerBattle.TRAINER_ENTITY_TYPE, 0xAAAAAA, 0xFF5555, new FabricItemSettings().maxCount(64));

    public static final Item BLUE_VS_SEEKER = new Item(new Item.Settings());
    public static final Item RED_VS_SEEKER = new Item(new Item.Settings());
    public static final Item GREEN_VS_SEEKER = new Item(new Item.Settings());
    public static final Item PURPLE_VS_SEEKER = new Item(new Item.Settings());

    public static final Item TRAINER_TOKEN = new Item(new Item.Settings());

    public static final Item RADICALRED_LEADER_BROCK_TICKET = new Item(new Item.Settings());
    public static final Item RADICALRED_LEADER_BROCK_TOKEN = new Item(new Item.Settings());

    public static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(CobblemonTrainerBattle.NAMESPACE, "item_group"));
    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder().icon(() -> new ItemStack(BLUE_VS_SEEKER)).displayName(Text.literal("Trainers")).build();
}
