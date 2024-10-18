package kiwiapollo.cobblemontrainerbattle.parser;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.BattleFactoryProfile;
import kiwiapollo.cobblemontrainerbattle.common.TrainerGroupProfile;
import kiwiapollo.cobblemontrainerbattle.common.TrainerOption;
import kiwiapollo.cobblemontrainerbattle.common.TrainerProfile;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    public static final String TRAINER_TEAM_DIR = "trainers/teams";
    public static final String TRAINER_OPTION_DIR = "trainers/options";
    public static final String DEFAULT_TRAINER_OPTION = String.format("%s/%s", TRAINER_OPTION_DIR, "defaults.json");

    public static final String GROUP_DIR = "groups";

    public static final String MINIGAME_DIR = "minigames";
    public static final String BATTLE_FACTORY_PROFILE = String.format("%s/%s", MINIGAME_DIR, "battlefactory.json");

    @Override
    public Identifier getFabricId() {
        return Identifier.of(CobblemonTrainerBattle.NAMESPACE, "resourceloader");
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        CobblemonTrainerBattle.trainerProfileRegistry = loadTrainerProfileRegistry(resourceManager);
        CobblemonTrainerBattle.trainerGroupProfileRegistry = loadTrainerGroupProfileRegistry(resourceManager);
        CobblemonTrainerBattle.battleFactoryProfile = loadBattleFactoryProfile(resourceManager);
    }

    private Map<Identifier, TrainerProfile> loadTrainerProfileRegistry(ResourceManager resourceManager) {
        List<String> namespaces = List.of(
                "radicalred",
                "inclementemerald",
                "smogon",
                "custom"
        );

        Map<Identifier, TrainerProfile> trainers = new HashMap<>();
        for (String namespace : namespaces) {
            trainers.putAll(loadTrainersByNamespace(resourceManager, namespace));
            CobblemonTrainerBattle.LOGGER.info(String.format("Loaded %s", namespace));
        }

        return trainers;
    }

    private Map<Identifier, TrainerProfile> loadTrainersByNamespace(ResourceManager resourceManager, String namespace) {
        TrainerOption defaultOption = loadDefaultTrainerOption(resourceManager);

        Map<Identifier, Resource> teamResourceMap = resourceManager.findResources(String.format("%s/%s", TRAINER_TEAM_DIR, namespace), this::isJsonFile);
        Map<Identifier, Resource> optionResourceMap = resourceManager.findResources(String.format("%s/%s", TRAINER_OPTION_DIR, namespace), this::isJsonFile);

        Map<Identifier, TrainerProfile> trainers = new HashMap<>();
        for (Map.Entry<Identifier, Resource> entry : teamResourceMap.entrySet()) {
            try {
                Identifier teamIdentifier = entry.getKey();
                Resource teamResource = entry.getValue();

                Identifier optionIdentifier = toOptionIdentifier(teamIdentifier);

                String name = Paths.get(teamIdentifier.getPath()).getFileName().toString().replace(".json", "");
                List<SmogonPokemon> team = readTeamResource(teamResource);

                TrainerOption option = defaultOption;
                if (optionResourceMap.containsKey(optionIdentifier)) {
                    option = readTrainerOptionResource(optionResourceMap.get(optionIdentifier));
                }

                trainers.put(
                        toNamespaceIdentifier(teamIdentifier),
                        new TrainerProfile(
                                name,
                                team,
                                option.onVictory,
                                option.onDefeat,
                                option.condition
                        )
                );

            } catch (JsonParseException | IOException e) {
                Identifier teamIdentifier = entry.getKey();
                CobblemonTrainerBattle.LOGGER.error("An error occurred while loading {}", teamIdentifier.toString());
            }
        }

        return trainers;
    }

    private TrainerOption loadDefaultTrainerOption(ResourceManager resourceManager) {
        try (InputStream inputStream = getDefaultTrainerOptionResource(resourceManager).getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, TrainerOption.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Identifier toOptionIdentifier(Identifier teamIdentifier) {
        String namespace = teamIdentifier.getNamespace();
        String path = teamIdentifier.getPath().replace(TRAINER_TEAM_DIR, TRAINER_OPTION_DIR);

        return Identifier.of(namespace, path);
    }

    private Identifier toNamespaceIdentifier(Identifier teamIdentifier) {
        String prefix = String.format("^%s/", TRAINER_TEAM_DIR);
        String suffix = "\\.json$";
        String trimmedPath = teamIdentifier.getPath().replaceAll(prefix, "").replaceAll(suffix, "");

        String[] parts = trimmedPath.split("/");

        StringBuilder remainingPath = new StringBuilder();
        for (int i = 1; i < parts.length; i++) {
            if (i == 1) {
                remainingPath.append(parts[i]);
            } else {
                remainingPath.append("/");
                remainingPath.append(parts[i]);
            }
        }

        String namespace = parts[0];
        String path = remainingPath.toString();

        return Identifier.of(namespace, path);
    }

    private Map<Identifier, TrainerGroupProfile> loadTrainerGroupProfileRegistry(ResourceManager resourceManager) {
        Map<Identifier, TrainerGroupProfile> trainerGroupProfileMap = new HashMap<>();

        resourceManager.findResources(GROUP_DIR, this::isJsonFile).forEach(((identifier, resource) -> {
            try (InputStream inputStream = resourceManager.getResourceOrThrow(identifier).getInputStream()) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                TrainerGroupProfile trainerGroupProfile = new Gson().fromJson(bufferedReader, TrainerGroupProfile.class);
                trainerGroupProfileMap.put(identifier, trainerGroupProfile);

            } catch (IOException ignored) {

            }
        }));

        return trainerGroupProfileMap;
    }

    private BattleFactoryProfile loadBattleFactoryProfile(ResourceManager resourceManager) {
        try (InputStream inputStream = getBattleFactoryProfileResource(resourceManager).getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, BattleFactoryProfile.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Resource getDefaultTrainerOptionResource(ResourceManager resourceManager)
            throws FileNotFoundException {
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, DEFAULT_TRAINER_OPTION);
        return resourceManager.getResourceOrThrow(identifier);
    }

    private List<SmogonPokemon> readTeamResource(Resource resource) throws IOException, JsonParseException {
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, new TypeToken<List<SmogonPokemon>>(){}.getType());
        }
    }

    private TrainerOption readTrainerOptionResource(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, TrainerOption.class);
        }
    }

    private Resource getBattleFactoryProfileResource(ResourceManager resourceManager) throws FileNotFoundException {
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, BATTLE_FACTORY_PROFILE);
        return resourceManager.getResourceOrThrow(identifier);
    }

    private boolean isJsonFile(Identifier identifier) {
        return identifier.toString().endsWith(".json");
    }
}
