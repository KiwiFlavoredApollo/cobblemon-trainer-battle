package kiwiapollo.cobblemontrainerbattle.block;

import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;

public class CustomScreenHandlerType {
    public static final ScreenHandlerType<PokeBallBoxScreenHandler> POKE_BALL_BOX;

    static {
        POKE_BALL_BOX = new ScreenHandlerType<>(PokeBallBoxScreenHandler::new, FeatureSet.empty());
    }
}
