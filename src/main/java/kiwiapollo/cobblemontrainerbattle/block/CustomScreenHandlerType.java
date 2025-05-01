package kiwiapollo.cobblemontrainerbattle.block;

import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;

public class CustomScreenHandlerType {
    public static final ScreenHandlerType<TrainerTableScreenHandler> TRAINER_TABLE;

    static {
        TRAINER_TABLE = new ScreenHandlerType<>(TrainerTableScreenHandler::new, FeatureSet.empty());
    }
}
