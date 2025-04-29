package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.util.Identifier;

public enum TrainerTexture {
    RED("textures/entity/trainer/slim/red_piikapiika.png"),
    GREEN("textures/entity/trainer/slim/green_piikapiika.png"),
    LEAF("textures/entity/trainer/slim/leaf_piikapiika.png"),
    ALOLA_LEAF("textures/entity/trainer/slim/alola_leaf_piikapiika.png"),
    SILVER("textures/entity/trainer/slim/silver_piikapiika.png"),
    BLACK_HILBERT("textures/entity/trainer/slim/black_hilbert_piikapiika.png"),
    WHITE_HILDA("textures/entity/trainer/slim/white_hilda_piikapiika.png");

    private final String path;

    TrainerTexture(String path) {
        this.path = path;
    }
    
    Identifier getIdentifier() {
        return Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
    }
}
