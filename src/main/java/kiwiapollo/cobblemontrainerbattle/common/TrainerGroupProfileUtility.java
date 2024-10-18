package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.util.Identifier;

public class TrainerGroupProfileUtility {
    public static String toResourceName(Identifier identifier) {
        return identifier.getPath().replace("group/", "").replace(".json", "");
    }

    public static Identifier toResourceIdentifier(String resource) {
        return Identifier.of(CobblemonTrainerBattle.NAMESPACE, String.format("group/%s.json", resource));
    }
}
