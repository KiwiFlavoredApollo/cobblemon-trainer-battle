package kiwiapollo.cobblemontrainerbattle.datagen.recipe;

import com.cobblemon.mod.common.CobblemonItems;
import kiwiapollo.cobblemontrainerbattle.item.ticket.InclementEmeraldTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.token.InclementEmeraldTokenItem;
import kiwiapollo.cobblemontrainerbattle.item.misc.MiscItem;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class InclementEmeraldTicketRecipeProvider implements RecipeProviderBehavior {
    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.LEADER_ROXANNE_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.ROCK_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ROCK_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ROCK_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.LEADER_BRAWLY_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FIGHTING_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIGHTING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIGHTING_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.LEADER_WATTSON_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.ELECTRIC_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ELECTRIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ELECTRIC_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.LEADER_FLANNERY_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FIRE_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIRE_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIRE_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.LEADER_NORMAN_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.NORMAL_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.NORMAL_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.NORMAL_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.LEADER_WINONA_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FLYING_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FLYING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FLYING_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.LEADER_TATE_AND_LIZA_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.PSYCHIC_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.PSYCHIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.PSYCHIC_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.LEADER_JUAN_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.WATER_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.WATER_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.WATER_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.ELITE_SIDNEY_TICKET)
                .input(InclementEmeraldTokenItem.LEADER_ROXANNE_TOKEN)
                .input(InclementEmeraldTokenItem.LEADER_BRAWLY_TOKEN)
                .input(CobblemonItems.BLACK_GLASSES)
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.LEADER_ROXANNE_TOKEN), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.LEADER_ROXANNE_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.LEADER_BRAWLY_TOKEN), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.LEADER_BRAWLY_TOKEN))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.ELITE_PHOEBE_TICKET)
                .input(InclementEmeraldTokenItem.LEADER_WATTSON_TOKEN)
                .input(InclementEmeraldTokenItem.LEADER_FLANNERY_TOKEN)
                .input(CobblemonItems.REAPER_CLOTH)
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.LEADER_WATTSON_TOKEN), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.LEADER_WATTSON_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.LEADER_FLANNERY_TOKEN), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.LEADER_FLANNERY_TOKEN))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.ELITE_GLACIA_TICKET)
                .input(InclementEmeraldTokenItem.LEADER_NORMAN_TOKEN)
                .input(InclementEmeraldTokenItem.LEADER_WINONA_TOKEN)
                .input(CobblemonItems.NEVER_MELT_ICE)
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.LEADER_NORMAN_TOKEN), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.LEADER_NORMAN_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.LEADER_WINONA_TOKEN), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.LEADER_WINONA_TOKEN))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.ELITE_DRAKE_TICKET)
                .input(InclementEmeraldTokenItem.LEADER_TATE_AND_LIZA_TOKEN)
                .input(InclementEmeraldTokenItem.LEADER_JUAN_TOKEN)
                .input(CobblemonItems.DRAGON_SCALE)
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.LEADER_TATE_AND_LIZA_TOKEN), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.LEADER_TATE_AND_LIZA_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.LEADER_JUAN_TOKEN), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.LEADER_JUAN_TOKEN))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.CHAMPION_WALLACE_TICKET)
                .input(InclementEmeraldTokenItem.ELITE_SIDNEY_TOKEN)
                .input(InclementEmeraldTokenItem.ELITE_PHOEBE_TOKEN)
                .input(InclementEmeraldTokenItem.ELITE_GLACIA_TOKEN)
                .input(InclementEmeraldTokenItem.ELITE_DRAKE_TOKEN)
                .input(Items.NETHER_STAR)
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.ELITE_SIDNEY_TOKEN), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.ELITE_SIDNEY_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.ELITE_PHOEBE_TOKEN), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.ELITE_PHOEBE_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.ELITE_GLACIA_TOKEN), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.ELITE_GLACIA_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.ELITE_DRAKE_TOKEN), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.ELITE_DRAKE_TOKEN))
                .offerTo(exporter);
    }
}
