package kiwiapollo.cobblemontrainerbattle.datagen;

import com.cobblemon.mod.common.CobblemonItems;
import kiwiapollo.cobblemontrainerbattle.item.XyTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.XyTokenItem;
import kiwiapollo.cobblemontrainerbattle.item.MiscItems;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class XyTicketRecipeGenerator implements ItemRecipeGenerator {
    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.LEADER_VIOLA_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.ROCK_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ROCK_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ROCK_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.LEADER_GRANT_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FIGHTING_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIGHTING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIGHTING_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.LEADER_KORRINA_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.ELECTRIC_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ELECTRIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ELECTRIC_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.LEADER_RAMOS_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FIRE_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIRE_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIRE_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.LEADER_CLEMONT_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.NORMAL_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.NORMAL_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.NORMAL_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.LEADER_VALERIE_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FLYING_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FLYING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FLYING_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.LEADER_OLYMPIA_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.PSYCHIC_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.PSYCHIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.PSYCHIC_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.LEADER_WULFRIC_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.WATER_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.WATER_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.WATER_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.ELITE_WIKSTROM_TICKET.getItem())
                .input(XyTokenItem.LEADER_VIOLA_TOKEN.getItem())
                .input(XyTokenItem.LEADER_GRANT_TOKEN.getItem())
                .input(CobblemonItems.BLACK_GLASSES)
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.LEADER_VIOLA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(XyTokenItem.LEADER_VIOLA_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.LEADER_GRANT_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(XyTokenItem.LEADER_GRANT_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.ELITE_MALVA_TICKET.getItem())
                .input(XyTokenItem.LEADER_KORRINA_TOKEN.getItem())
                .input(XyTokenItem.LEADER_RAMOS_TOKEN.getItem())
                .input(CobblemonItems.REAPER_CLOTH)
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.LEADER_KORRINA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(XyTokenItem.LEADER_KORRINA_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.LEADER_RAMOS_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(XyTokenItem.LEADER_RAMOS_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.ELITE_DRASNA_TICKET.getItem())
                .input(XyTokenItem.LEADER_CLEMONT_TOKEN.getItem())
                .input(XyTokenItem.LEADER_VALERIE_TOKEN.getItem())
                .input(CobblemonItems.NEVER_MELT_ICE)
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.LEADER_CLEMONT_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(XyTokenItem.LEADER_CLEMONT_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.LEADER_VALERIE_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(XyTokenItem.LEADER_VALERIE_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.ELITE_SIEBOLD_TICKET.getItem())
                .input(XyTokenItem.LEADER_OLYMPIA_TOKEN.getItem())
                .input(XyTokenItem.LEADER_WULFRIC_TOKEN.getItem())
                .input(CobblemonItems.DRAGON_SCALE)
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.LEADER_OLYMPIA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(XyTokenItem.LEADER_OLYMPIA_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.LEADER_WULFRIC_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(XyTokenItem.LEADER_WULFRIC_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.CHAMPION_DIANTHA_TICKET.getItem())
                .input(XyTokenItem.ELITE_WIKSTROM_TOKEN.getItem())
                .input(XyTokenItem.ELITE_MALVA_TOKEN.getItem())
                .input(XyTokenItem.ELITE_DRASNA_TOKEN.getItem())
                .input(XyTokenItem.ELITE_SIEBOLD_TOKEN.getItem())
                .input(Items.NETHER_STAR)
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.ELITE_WIKSTROM_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(XyTokenItem.ELITE_WIKSTROM_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.ELITE_MALVA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(XyTokenItem.ELITE_MALVA_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.ELITE_DRASNA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(XyTokenItem.ELITE_DRASNA_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.ELITE_SIEBOLD_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(XyTokenItem.ELITE_SIEBOLD_TOKEN.getItem()))
                .offerTo(exporter);
    }
}
