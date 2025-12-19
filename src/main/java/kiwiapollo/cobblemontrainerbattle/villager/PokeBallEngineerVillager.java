package kiwiapollo.cobblemontrainerbattle.villager;

import com.google.common.collect.ImmutableSet;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.block.CustomBlock;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class PokeBallEngineerVillager {
    public static final String POI_ID = "pokeballengineer_poi";
    public static final String PROFESSION_ID = "pokeballengineer";

    public static final RegistryKey<PointOfInterestType> POI_KEY = RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, Identifier.of(CobblemonTrainerBattle.MOD_ID, PokeBallEngineerVillager.POI_ID));
    public static final PointOfInterestType POI_TYPE = PointOfInterestHelper.register(Identifier.of(CobblemonTrainerBattle.MOD_ID, POI_ID), 1, 1, CustomBlock.POKE_BALL_BOX);
    public static final VillagerProfession PROFESSION = new VillagerProfession(
            PROFESSION_ID,
            entry -> entry.matchesKey(POI_KEY),
            entry -> entry.matchesKey(POI_KEY),
            ImmutableSet.of(),
            ImmutableSet.of(),
            null
    );
}
