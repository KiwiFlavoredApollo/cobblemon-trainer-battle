package kiwiapollo.cobblemontrainerbattle.groupbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.parser.ResourceReloadListener;
import net.minecraft.util.Identifier;

public class TrainerGroupProfileUtility {
    public static String toResourceName(Identifier identifier) {
        return identifier.getPath().replace(String.format("%s/", ResourceReloadListener.GROUP_DIR), "").replace(".json", "");
    }

    public static Identifier toResourceIdentifier(String resource) {
        return Identifier.of(CobblemonTrainerBattle.NAMESPACE, String.format("%s/%s.json", ResourceReloadListener.GROUP_DIR, resource));
    }
}
