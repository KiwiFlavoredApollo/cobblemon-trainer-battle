package kiwiapollo.cobblemontrainerbattle.parser;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerOption;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerProfile;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class TrainerProfileLoader implements SimpleSynchronousResourceReloadListener {
    private static final String TRAINER_TEAM_DIR = "trainers/teams";
    private static final String TRAINER_OPTION_DIR = "trainers/options";
    private static final String DEFAULT_TRAINER_OPTION_PATH = String.format("%s/%s", TRAINER_OPTION_DIR, "defaults.json");
    private static final Gson TRAINER_OPTION_GSON = new GsonBuilder().registerTypeAdapter(SoundEvent.class, new BattleThemeDeserializer()).create();

    @Override
    public Identifier getFabricId() {
        return Identifier.of(CobblemonTrainerBattle.MOD_ID, "trainer_profile_loader");
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        TrainerOption defaultOption = readDefaultTrainerOptionResource(getDefaultTrainerOptionResource(resourceManager));

        Map<Identifier, Resource> teamResourceMap = resourceManager.findResources(TRAINER_TEAM_DIR, this::isJsonFile);
        Map<Identifier, Resource> optionResourceMap = resourceManager.findResources(TRAINER_OPTION_DIR, this::isJsonFile);

        TrainerProfileStorage.clear();
        for (Map.Entry<Identifier, Resource> entry : teamResourceMap.entrySet()) {
            try {
                Identifier teamIdentifier = entry.getKey();
                Resource teamResource = entry.getValue();

                Identifier optionIdentifier = toOptionIdentifier(teamIdentifier);

                String name = Paths.get(teamIdentifier.getPath()).getFileName().toString().replace(".json", "");
                List<ShowdownPokemon> team = readTrainerTeamResource(teamResource);

                TrainerOption option = defaultOption;
                if (optionResourceMap.containsKey(optionIdentifier)) {
                    option = readTrainerOptionResource(optionResourceMap.get(optionIdentifier));
                }

                TrainerProfileStorage.put(
                        toTrainerIdentifier(teamIdentifier),
                        new TrainerProfile(
                                name,
                                team,
                                option.isSpawningAllowed,
                                option.condition,
                                option.battleTheme,
                                option.onVictory,
                                option.onDefeat
                        )
                );

            } catch (JsonParseException | IOException e) {
                Identifier teamIdentifier = entry.getKey();
                CobblemonTrainerBattle.LOGGER.error("An error occurred while loading {}", teamIdentifier.toString());
            }
        }
    }

    private Identifier toOptionIdentifier(Identifier teamIdentifier) {
        String namespace = teamIdentifier.getNamespace();
        String path = teamIdentifier.getPath().replace(TRAINER_TEAM_DIR, TRAINER_OPTION_DIR);

        return Identifier.of(namespace, path);
    }

    private Identifier toTrainerIdentifier(Identifier teamIdentifier) {
        String prefix = String.format("^%s/", TRAINER_TEAM_DIR);
        String suffix = "\\.json$";
        String path = teamIdentifier.getPath().replaceAll(prefix, "").replaceAll(suffix, "");

        return Identifier.of("trainer", path);
    }

    private Resource getDefaultTrainerOptionResource(ResourceManager resourceManager) {
        try {
            Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, DEFAULT_TRAINER_OPTION_PATH);
            return resourceManager.getResourceOrThrow(identifier);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<ShowdownPokemon> readTrainerTeamResource(Resource resource) throws IOException, JsonParseException {
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, new TypeToken<List<ShowdownPokemon>>(){}.getType());
        }
    }

    private TrainerOption readDefaultTrainerOptionResource(Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return TRAINER_OPTION_GSON.fromJson(bufferedReader, TrainerOption.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TrainerOption readTrainerOptionResource(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return TRAINER_OPTION_GSON.fromJson(bufferedReader, TrainerOption.class);
        }
    }

    private boolean isJsonFile(Identifier identifier) {
        return identifier.toString().endsWith(".json");
    }
}
