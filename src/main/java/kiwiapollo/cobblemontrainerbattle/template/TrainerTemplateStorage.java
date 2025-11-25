package kiwiapollo.cobblemontrainerbattle.template;

import com.cobblemon.mod.common.Cobblemon;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.ImmutableMap;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.*;
import java.util.*;

public class TrainerTemplateStorage implements SimpleSynchronousResourceReloadListener, ImmutableMap<Identifier, TrainerTemplate> {
    private static final String TEAM_DIR = "trainer_team";
    private static final String PRESET_DIR = "trainer_preset";

    private static TrainerTemplateStorage instance;

    private final Map<Identifier, TrainerTemplate> storage;

    private TrainerTemplateStorage() {
        this.storage = new HashMap<>();
    }

    public static TrainerTemplateStorage getInstance() {
        if (instance == null) {
            instance = new TrainerTemplateStorage();
        }

        return instance;
    }

    @Override
    public TrainerTemplate get(Identifier trainer) {
        return storage.get(trainer);
    }

    @Override
    public Set<Identifier> keySet() {
        return storage.keySet();
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(CobblemonTrainerBattle.MOD_ID, "trainer_storage");
    }

    /**
     * Added dependency to Cobblemon resource reloader
     * @see com.cobblemon.mod.common.data.CobblemonDataProvider
     */
    @Override
    public Collection<Identifier> getFabricDependencies() {
        return List.of(
                Identifier.of(Cobblemon.MODID, "data_resources")
        );
    }

    @Override
    public void reload(ResourceManager manager) {
        Map<Identifier, TrainerTeam> teams = reloadTrainerTeam(manager);
        Map<Identifier, TrainerPreset> presets = reloadTrainerPreset(manager);

        storage.clear();

        for (Map.Entry<Identifier, TrainerPreset> entry : presets.entrySet()) {
            try {
                Identifier identifier = toTrainerTemplateIdentifier(entry.getKey());
                TrainerPreset preset = entry.getValue();
                TrainerTeam team = teams.get(toDefaultedIdentifier(preset.team));
                TrainerTemplate template = new TrainerTemplateFactory(identifier, preset, team).create();

                storage.put(identifier, template);

            } catch (NullPointerException | IllegalArgumentException e) {
                CobblemonTrainerBattle.LOGGER.error("Error parsing trainer preset: {}", entry.getKey());
            }
        }
    }

    private Map<Identifier, TrainerTeam> reloadTrainerTeam(ResourceManager manager) {
        Map<Identifier, TrainerTeam> teams = new HashMap<>();

        Map<Identifier, Resource> resources = manager.findResources(TEAM_DIR, identifier -> identifier.toString().endsWith(".json"));
        for (Map.Entry<Identifier, Resource> entry: resources.entrySet()) {
            try (BufferedReader reader = entry.getValue().getReader()) {
                Identifier identifier = toTrainerTeamIdentifier(entry.getKey());
                TrainerTeam team = new Gson().fromJson(reader, TrainerTeam.class);

                teams.put(identifier, team);

            } catch (IOException | JsonParseException e) {
                CobblemonTrainerBattle.LOGGER.error("Error loading file: {}", entry.getKey());
            }
        }

        return teams;
    }

    private Map<Identifier, TrainerPreset> reloadTrainerPreset(ResourceManager manager) {
        Map<Identifier, TrainerPreset> presets = new HashMap<>();

        Map<Identifier, Resource> resources = manager.findResources(PRESET_DIR, identifier -> identifier.toString().endsWith(".json"));
        for (Map.Entry<Identifier, Resource> entry: resources.entrySet()) {
            try (BufferedReader reader = entry.getValue().getReader()) {
                Identifier identifier = toTrainerPresetIdentifier(entry.getKey());
                TrainerPreset preset = new Gson().fromJson(reader, TrainerPreset.class);

                presets.put(identifier, preset);

            } catch (IOException | JsonParseException e) {
                CobblemonTrainerBattle.LOGGER.error("Error loading file: {}", entry.getKey());
            }
        }

        return presets;
    }

    private Identifier toTrainerTemplateIdentifier(Identifier preset) {
        return preset;
    }

    private Identifier toTrainerTeamIdentifier(Identifier resource) {
        String namespace = resource.getNamespace();
        String path = resource.getPath();

        path = path.replace(TEAM_DIR + "/", "");
        path = path.replace(".json", "");

        return Identifier.of(namespace, path);
    }

    private Identifier toTrainerPresetIdentifier(Identifier resource) {
        String namespace = resource.getNamespace();
        String path = resource.getPath();

        path = path.replace(PRESET_DIR + "/", "");
        path = path.replace(".json", "");

        return Identifier.of(namespace, path);
    }

    private Identifier toDefaultedIdentifier(String string) {
        if (string.contains(String.valueOf(Identifier.NAMESPACE_SEPARATOR))) {
            return Identifier.tryParse(string);

        } else {
            return Identifier.of(CobblemonTrainerBattle.MOD_ID, string);
        }
    }
}
