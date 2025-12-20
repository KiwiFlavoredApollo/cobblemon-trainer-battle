package kiwiapollo.cobblemontrainerbattle.villager;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.block.CustomBlock;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;

public class CustomPointOfInterestType {
    public static final RegistryKey<PointOfInterestType> POKE_BALL_ENGINEER = of(PokeBallEngineer.NAME);

    private static RegistryKey<PointOfInterestType> of(String name) {
        return RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, Identifier.of(CobblemonTrainerBattle.MOD_ID, name));
    }

    static {
        PointOfInterestHelper.register(POKE_BALL_ENGINEER.getValue(), 1, 1, CustomBlock.POKE_BALL_BOX);
    }
}
