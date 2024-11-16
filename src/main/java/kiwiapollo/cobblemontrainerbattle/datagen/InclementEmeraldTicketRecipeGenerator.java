package kiwiapollo.cobblemontrainerbattle.datagen;

import com.cobblemon.mod.common.CobblemonItems;
import kiwiapollo.cobblemontrainerbattle.item.InclementEmeraldTickets;
import kiwiapollo.cobblemontrainerbattle.item.InclementEmeraldTokens;
import kiwiapollo.cobblemontrainerbattle.item.MiscItems;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class InclementEmeraldTicketRecipeGenerator implements ItemRecipeGenerator {
    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTickets.LEADER_ROXANNE_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.ROCK_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ROCK_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ROCK_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTickets.LEADER_BRAWLY_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FIGHTING_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIGHTING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIGHTING_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTickets.LEADER_WATTSON_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.ELECTRIC_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ELECTRIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ELECTRIC_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTickets.LEADER_FLANNERY_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FIRE_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIRE_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIRE_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTickets.LEADER_NORMAN_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.NORMAL_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.NORMAL_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.NORMAL_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTickets.LEADER_WINONA_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.FLYING_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FLYING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FLYING_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTickets.LEADER_TATE_AND_LIZA_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.PSYCHIC_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.PSYCHIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.PSYCHIC_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTickets.LEADER_JUAN_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.GREEN_DYE)
                .input(CobblemonItems.WATER_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.WATER_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.WATER_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTickets.ELITE_SIDNEY_TICKET.getItem())
                .input(InclementEmeraldTokens.LEADER_ROXANNE_TOKEN.getItem())
                .input(InclementEmeraldTokens.LEADER_BRAWLY_TOKEN.getItem())
                .input(CobblemonItems.BLACK_GLASSES)
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokens.LEADER_ROXANNE_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokens.LEADER_ROXANNE_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokens.LEADER_BRAWLY_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokens.LEADER_BRAWLY_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTickets.ELITE_PHOEBE_TICKET.getItem())
                .input(InclementEmeraldTokens.LEADER_WATTSON_TOKEN.getItem())
                .input(InclementEmeraldTokens.LEADER_FLANNERY_TOKEN.getItem())
                .input(CobblemonItems.REAPER_CLOTH)
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokens.LEADER_WATTSON_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokens.LEADER_WATTSON_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokens.LEADER_FLANNERY_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokens.LEADER_FLANNERY_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTickets.ELITE_GLACIA_TICKET.getItem())
                .input(InclementEmeraldTokens.LEADER_NORMAN_TOKEN.getItem())
                .input(InclementEmeraldTokens.LEADER_WINONA_TOKEN.getItem())
                .input(CobblemonItems.NEVER_MELT_ICE)
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokens.LEADER_NORMAN_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokens.LEADER_NORMAN_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokens.LEADER_WINONA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokens.LEADER_WINONA_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTickets.ELITE_DRAKE_TICKET.getItem())
                .input(InclementEmeraldTokens.LEADER_TATE_AND_LIZA_TOKEN.getItem())
                .input(InclementEmeraldTokens.LEADER_JUAN_TOKEN.getItem())
                .input(CobblemonItems.DRAGON_SCALE)
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokens.LEADER_TATE_AND_LIZA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokens.LEADER_TATE_AND_LIZA_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokens.LEADER_JUAN_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokens.LEADER_JUAN_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, InclementEmeraldTickets.CHAMPION_WALLACE_TICKET.getItem())
                .input(InclementEmeraldTokens.ELITE_SIDNEY_TOKEN.getItem())
                .input(InclementEmeraldTokens.ELITE_PHOEBE_TOKEN.getItem())
                .input(InclementEmeraldTokens.ELITE_GLACIA_TOKEN.getItem())
                .input(InclementEmeraldTokens.ELITE_DRAKE_TOKEN.getItem())
                .input(Items.NETHER_STAR)
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokens.ELITE_SIDNEY_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokens.ELITE_SIDNEY_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokens.ELITE_PHOEBE_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokens.ELITE_PHOEBE_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokens.ELITE_GLACIA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokens.ELITE_GLACIA_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(InclementEmeraldTokens.ELITE_DRAKE_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(InclementEmeraldTokens.ELITE_DRAKE_TOKEN.getItem()))
                .offerTo(exporter);
    }
}
