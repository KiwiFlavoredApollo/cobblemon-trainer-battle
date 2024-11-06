package kiwiapollo.cobblemontrainerbattle.parser.profile;

import com.google.gson.Gson;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactoryProfile;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.*;

public class MiniGameProfileLoader implements SimpleSynchronousResourceReloadListener {
    private static final String MINIGAME_DIR = "minigames";
    private static final String BATTLE_FACTORY_PROFILE_PATH = String.format("%s/%s", MINIGAME_DIR, "battlefactory.json");

    @Override
    public Identifier getFabricId() {
        return Identifier.of(CobblemonTrainerBattle.MOD_ID, "mini_game_profile_loader");
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        reloadBattleFactoryProfile(resourceManager);
    }

    private void reloadBattleFactoryProfile(ResourceManager resourceManager) {
        try (InputStream inputStream = getBattleFactoryProfileResource(resourceManager).getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            MiniGameProfileStorage.setBattleFactoryProfile(new Gson().fromJson(bufferedReader, BattleFactoryProfile.class));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Resource getBattleFactoryProfileResource(ResourceManager resourceManager) throws FileNotFoundException {
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, BATTLE_FACTORY_PROFILE_PATH);
        return resourceManager.getResourceOrThrow(identifier);
    }
}
