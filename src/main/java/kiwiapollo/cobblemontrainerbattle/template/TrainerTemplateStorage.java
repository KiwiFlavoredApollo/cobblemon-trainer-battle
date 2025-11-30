package kiwiapollo.cobblemontrainerbattle.template;

import com.cobblemon.mod.common.Cobblemon;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.*;
import java.util.*;

public class TrainerTemplateStorage implements SimpleSynchronousResourceReloadListener {
    private static final String TEAM_DIR = "trainer_team";
    private static final String PRESET_DIR = "trainer_preset";

    private static TrainerTemplateStorage instance;

    private final Map<Identifier, TrainerTemplate> template;
    private final Map<Identifier, TrainerTeam> team;
    private final Map<Identifier, TrainerPreset> preset;

    private TrainerTemplateStorage() {
        this.template = new HashMap<>();
        this.team = new HashMap<>();
        this.preset = new HashMap<>();
    }

    public static TrainerTemplateStorage getInstance() {
        if (instance == null) {
            instance = new TrainerTemplateStorage();
        }

        return instance;
    }

    public TrainerTemplate get(Identifier trainer) {
        return template.get(trainer);
    }

    public Set<Identifier> keySet() {
        return template.keySet();
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
        reloadTrainerTeam(manager);
        reloadTrainerPreset(manager);
        reloadTrainerTemplate(manager);
    }

    private void reloadTrainerTemplate(ResourceManager manager) {
        this.template.clear();

        for (Map.Entry<Identifier, TrainerPreset> entry : this.preset.entrySet()) {
            try {
                Identifier identifier = getTrainerTemplateIdentifier(entry);
                TrainerPreset preset = getTrainerPreset(entry);
                TrainerTeam team = getTrainerTeam(entry);
                TrainerTemplate template = new TrainerTemplateFactory(identifier, preset, team).create();

                this.template.put(identifier, template);

            } catch (NullPointerException | IllegalArgumentException e) {
                // TODO better name
                CobblemonTrainerBattle.LOGGER.error("Error parsing trainer preset: {}", entry.getKey());
            }
        }
    }

    private Identifier getTrainerTemplateIdentifier(Map.Entry<Identifier, TrainerPreset> entry) {
        return entry.getKey();
    }

    private TrainerPreset getTrainerPreset(Map.Entry<Identifier, TrainerPreset> entry) {
        return entry.getValue();
    }

    private TrainerTeam getTrainerTeam(Map.Entry<Identifier, TrainerPreset> entry) {
        try {
            TrainerPreset preset = entry.getValue();
            Identifier identifier = Objects.requireNonNull(toDefaultedIdentifier(preset.team));
            return this.team.get(identifier);

        } catch (NullPointerException e) {
            return new TrainerTeam();
        }
    }

    private Identifier toDefaultedIdentifier(String string) {
        if (string.contains(String.valueOf(Identifier.NAMESPACE_SEPARATOR))) {
            return Identifier.tryParse(string);

        } else {
            return Identifier.of(CobblemonTrainerBattle.MOD_ID, string);
        }
    }

    private void reloadTrainerTeam(ResourceManager manager) {
        this.team.clear();

        Map<Identifier, Resource> resources = manager.findResources(TEAM_DIR, identifier -> identifier.toString().endsWith(".json"));
        for (Map.Entry<Identifier, Resource> entry: resources.entrySet()) {
            try (BufferedReader reader = entry.getValue().getReader()) {
                String namespace = entry.getKey().getNamespace();
                String path = entry.getKey().getPath();

                path = path.replace(TEAM_DIR + "/", "");
                path = path.replace(".json", "");

                Identifier identifier = Identifier.of(namespace, path);
                TrainerTeam team = new Gson().fromJson(reader, TrainerTeam.class);

                this.team.put(identifier, team);

            } catch (IOException | JsonParseException e) {
                // TODO better name
                CobblemonTrainerBattle.LOGGER.error("Error loading file: {}", entry.getKey());
            }
        }
    }

    private void reloadTrainerPreset(ResourceManager manager) {
        this.preset.clear();

        Map<Identifier, Resource> resources = manager.findResources(PRESET_DIR, identifier -> identifier.toString().endsWith(".json"));
        for (Map.Entry<Identifier, Resource> entry: resources.entrySet()) {
            try (BufferedReader reader = entry.getValue().getReader()) {
                String namespace = entry.getKey().getNamespace();
                String path = entry.getKey().getPath();

                path = path.replace(PRESET_DIR + "/", "");
                path = path.replace(".json", "");

                Identifier identifier = Identifier.of(namespace, path);
                TrainerPreset preset = new Gson().fromJson(reader, TrainerPreset.class);

                this.preset.put(identifier, preset);

            } catch (IOException | JsonParseException e) {
                // TODO better name
                CobblemonTrainerBattle.LOGGER.error("Error loading file: {}", entry.getKey());
            }
        }
    }
}
