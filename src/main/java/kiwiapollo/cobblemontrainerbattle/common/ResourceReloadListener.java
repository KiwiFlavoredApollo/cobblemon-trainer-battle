package kiwiapollo.cobblemontrainerbattle.common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.LoadingResourceFailedException;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupFile;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerFile;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        CobblemonTrainerBattle.trainerFiles = loadTrainerFiles(resourceManager);
        CobblemonTrainerBattle.groupFiles = loadGroupFiles(resourceManager);
        CobblemonTrainerBattle.battleFactoryConfiguration = loadBattleFactoryConfiguration(resourceManager);
    }

    private JsonObject loadDefaultTrainerConfiguration(ResourceManager resourceManager) {
        String path = String.format("%s/defaults.json", CobblemonTrainerBattle.TRAINER_CONFIG_DIR);
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, path);

        try (InputStream inputStream = resourceManager
                .getResourceOrThrow(identifier)
                .getInputStream()) {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, JsonObject.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, TrainerFile> loadTrainerFiles(ResourceManager resourceManager) {
        List<String> namespaces = List.of(
                "radicalred",
                "inclementemerald",
                "custom"
        );

        Map<String, TrainerFile> trainerFiles = new HashMap<>();
        for (String namespace : namespaces) {
            resourceManager.findResources(namespace, this::isJsonFile)
                    .forEach((identifier, resource) -> {
                        try {
                            // identifier: cobblemontrainerbattle:custom/custom_trainer.json
                            JsonArray pokemons = loadTrainerPokemons(resource);
                            JsonObject configuration = loadTrainerConfiguration(resourceManager, identifier);
                            trainerFiles.put(identifier.getPath(), new TrainerFile(pokemons, configuration));

                        } catch (LoadingResourceFailedException e) {
                            CobblemonTrainerBattle.LOGGER.error(
                                    String.format("Error occurred while loading trainer file %s",
                                            identifier.toString()));
                        }
                    });
            CobblemonTrainerBattle.LOGGER.info(String.format("Loaded %s trainer files", namespace));
        }
        return trainerFiles;
    }

    private Map<String, GroupFile> loadGroupFiles(ResourceManager resourceManager) {
        Map<String, GroupFile> groupFiles = new HashMap<>();
        String startingPath = CobblemonTrainerBattle.GROUP_CONFIG_DIR;
        resourceManager.findResources(startingPath, this::isJsonFile)
                .forEach((identifier, resource) -> {
                    try {
                        // identifier: cobblemontrainerbattle:battlegroup/custom_trainer_group.json
                        JsonObject configuration = loadGroupConfiguration(resourceManager, identifier);
                        String path = identifier.getPath().replace(String.format("%s/", startingPath), "");
                        groupFiles.put(path, new GroupFile(configuration));

                    } catch (LoadingResourceFailedException e) {
                        CobblemonTrainerBattle.LOGGER.error(
                                String.format("Error occurred while loading group file %s",
                                        identifier.toString()));
                    }
                });
        CobblemonTrainerBattle.LOGGER.info("Loaded group files");
        return groupFiles;
    }


    private JsonArray loadTrainerPokemons(Resource resource) throws LoadingResourceFailedException {
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, JsonArray.class);

        } catch (IOException | JsonSyntaxException e) {
            throw new LoadingResourceFailedException();
        }
    }

    private JsonObject loadTrainerConfiguration(ResourceManager resourceManager, Identifier identifier) {
        String configPath = String.format("%s/%s", CobblemonTrainerBattle.TRAINER_CONFIG_DIR, identifier.getPath());
        Identifier configIdentifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, configPath);

        try (InputStream inputStream = resourceManager
                .getResourceOrThrow(configIdentifier)
                .getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, JsonObject.class);

        } catch (IOException e) {
            return CobblemonTrainerBattle.defaultTrainerConfiguration;
        }
    }

    private JsonObject loadGroupConfiguration(ResourceManager resourceManager, Identifier identifier)
            throws LoadingResourceFailedException {
        try (InputStream inputStream = resourceManager
                .getResourceOrThrow(identifier)
                .getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, JsonObject.class);

        } catch (IOException e) {
            throw new LoadingResourceFailedException();
        }
    }

    private boolean isJsonFile(Identifier identifier) {
        return identifier.toString().endsWith(".json");
    }

    private JsonObject loadBattleFactoryConfiguration(ResourceManager resourceManager) {
        String path = String.format("%s/battlefactory.json", CobblemonTrainerBattle.ARCADE_CONFIG_DIR);
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, path);

        try (InputStream inputStream = resourceManager
                .getResourceOrThrow(identifier)
                .getInputStream()) {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return new Gson().fromJson(bufferedReader, JsonObject.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
