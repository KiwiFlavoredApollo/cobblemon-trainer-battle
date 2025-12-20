package kiwiapollo.cobblemontrainerbattle.datagen.recipe;

import com.cobblemon.mod.common.CobblemonItems;
import kiwiapollo.cobblemontrainerbattle.item.misc.MiscItem;
import kiwiapollo.cobblemontrainerbattle.item.ticket.RadicalRedTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.token.RadicalRedTokenItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class RadicalRedTicketRecipeProvider extends FabricRecipeProvider {
    public RadicalRedTicketRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.LEADER_BROCK_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.ROCK_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ROCK_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ROCK_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.LEADER_MISTY_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.WATER_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.WATER_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.WATER_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.LEADER_LT_SURGE_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.ELECTRIC_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ELECTRIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ELECTRIC_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.LEADER_ERIKA_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.GRASS_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.GRASS_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.GRASS_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.LEADER_KOGA_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.POISON_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.POISON_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.POISON_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.LEADER_SABRINA_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.PSYCHIC_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.PSYCHIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.PSYCHIC_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.LEADER_BLAINE_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.FIRE_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIRE_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIRE_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.LEADER_GIOVANNI_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.GROUND_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.GROUND_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.GROUND_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.LEADER_FALKNER_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.FLYING_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FLYING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FLYING_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.LEADER_BUGSY_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.BUG_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.BUG_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.BUG_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.LEADER_WHITNEY_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.NORMAL_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.NORMAL_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.NORMAL_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.LEADER_MORTY_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.GHOST_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.GHOST_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.GHOST_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.LEADER_CHUCK_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.FIGHTING_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIGHTING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIGHTING_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.LEADER_JASMINE_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.STEEL_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.STEEL_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.STEEL_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.LEADER_PRYCE_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.ICE_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ICE_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ICE_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.LEADER_CLAIR_TICKET)
                .input(MiscItem.TRAINER_TOKEN, 6)
                .input(Items.RED_DYE)
                .input(CobblemonItems.DRAGON_GEM)
                .criterion(FabricRecipeProvider.hasItem(MiscItem.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(MiscItem.TRAINER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(CobblemonItems.DRAGON_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.DRAGON_GEM))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.ELITE_LORELEI_TICKET)
                .input(RadicalRedTokenItem.LEADER_BROCK_TOKEN)
                .input(RadicalRedTokenItem.LEADER_MISTY_TOKEN)
                .input(RadicalRedTokenItem.LEADER_LT_SURGE_TOKEN)
                .input(RadicalRedTokenItem.LEADER_ERIKA_TOKEN)
                .input(CobblemonItems.MYSTIC_WATER)
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.LEADER_BROCK_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.LEADER_BROCK_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.LEADER_MISTY_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.LEADER_MISTY_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.LEADER_LT_SURGE_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.LEADER_LT_SURGE_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.LEADER_ERIKA_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.LEADER_ERIKA_TOKEN))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.ELITE_BRUNO_TICKET)
                .input(RadicalRedTokenItem.LEADER_KOGA_TOKEN)
                .input(RadicalRedTokenItem.LEADER_SABRINA_TOKEN)
                .input(RadicalRedTokenItem.LEADER_BLAINE_TOKEN)
                .input(RadicalRedTokenItem.LEADER_GIOVANNI_TOKEN)
                .input(CobblemonItems.BLACK_BELT)
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.LEADER_KOGA_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.LEADER_KOGA_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.LEADER_SABRINA_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.LEADER_SABRINA_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.LEADER_BLAINE_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.LEADER_BLAINE_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.LEADER_GIOVANNI_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.LEADER_GIOVANNI_TOKEN))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.ELITE_AGATHA_TICKET)
                .input(RadicalRedTokenItem.LEADER_FALKNER_TOKEN)
                .input(RadicalRedTokenItem.LEADER_BUGSY_TOKEN)
                .input(RadicalRedTokenItem.LEADER_WHITNEY_TOKEN)
                .input(RadicalRedTokenItem.LEADER_MORTY_TOKEN)
                .input(CobblemonItems.SPELL_TAG)
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.LEADER_FALKNER_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.LEADER_FALKNER_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.LEADER_BUGSY_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.LEADER_BUGSY_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.LEADER_WHITNEY_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.LEADER_WHITNEY_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.LEADER_MORTY_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.LEADER_MORTY_TOKEN))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.ELITE_LANCE_TICKET)
                .input(RadicalRedTokenItem.LEADER_CHUCK_TOKEN)
                .input(RadicalRedTokenItem.LEADER_JASMINE_TOKEN)
                .input(RadicalRedTokenItem.LEADER_PRYCE_TOKEN)
                .input(RadicalRedTokenItem.LEADER_CLAIR_TOKEN)
                .input(CobblemonItems.DRAGON_FANG)
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.LEADER_CHUCK_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.LEADER_CHUCK_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.LEADER_JASMINE_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.LEADER_JASMINE_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.LEADER_PRYCE_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.LEADER_PRYCE_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.LEADER_CLAIR_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.LEADER_CLAIR_TOKEN))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RadicalRedTicketItem.CHAMPION_TERRY_TICKET)
                .input(RadicalRedTokenItem.ELITE_LORELEI_TOKEN)
                .input(RadicalRedTokenItem.ELITE_BRUNO_TOKEN)
                .input(RadicalRedTokenItem.ELITE_AGATHA_TOKEN)
                .input(RadicalRedTokenItem.ELITE_LANCE_TOKEN)
                .input(Items.NETHER_STAR)
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.ELITE_LORELEI_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.ELITE_LORELEI_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.ELITE_BRUNO_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.ELITE_BRUNO_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.ELITE_AGATHA_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.ELITE_AGATHA_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(RadicalRedTokenItem.ELITE_LANCE_TOKEN), FabricRecipeProvider.conditionsFromItem(RadicalRedTokenItem.ELITE_LANCE_TOKEN))
                .offerTo(exporter);
    }
}
