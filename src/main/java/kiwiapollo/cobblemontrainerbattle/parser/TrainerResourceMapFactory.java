package kiwiapollo.cobblemontrainerbattle.parser;

import com.google.gson.JsonParseException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TrainerResourceMapFactory {
    private static final String TRAINER_TEAM_DIR = "trainers/teams";
    private static final String TRAINER_OPTION_DIR = "trainers/options";
    private static final String DEFAULT_TRAINER_OPTION_PATH = String.format("%s/%s", TRAINER_OPTION_DIR, "defaults.json");

    private final Resource defaultOptionResource;
    private final Map<Identifier, Resource> teamResourceMap;
    private final Map<Identifier, Resource> optionResourceMap;

    public TrainerResourceMapFactory(ResourceManager resourceManager) {
        this.defaultOptionResource = getDefaultTrainerOptionResource(resourceManager);
        this.teamResourceMap = resourceManager.findResources(TRAINER_TEAM_DIR, this::isJsonFile);
        this.optionResourceMap = resourceManager.findResources(TRAINER_OPTION_DIR, this::isJsonFile);
    }

    public Map<Identifier, TrainerResource> create() {
        Map<Identifier, TrainerResource> trainerResourceMap = new HashMap<>();

        for (Map.Entry<Identifier, Resource> entry : teamResourceMap.entrySet()) {
            try {
                Identifier team = entry.getKey();

                Identifier trainer = toTrainerIdentifier(team);
                TrainerResource resource = new TrainerResource(
                        getTeamResource(team),
                        getOptionResource(toOptionIdentifier(team))
                );

                trainerResourceMap.put(trainer, resource);

            } catch (JsonParseException e) {
                Identifier team = entry.getKey();
                CobblemonTrainerBattle.LOGGER.error("An error occurred while loading {}", team.toString());
            }
        }

        return trainerResourceMap;
    }

    private Resource getTeamResource(Identifier team) {
        return teamResourceMap.get(team);
    }

    private Resource getOptionResource(Identifier option) {
        return optionResourceMap.getOrDefault(option, defaultOptionResource);
    }

    private Resource getDefaultTrainerOptionResource(ResourceManager resourceManager) {
        try {
            Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, DEFAULT_TRAINER_OPTION_PATH);
            return resourceManager.getResourceOrThrow(identifier);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Identifier toTrainerIdentifier(Identifier team) {
        String prefix = String.format("^%s/", TRAINER_TEAM_DIR);
        String suffix = "\\.json$";
        String path = team.getPath().replaceAll(prefix, "").replaceAll(suffix, "");

        return Identifier.of("trainer", path);
    }

    private Identifier toOptionIdentifier(Identifier team) {
        String namespace = team.getNamespace();
        String path = team.getPath().replace(TRAINER_TEAM_DIR, TRAINER_OPTION_DIR);

        return Identifier.of(namespace, path);
    }

    private boolean isJsonFile(Identifier file) {
        return file.toString().endsWith(".json");
    }
}
