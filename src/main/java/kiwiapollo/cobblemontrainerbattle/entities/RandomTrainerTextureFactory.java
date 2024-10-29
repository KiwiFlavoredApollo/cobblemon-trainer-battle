package kiwiapollo.cobblemontrainerbattle.entities;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.IdentifierFactory;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomTrainerTextureFactory implements IdentifierFactory {
    private static final String TEXTURE_PARENTS = "textures/entity/trainer/slim/";
    private static final List<String> TEXTURE_FILES = List.of(
            "red_piikapiika.png",
            "green_piikapiika.png",
            "leaf_piikapiika.png",
            "alola_leaf_piikapiika.png",
            "silver_piikapiika.png",
            "black_hilbert_piikapiika.png",
            "white_hilda_piikapiika.png",

            "blacksmith_roxie_idkgraceorsmth.png",
            "cherry_blossom_garden_selene_idkgraceorsmth.png",
            "diner_waitress_mia_idkgraceorsmth.png"
    );
    private static final List<Identifier> TEXTURES = TEXTURE_FILES.stream()
            .map(file -> TEXTURE_PARENTS + file)
            .map(path -> Identifier.of(CobblemonTrainerBattle.MOD_ID, path)).toList();

    @Override
    public Identifier create() {
        List<Identifier> textures = new ArrayList<>(TEXTURES);
        Collections.shuffle(textures);
        return textures.get(0);
    }
}
