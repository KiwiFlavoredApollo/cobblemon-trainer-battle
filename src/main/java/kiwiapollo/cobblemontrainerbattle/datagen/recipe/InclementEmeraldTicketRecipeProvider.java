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
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.LEADER_ROXANNE_TICKET.getItem())
                .input(MiscItem.TRAINER_TOKEN.getItem(), 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.ROCK_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ROCK_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ROCK_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.LEADER_BRAWLY_TICKET.getItem())
                .input(MiscItem.TRAINER_TOKEN.getItem(), 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FIGHTING_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIGHTING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIGHTING_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.LEADER_WATTSON_TICKET.getItem())
                .input(MiscItem.TRAINER_TOKEN.getItem(), 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.ELECTRIC_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ELECTRIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ELECTRIC_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.LEADER_FLANNERY_TICKET.getItem())
                .input(MiscItem.TRAINER_TOKEN.getItem(), 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FIRE_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIRE_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIRE_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.LEADER_NORMAN_TICKET.getItem())
                .input(MiscItem.TRAINER_TOKEN.getItem(), 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.NORMAL_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.NORMAL_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.NORMAL_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.LEADER_WINONA_TICKET.getItem())
                .input(MiscItem.TRAINER_TOKEN.getItem(), 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FLYING_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FLYING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FLYING_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.LEADER_TATE_AND_LIZA_TICKET.getItem())
                .input(MiscItem.TRAINER_TOKEN.getItem(), 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.PSYCHIC_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.PSYCHIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.PSYCHIC_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.LEADER_JUAN_TICKET.getItem())
                .input(MiscItem.TRAINER_TOKEN.getItem(), 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.WATER_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.WATER_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.WATER_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.ELITE_SIDNEY_TICKET.getItem())
                .input(InclementEmeraldTokenItem.LEADER_ROXANNE_TOKEN.getItem())
                .input(InclementEmeraldTokenItem.LEADER_BRAWLY_TOKEN.getItem())
                .input(CobblemonItems.BLACK_GLASSES)
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.LEADER_ROXANNE_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.LEADER_ROXANNE_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.LEADER_BRAWLY_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.LEADER_BRAWLY_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.ELITE_PHOEBE_TICKET.getItem())
                .input(InclementEmeraldTokenItem.LEADER_WATTSON_TOKEN.getItem())
                .input(InclementEmeraldTokenItem.LEADER_FLANNERY_TOKEN.getItem())
                .input(CobblemonItems.REAPER_CLOTH)
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.LEADER_WATTSON_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.LEADER_WATTSON_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.LEADER_FLANNERY_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.LEADER_FLANNERY_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.ELITE_GLACIA_TICKET.getItem())
                .input(InclementEmeraldTokenItem.LEADER_NORMAN_TOKEN.getItem())
                .input(InclementEmeraldTokenItem.LEADER_WINONA_TOKEN.getItem())
                .input(CobblemonItems.NEVER_MELT_ICE)
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.LEADER_NORMAN_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.LEADER_NORMAN_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.LEADER_WINONA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.LEADER_WINONA_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.ELITE_DRAKE_TICKET.getItem())
                .input(InclementEmeraldTokenItem.LEADER_TATE_AND_LIZA_TOKEN.getItem())
                .input(InclementEmeraldTokenItem.LEADER_JUAN_TOKEN.getItem())
                .input(CobblemonItems.DRAGON_SCALE)
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.LEADER_TATE_AND_LIZA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.LEADER_TATE_AND_LIZA_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.LEADER_JUAN_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.LEADER_JUAN_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTicketItem.CHAMPION_WALLACE_TICKET.getItem())
                .input(InclementEmeraldTokenItem.ELITE_SIDNEY_TOKEN.getItem())
                .input(InclementEmeraldTokenItem.ELITE_PHOEBE_TOKEN.getItem())
                .input(InclementEmeraldTokenItem.ELITE_GLACIA_TOKEN.getItem())
                .input(InclementEmeraldTokenItem.ELITE_DRAKE_TOKEN.getItem())
                .input(Items.NETHER_STAR)
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.ELITE_SIDNEY_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.ELITE_SIDNEY_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.ELITE_PHOEBE_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.ELITE_PHOEBE_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.ELITE_GLACIA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.ELITE_GLACIA_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokenItem.ELITE_DRAKE_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokenItem.ELITE_DRAKE_TOKEN.getItem()))
                .offerTo(exporter);
    }
}
