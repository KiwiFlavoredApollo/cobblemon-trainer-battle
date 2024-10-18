package kiwiapollo.cobblemontrainerbattle.common;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import kiwiapollo.cobblemontrainerbattle.parser.SmogonPokemon;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    public static final String GROUP_DIR = "group";
    public static final String TRAINER_TEAM_DIR = "trainer/team";
    public static final String TRAINER_OPTION_DIR = "trainer/option";
    public static final String MINIGAME_DIR = "minigame";

    @Override
    public Identifier getFabricId() {
        return Identifier.of(CobblemonTrainerBattle.NAMESPACE, "resourceloader");
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        CobblemonTrainerBattle.trainerProfileRegistry = loadTrainers(resourceManager);
        CobblemonTrainerBattle.trainerGroupProfileRegistry = loadTrainerGroups(resourceManager);

        BattleFactory.configuration = loadBattleFactoryConfiguration(resourceManager);
    }

    private TrainerConfiguration loadDefaultTrainerConfiguration(ResourceManager resourceManager) {
        try (InputStream inputStream = getDefaultTrainerConfigurationResource(resourceManager).getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, TrainerConfiguration.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<Identifier, TrainerProfile> loadTrainers(ResourceManager resourceManager) {
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
        TrainerConfiguration defaultOption = loadDefaultTrainerConfiguration(resourceManager);

        Map<Identifier, Resource> teamResources = resourceManager.findResources(String.format("%s/%s", TRAINER_TEAM_DIR, namespace), this::isJsonFile);
        Map<Identifier, Resource> optionResources = resourceManager.findResources(String.format("%s/%s", TRAINER_OPTION_DIR, namespace), this::isJsonFile);

        Map<Identifier, TrainerProfile> trainers = new HashMap<>();
        for (Map.Entry<Identifier, Resource> entry : teamResources.entrySet()) {
            try {
                Identifier teamIdentifier = entry.getKey();
                Resource teamResource = entry.getValue();

                Identifier optionIdentifier = toOptionIdentifier(teamIdentifier);

                String name = Paths.get(teamIdentifier.getPath()).getFileName().toString().replace(".json", "");
                List<SmogonPokemon> pokemons = readSmogonPokemonResource(teamResource);

                TrainerConfiguration configuration = defaultOption;
                if (optionResources.containsKey(optionIdentifier)) {
                    configuration = readTrainerConfigurationResource(optionResources.get(optionIdentifier));
                }

                trainers.put(
                        toNamespaceIdentifier(teamIdentifier),
                        new TrainerProfile(
                                name,
                                pokemons,
                                configuration.onVictory,
                                configuration.onDefeat,
                                configuration.condition
                        )
                );

            } catch (JsonParseException | IOException e) {
                Identifier teamIdentifier = entry.getKey();
                CobblemonTrainerBattle.LOGGER.error("An error occurred while loading {}", teamIdentifier.toString());
            }
        }

        return trainers;
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

    private Map<Identifier, TrainerGroupProfile> loadTrainerGroups(ResourceManager resourceManager) {
        Map<Identifier, TrainerGroupProfile> trainerGroups = new HashMap<>();

        resourceManager.findResources(GROUP_DIR, this::isJsonFile).forEach(((identifier, resource) -> {
            try (InputStream inputStream = resourceManager.getResourceOrThrow(identifier).getInputStream()) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                TrainerGroupProfile trainerGroupProfile = new Gson().fromJson(bufferedReader, TrainerGroupProfile.class);
                trainerGroups.put(identifier, trainerGroupProfile);

            } catch (IOException ignored) {

            }
        }));

        return trainerGroups;
    }

    private BattleFactoryConfiguration loadBattleFactoryConfiguration(ResourceManager resourceManager) {
        try (InputStream inputStream = getBattleFactoryConfigurationResource(resourceManager).getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, BattleFactoryConfiguration.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Resource getDefaultTrainerConfigurationResource(ResourceManager resourceManager)
            throws FileNotFoundException {
        String path = String.format("%s/defaults.json", TRAINER_OPTION_DIR);
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, path);
        return resourceManager.getResourceOrThrow(identifier);
    }

    private List<SmogonPokemon> readSmogonPokemonResource(Resource resource) throws IOException, JsonParseException {
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, new TypeToken<List<SmogonPokemon>>(){}.getType());
        }
    }

    private TrainerConfiguration readTrainerConfigurationResource(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, TrainerConfiguration.class);
        }
    }

    private Resource getBattleFactoryConfigurationResource(ResourceManager resourceManager) throws FileNotFoundException {
        String path = String.format("%s/battlefactory.json", MINIGAME_DIR);
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, path);
        return resourceManager.getResourceOrThrow(identifier);
    }

    private boolean isJsonFile(Identifier identifier) {
        return identifier.toString().endsWith(".json");
    }
}
