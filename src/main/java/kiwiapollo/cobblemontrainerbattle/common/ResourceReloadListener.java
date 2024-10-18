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
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    public static final String GROUP_CONFIG_DIR = "groups";
    public static final String TRAINER_CONFIG_DIR = "trainers";
    public static final String ARCADE_CONFIG_DIR = "arcades";

    public static TrainerConfiguration defaultTrainerConfiguration;

    @Override
    public Identifier getFabricId() {
        return Identifier.of(CobblemonTrainerBattle.NAMESPACE, "resourceloader");
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        defaultTrainerConfiguration = loadDefaultTrainerConfiguration(resourceManager);

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
        Map<Identifier, TrainerProfile> trainers = new HashMap<>();

        resourceManager.findResources(namespace, this::isJsonFile).forEach(((identifier, resource) -> {
            try {
                // identifier: cobblemontrainerbattle:custom/custom_trainer.json
                List<SmogonPokemon> pokemons = readSmogonPokemonResource(resource);
                TrainerConfiguration configuration = loadTrainerConfiguration(resourceManager, identifier);
                String name = Paths.get(identifier.getPath()).getFileName().toString().replace(".json", "");

                trainers.put(
                        identifier,
                        new TrainerProfile(
                                name,
                                pokemons,
                                configuration.onVictory,
                                configuration.onDefeat,
                                configuration.condition
                        )
                );

            } catch (JsonParseException | IOException e) {
                String message = String.format("An error occurred while loading %s", identifier.toString());
                CobblemonTrainerBattle.LOGGER.error(message);
            }
        }));

        return trainers;
    }

    private Map<Identifier, TrainerGroupProfile> loadTrainerGroups(ResourceManager resourceManager) {
        Map<Identifier, TrainerGroupProfile> trainerGroups = new HashMap<>();

        resourceManager.findResources(GROUP_CONFIG_DIR, this::isJsonFile).forEach(((identifier, resource) -> {
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
        String path = String.format("%s/defaults.json", TRAINER_CONFIG_DIR);
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, path);
        return resourceManager.getResourceOrThrow(identifier);
    }

    private Resource getTrainerConfigurationResource(ResourceManager resourceManager, Identifier trainerIdentifier)
            throws FileNotFoundException {
        String path = String.format("%s/%s", TRAINER_CONFIG_DIR, trainerIdentifier.getPath());
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, path);
        return resourceManager.getResourceOrThrow(identifier);
    }

    private List<SmogonPokemon> readSmogonPokemonResource(Resource resource) throws IOException, JsonParseException {
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, new TypeToken<List<SmogonPokemon>>(){}.getType());
        }
    }

    private TrainerConfiguration loadTrainerConfiguration(ResourceManager resourceManager, Identifier identifier) {
        try {
            Resource resource = getTrainerConfigurationResource(resourceManager, identifier);
            return readTrainerConfigurationResource(resource);

        } catch (IOException e) {
            return defaultTrainerConfiguration;
        }
    }

    private TrainerConfiguration readTrainerConfigurationResource(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, TrainerConfiguration.class);
        }
    }

    private Resource getBattleFactoryConfigurationResource(ResourceManager resourceManager) throws FileNotFoundException {
        String path = String.format("%s/battlefactory.json", ARCADE_CONFIG_DIR);
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, path);
        return resourceManager.getResourceOrThrow(identifier);
    }

    private boolean isJsonFile(Identifier identifier) {
        return identifier.toString().endsWith(".json");
    }
}
