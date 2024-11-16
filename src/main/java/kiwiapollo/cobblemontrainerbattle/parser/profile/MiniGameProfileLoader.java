package kiwiapollo.cobblemontrainerbattle.parser.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.battlefactory.BattleFactoryProfile;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.*;

public class MiniGameProfileLoader implements SimpleSynchronousResourceReloadListener {
    private static final String MINIGAME_DIR = "minigames";
    private static final String BATTLE_FACTORY_PROFILE_PATH = String.format("%s/%s", MINIGAME_DIR, "battlefactory.json");
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ItemStack.class, new HeldItemDeserializer())
            .create();

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
            MiniGameProfileStorage.setBattleFactoryProfile(GSON.fromJson(bufferedReader, BattleFactoryProfile.class));

        } catch (JsonParseException | IOException e) {
            CobblemonTrainerBattle.LOGGER.error("An error occurred while loading {}", BATTLE_FACTORY_PROFILE_PATH);
            throw new RuntimeException(e);
        }
    }

    private Resource getBattleFactoryProfileResource(ResourceManager resourceManager) throws FileNotFoundException {
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, BATTLE_FACTORY_PROFILE_PATH);
        return resourceManager.getResourceOrThrow(identifier);
    }
}
