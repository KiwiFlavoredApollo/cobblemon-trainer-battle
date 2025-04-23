package kiwiapollo.cobblemontrainerbattle.datagen;

import com.cobblemon.mod.common.CobblemonItems;
import kiwiapollo.cobblemontrainerbattle.item.MiscItems;
import kiwiapollo.cobblemontrainerbattle.item.RadicalRedTicketItems;
import kiwiapollo.cobblemontrainerbattle.item.RadicalRedTokenItems;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class RadicalRedTicketRecipeGenerator implements ItemRecipeGenerator {
    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.LEADER_BROCK_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.ROCK_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ROCK_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ROCK_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.LEADER_MISTY_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.WATER_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.WATER_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.WATER_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.LEADER_LT_SURGE_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.ELECTRIC_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ELECTRIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ELECTRIC_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.LEADER_ERIKA_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.GRASS_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.GRASS_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.GRASS_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.LEADER_KOGA_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.POISON_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.POISON_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.POISON_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.LEADER_SABRINA_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.PSYCHIC_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.PSYCHIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.PSYCHIC_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.LEADER_BLAINE_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.FIRE_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIRE_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIRE_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.LEADER_GIOVANNI_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.GROUND_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.GROUND_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.GROUND_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.LEADER_FALKNER_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.FLYING_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FLYING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FLYING_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.LEADER_BUGSY_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.BUG_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.BUG_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.BUG_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.LEADER_WHITNEY_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.NORMAL_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.NORMAL_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.NORMAL_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.LEADER_MORTY_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.GHOST_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.GHOST_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.GHOST_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.LEADER_CHUCK_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.FIGHTING_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIGHTING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIGHTING_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.LEADER_JASMINE_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.STEEL_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.STEEL_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.STEEL_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.LEADER_PRYCE_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.ICE_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ICE_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ICE_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.LEADER_CLAIR_TICKET.getItem())
                .input(MiscItems.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.DRAGON_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItems.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.DRAGON_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.DRAGON_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.ELITE_LORELEI_TICKET.getItem())
                .input(RadicalRedTokenItems.LEADER_BROCK_TOKEN.getItem())
                .input(RadicalRedTokenItems.LEADER_MISTY_TOKEN.getItem())
                .input(RadicalRedTokenItems.LEADER_LT_SURGE_TOKEN.getItem())
                .input(RadicalRedTokenItems.LEADER_ERIKA_TOKEN.getItem())
                .input(CobblemonItems.MYSTIC_WATER)
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.LEADER_BROCK_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.LEADER_BROCK_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.LEADER_MISTY_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.LEADER_MISTY_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.LEADER_LT_SURGE_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.LEADER_LT_SURGE_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.LEADER_ERIKA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.LEADER_ERIKA_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.ELITE_BRUNO_TICKET.getItem())
                .input(RadicalRedTokenItems.LEADER_KOGA_TOKEN.getItem())
                .input(RadicalRedTokenItems.LEADER_SABRINA_TOKEN.getItem())
                .input(RadicalRedTokenItems.LEADER_BLAINE_TOKEN.getItem())
                .input(RadicalRedTokenItems.LEADER_GIOVANNI_TOKEN.getItem())
                .input(CobblemonItems.BLACK_BELT)
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.LEADER_KOGA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.LEADER_KOGA_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.LEADER_SABRINA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.LEADER_SABRINA_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.LEADER_BLAINE_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.LEADER_BLAINE_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.LEADER_GIOVANNI_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.LEADER_GIOVANNI_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.ELITE_AGATHA_TICKET.getItem())
                .input(RadicalRedTokenItems.LEADER_FALKNER_TOKEN.getItem())
                .input(RadicalRedTokenItems.LEADER_BUGSY_TOKEN.getItem())
                .input(RadicalRedTokenItems.LEADER_WHITNEY_TOKEN.getItem())
                .input(RadicalRedTokenItems.LEADER_MORTY_TOKEN.getItem())
                .input(CobblemonItems.SPELL_TAG)
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.LEADER_FALKNER_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.LEADER_FALKNER_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.LEADER_BUGSY_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.LEADER_BUGSY_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.LEADER_WHITNEY_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.LEADER_WHITNEY_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.LEADER_MORTY_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.LEADER_MORTY_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.ELITE_LANCE_TICKET.getItem())
                .input(RadicalRedTokenItems.LEADER_CHUCK_TOKEN.getItem())
                .input(RadicalRedTokenItems.LEADER_JASMINE_TOKEN.getItem())
                .input(RadicalRedTokenItems.LEADER_PRYCE_TOKEN.getItem())
                .input(RadicalRedTokenItems.LEADER_CLAIR_TOKEN.getItem())
                .input(CobblemonItems.DRAGON_FANG)
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.LEADER_CHUCK_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.LEADER_CHUCK_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.LEADER_JASMINE_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.LEADER_JASMINE_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.LEADER_PRYCE_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.LEADER_PRYCE_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.LEADER_CLAIR_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.LEADER_CLAIR_TOKEN.getItem()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItems.CHAMPION_TERRY_TICKET.getItem())
                .input(RadicalRedTokenItems.ELITE_LORELEI_TOKEN.getItem())
                .input(RadicalRedTokenItems.ELITE_BRUNO_TOKEN.getItem())
                .input(RadicalRedTokenItems.ELITE_AGATHA_TOKEN.getItem())
                .input(RadicalRedTokenItems.ELITE_LANCE_TOKEN.getItem())
                .input(Items.NETHER_STAR)
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.ELITE_LORELEI_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.ELITE_LORELEI_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.ELITE_BRUNO_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.ELITE_BRUNO_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.ELITE_AGATHA_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.ELITE_AGATHA_TOKEN.getItem()))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItems.ELITE_LANCE_TOKEN.getItem()), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItems.ELITE_LANCE_TOKEN.getItem()))
                .offerTo(exporter);
    }
}
