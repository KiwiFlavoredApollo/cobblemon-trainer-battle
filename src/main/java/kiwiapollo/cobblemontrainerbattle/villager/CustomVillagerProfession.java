package kiwiapollo.cobblemontrainerbattle.villager;

import com.google.common.collect.ImmutableSet;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class CustomVillagerProfession {
    public static final VillagerProfession POKE_BALL_ENGINEER = register(PokeBallEngineer.NAME, CustomPointOfInterestType.POKE_BALL_ENGINEER);

    public static void initialize() {

    }

    private static VillagerProfession register(String name, RegistryKey<PointOfInterestType> type) {
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, name);
        VillagerProfession profession = new VillagerProfession(
                name,
                entry -> entry.matchesKey(type),
                entry -> entry.matchesKey(type),
                ImmutableSet.of(),
                ImmutableSet.of(),
                null
        );

        return Registry.register(Registries.VILLAGER_PROFESSION, identifier, profession);
    }
}
