package kiwiapollo.cobblemontrainerbattle.datagen.recipe;

import com.cobblemon.mod.common.CobblemonItems;
import kiwiapollo.cobblemontrainerbattle.item.ticket.BdspTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.token.BdspTokenItem;
import kiwiapollo.cobblemontrainerbattle.item.misc.MiscItem;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class BdspTicketRecipeProvider implements RecipeProviderBehavior {
    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, BdspTicketItem.LEADER_ROARK_TICKET.getItem())
                .input(MiscItem.TRAINER_TOKEN.getItem(), 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.ROCK_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ROCK_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ROCK_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, BdspTicketItem.LEADER_GARDENIA_TICKET.getItem())
                .input(MiscItem.TRAINER_TOKEN.getItem(), 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FIGHTING_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIGHTING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIGHTING_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, BdspTicketItem.LEADER_MAYLENE_TICKET.getItem())
                .input(MiscItem.TRAINER_TOKEN.getItem(), 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.ELECTRIC_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ELECTRIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ELECTRIC_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, BdspTicketItem.LEADER_CRASHER_WAKE_TICKET.getItem())
                .input(MiscItem.TRAINER_TOKEN.getItem(), 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FIRE_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIRE_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIRE_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, BdspTicketItem.LEADER_FANTINA_TICKET.getItem())
                .input(MiscItem.TRAINER_TOKEN.getItem(), 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.NORMAL_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.NORMAL_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.NORMAL_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, BdspTicketItem.LEADER_BYRON_TICKET.getItem())
                .input(MiscItem.TRAINER_TOKEN.getItem(), 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FLYING_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FLYING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FLYING_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, BdspTicketItem.LEADER_CANDICE_TICKET.getItem())
                .input(MiscItem.TRAINER_TOKEN.getItem(), 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.PSYCHIC_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.PSYCHIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.PSYCHIC_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, BdspTicketItem.LEADER_VOLKNER_TICKET.getItem())
                .input(MiscItem.TRAINER_TOKEN.getItem(), 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.WATER_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.WATER_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.WATER_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, BdspTicketItem.ELITE_AARON_TICKET.getItem())
                .input(BdspTokenItem.LEADER_ROARK_TOKEN.getItem())
                .input(BdspTokenItem.LEADER_GARDENIA_TOKEN.getItem())
                .input(CobblemonItems.BLACK_GLASSES)
                .criterion(FabricRecipeProvider.hasItem(BdspTokenItem.LEADER_ROARK_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(BdspTokenItem.LEADER_ROARK_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(BdspTokenItem.LEADER_GARDENIA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(BdspTokenItem.LEADER_GARDENIA_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, BdspTicketItem.ELITE_BERTHA_TICKET.getItem())
                .input(BdspTokenItem.LEADER_MAYLENE_TOKEN.getItem())
                .input(BdspTokenItem.LEADER_CRASHER_WAKE_TOKEN.getItem())
                .input(CobblemonItems.REAPER_CLOTH)
                .criterion(FabricRecipeProvider.hasItem(BdspTokenItem.LEADER_MAYLENE_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(BdspTokenItem.LEADER_MAYLENE_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(BdspTokenItem.LEADER_CRASHER_WAKE_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(BdspTokenItem.LEADER_CRASHER_WAKE_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, BdspTicketItem.ELITE_FLINT_TICKET.getItem())
                .input(BdspTokenItem.LEADER_FANTINA_TOKEN.getItem())
                .input(BdspTokenItem.LEADER_BYRON_TOKEN.getItem())
                .input(CobblemonItems.NEVER_MELT_ICE)
                .criterion(FabricRecipeProvider.hasItem(BdspTokenItem.LEADER_FANTINA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(BdspTokenItem.LEADER_FANTINA_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(BdspTokenItem.LEADER_BYRON_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(BdspTokenItem.LEADER_BYRON_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, BdspTicketItem.ELITE_LUCIAN_TICKET.getItem())
                .input(BdspTokenItem.LEADER_CANDICE_TOKEN.getItem())
                .input(BdspTokenItem.LEADER_VOLKNER_TOKEN.getItem())
                .input(CobblemonItems.DRAGON_SCALE)
                .criterion(FabricRecipeProvider.hasItem(BdspTokenItem.LEADER_CANDICE_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(BdspTokenItem.LEADER_CANDICE_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(BdspTokenItem.LEADER_VOLKNER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(BdspTokenItem.LEADER_VOLKNER_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, BdspTicketItem.CHAMPION_CYNTHIA_TICKET.getItem())
                .input(BdspTokenItem.ELITE_AARON_TOKEN.getItem())
                .input(BdspTokenItem.ELITE_BERTHA_TOKEN.getItem())
                .input(BdspTokenItem.ELITE_FLINT_TOKEN.getItem())
                .input(BdspTokenItem.ELITE_LUCIAN_TOKEN.getItem())
                .input(Items.NETHER_STAR)
                .criterion(FabricRecipeProvider.hasItem(BdspTokenItem.ELITE_AARON_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(BdspTokenItem.ELITE_AARON_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(BdspTokenItem.ELITE_BERTHA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(BdspTokenItem.ELITE_BERTHA_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(BdspTokenItem.ELITE_FLINT_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(BdspTokenItem.ELITE_FLINT_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(BdspTokenItem.ELITE_LUCIAN_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(BdspTokenItem.ELITE_LUCIAN_TOKEN.getItem()))
                .offerTo(exporter);
    }
}
