package kiwiapollo.cobblemontrainerbattle.block;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class CustomScreenHandlerType {
    public static final ScreenHandlerType<PokeBallBoxScreenHandler> POKE_BALL_BOX = register("poke_ball_box", PokeBallBoxScreenHandler::new);

    public static void initialize() {

    }

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, Identifier.of(CobblemonTrainerBattle.MOD_ID, name), new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
    }
}
