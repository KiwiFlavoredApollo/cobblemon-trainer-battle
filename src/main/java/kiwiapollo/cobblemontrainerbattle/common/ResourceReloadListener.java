package kiwiapollo.cobblemontrainerbattle.common;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.LoadingResourceFailedException;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return Identifier.of(CobblemonTrainerBattle.NAMESPACE, "resourceloader");
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        CobblemonTrainerBattle.defaultTrainerConfiguration = loadDefaultTrainerConfiguration(resourceManager);
        CobblemonTrainerBattle.battleFactoryConfiguration = loadBattleFactoryConfiguration(resourceManager);

        CobblemonTrainerBattle.trainers = loadTrainers(resourceManager);
        CobblemonTrainerBattle.trainerGroups = loadTrainerGroups(resourceManager);
    }

    private Map<Identifier, TrainerGroup> loadTrainerGroups(ResourceManager resourceManager) {
        Map<Identifier, TrainerGroup> trainerGroups = new HashMap<>();
        resourceManager.findResources(CobblemonTrainerBattle.GROUP_CONFIG_DIR, this::isJsonFile).forEach(((identifier, resource) -> {
            try (InputStream inputStream = resourceManager.getResourceOrThrow(identifier).getInputStream()) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                TrainerGroup trainerGroup = new Gson().fromJson(bufferedReader, TrainerGroup.class);
                trainerGroups.put(identifier, trainerGroup);
            } catch (IOException e) {

            }
        }));

        return trainerGroups;
    }

    private TrainerConfiguration loadDefaultTrainerConfiguration(ResourceManager resourceManager) {
        String path = String.format("%s/defaults.json", CobblemonTrainerBattle.TRAINER_CONFIG_DIR);
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, path);

        try (InputStream inputStream = resourceManager.getResourceOrThrow(identifier).getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, TrainerConfiguration.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<Identifier, Trainer> loadTrainers(ResourceManager resourceManager) {
        List<String> namespaces = List.of(
                "radicalred",
                "inclementemerald",
                "smogon",
                "custom"
        );

        Map<Identifier, Trainer> trainers = new HashMap<>();
        for (String namespace : namespaces) {
            resourceManager.findResources(namespace, this::isJsonFile).forEach(((identifier, resource) -> {
                try {
                    // identifier: cobblemontrainerbattle:custom/custom_trainer.json
                    List<SmogonPokemon> pokemons = loadTrainerPokemons(resource);
                    TrainerConfiguration configuration = loadTrainerConfiguration(resourceManager, identifier);

                    trainers.put(
                            identifier,
                            new Trainer(
                                    pokemons,
                                    configuration.onVictory,
                                    configuration.onDefeat,
                                    configuration.condition
                            )
                    );

                } catch (LoadingResourceFailedException e) {
                    CobblemonTrainerBattle.LOGGER.error(
                            String.format("An error occurred while loading %s", identifier.toString()));
                }
            }));
            CobblemonTrainerBattle.LOGGER.info(
                    String.format("Loaded %s", namespace));
        }

        return trainers;
    }

    private List<SmogonPokemon> loadTrainerPokemons(Resource resource) throws LoadingResourceFailedException {
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, new TypeToken<List<SmogonPokemon>>(){}.getType());

        } catch (IOException | JsonSyntaxException e) {
            throw new LoadingResourceFailedException();
        }
    }

    private TrainerConfiguration loadTrainerConfiguration(ResourceManager resourceManager, Identifier identifier) {
        String configPath = String.format("%s/%s", CobblemonTrainerBattle.TRAINER_CONFIG_DIR, identifier.getPath());
        Identifier configIdentifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, configPath);

        try (InputStream inputStream = resourceManager.getResourceOrThrow(configIdentifier).getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, TrainerConfiguration.class);

        } catch (IOException e) {
            return CobblemonTrainerBattle.defaultTrainerConfiguration;
        }
    }

    private boolean isJsonFile(Identifier identifier) {
        return identifier.toString().endsWith(".json");
    }

    private BattleFactoryConfiguration loadBattleFactoryConfiguration(ResourceManager resourceManager) {
        String path = String.format("%s/battlefactory.json", CobblemonTrainerBattle.ARCADE_CONFIG_DIR);
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, path);

        try (InputStream inputStream = resourceManager.getResourceOrThrow(identifier).getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, BattleFactoryConfiguration.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
