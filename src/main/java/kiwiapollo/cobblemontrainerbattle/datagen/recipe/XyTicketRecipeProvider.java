package kiwiapollo.cobblemontrainerbattle.datagen.recipe;

import com.cobblemon.mod.common.CobblemonItems;
import kiwiapollo.cobblemontrainerbattle.item.ticket.XyTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.token.XyTokenItem;
import kiwiapollo.cobblemontrainerbattle.item.misc.MiscItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class XyTicketRecipeProvider extends FabricRecipeProvider {
    public XyTicketRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.LEADER_VIOLA_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.ROCK_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ROCK_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ROCK_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.LEADER_GRANT_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FIGHTING_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIGHTING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIGHTING_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.LEADER_KORRINA_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.ELECTRIC_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ELECTRIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ELECTRIC_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.LEADER_RAMOS_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FIRE_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIRE_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIRE_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.LEADER_CLEMONT_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.NORMAL_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.NORMAL_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.NORMAL_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.LEADER_VALERIE_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FLYING_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FLYING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FLYING_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.LEADER_OLYMPIA_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.PSYCHIC_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.PSYCHIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.PSYCHIC_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.LEADER_WULFRIC_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.WATER_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.WATER_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.WATER_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.ELITE_WIKSTROM_TICKET)
                .input(XyTokenItem.LEADER_VIOLA_TOKEN)
                .input(XyTokenItem.LEADER_GRANT_TOKEN)
                .input(CobblemonItems.BLACK_GLASSES)
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.LEADER_VIOLA_TOKEN), FabricRecipeProvider.conditionsFromItem(XyTokenItem.LEADER_VIOLA_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.LEADER_GRANT_TOKEN), FabricRecipeProvider.conditionsFromItem(XyTokenItem.LEADER_GRANT_TOKEN))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.ELITE_MALVA_TICKET)
                .input(XyTokenItem.LEADER_KORRINA_TOKEN)
                .input(XyTokenItem.LEADER_RAMOS_TOKEN)
                .input(CobblemonItems.REAPER_CLOTH)
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.LEADER_KORRINA_TOKEN), FabricRecipeProvider.conditionsFromItem(XyTokenItem.LEADER_KORRINA_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.LEADER_RAMOS_TOKEN), FabricRecipeProvider.conditionsFromItem(XyTokenItem.LEADER_RAMOS_TOKEN))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.ELITE_DRASNA_TICKET)
                .input(XyTokenItem.LEADER_CLEMONT_TOKEN)
                .input(XyTokenItem.LEADER_VALERIE_TOKEN)
                .input(CobblemonItems.NEVER_MELT_ICE)
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.LEADER_CLEMONT_TOKEN), FabricRecipeProvider.conditionsFromItem(XyTokenItem.LEADER_CLEMONT_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.LEADER_VALERIE_TOKEN), FabricRecipeProvider.conditionsFromItem(XyTokenItem.LEADER_VALERIE_TOKEN))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.ELITE_SIEBOLD_TICKET)
                .input(XyTokenItem.LEADER_OLYMPIA_TOKEN)
                .input(XyTokenItem.LEADER_WULFRIC_TOKEN)
                .input(CobblemonItems.DRAGON_SCALE)
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.LEADER_OLYMPIA_TOKEN), FabricRecipeProvider.conditionsFromItem(XyTokenItem.LEADER_OLYMPIA_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.LEADER_WULFRIC_TOKEN), FabricRecipeProvider.conditionsFromItem(XyTokenItem.LEADER_WULFRIC_TOKEN))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, XyTicketItem.CHAMPION_DIANTHA_TICKET)
                .input(XyTokenItem.ELITE_WIKSTROM_TOKEN)
                .input(XyTokenItem.ELITE_MALVA_TOKEN)
                .input(XyTokenItem.ELITE_DRASNA_TOKEN)
                .input(XyTokenItem.ELITE_SIEBOLD_TOKEN)
                .input(Items.NETHER_STAR)
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.ELITE_WIKSTROM_TOKEN), FabricRecipeProvider.conditionsFromItem(XyTokenItem.ELITE_WIKSTROM_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.ELITE_MALVA_TOKEN), FabricRecipeProvider.conditionsFromItem(XyTokenItem.ELITE_MALVA_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.ELITE_DRASNA_TOKEN), FabricRecipeProvider.conditionsFromItem(XyTokenItem.ELITE_DRASNA_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(XyTokenItem.ELITE_SIEBOLD_TOKEN), FabricRecipeProvider.conditionsFromItem(XyTokenItem.ELITE_SIEBOLD_TOKEN))
                .offerTo(exporter);
    }
}
