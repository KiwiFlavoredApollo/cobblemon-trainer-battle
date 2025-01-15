package kiwiapollo.cobblemontrainerbattle.parser.preset;

import com.cobblemon.mod.common.battles.BattleFormat;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.BattleFormatFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.MinimumPartySizePredicate;
import kiwiapollo.cobblemontrainerbattle.common.ReadOnlyMap;
import kiwiapollo.cobblemontrainerbattle.parser.pokemon.ShowdownPokemon;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.*;
import java.util.*;

public class TrainerStorage implements SimpleSynchronousResourceReloadListener, ReadOnlyMap<String, TrainerBattleParticipant> {
    private static final String TEAM_DIR = "trainer_team";
    private static final String PRESET_DIR = "trainer_preset";
    private static final Gson GSON = new Gson();

    private static TrainerStorage instance;

    private final Map<String, TrainerBattleParticipant> storage;

    private TrainerStorage() {
        this.storage = new HashMap<>();
    }

    public static TrainerStorage getInstance() {
        if (instance == null) {
            instance = new TrainerStorage();
        }

        return instance;
    }

    @Override
    public TrainerBattleParticipant get(String trainer) {
        return storage.get(trainer);
    }

    @Override
    public Set<String> keySet() {
        return storage.keySet();
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(CobblemonTrainerBattle.MOD_ID, "trainer_storage");
    }

    @Override
    public void reload(ResourceManager manager) {
        Map<String, List<ShowdownPokemon>> teams = reloadTrainerTeam(manager);
        Map<Identifier, TrainerPreset> presets = reloadTrainerPreset(manager);

        storage.clear();

        for (Map.Entry<Identifier, TrainerPreset> trainer: presets.entrySet()) {
            List<ShowdownPokemon> team = teams.getOrDefault(trainer.getValue().team, List.of());

            if (!isValidTrainer(trainer, team)) {
                CobblemonTrainerBattle.LOGGER.error("Invalid trainer preset : {}", toSimplePath(trainer));
                continue;
            }

            storage.put(toTrainerId(trainer), new TrainerFactory(toTrainerId(trainer), toTrainerPreset(trainer), team).create());
        }
    }

    private Map<String, List<ShowdownPokemon>> reloadTrainerTeam(ResourceManager manager) {
        Map<String, List<ShowdownPokemon>> teams = new HashMap<>();

        manager.findResources(TEAM_DIR, identifier -> identifier.toString().endsWith(".json"))
                .forEach((identifier, resource) -> {
                    try (BufferedReader reader = resource.getReader()) {
                        List<ShowdownPokemon> team = GSON.fromJson(reader, new TypeToken<List<ShowdownPokemon>>(){}.getType());
                        teams.put(toTeamId(identifier), team);

                    } catch (IOException | JsonParseException e) {
                        CobblemonTrainerBattle.LOGGER.error("Error occurred while loading {}", identifier);
                    }
                });

        return teams;
    }

    private Map<Identifier, TrainerPreset> reloadTrainerPreset(ResourceManager manager) {
        Map<Identifier, TrainerPreset> presets = new HashMap<>();

        manager.findResources(PRESET_DIR, identifier -> identifier.toString().endsWith(".json"))
                .forEach((identifier, resource) -> {
                    try (BufferedReader reader = resource.getReader()) {
                        TrainerPreset preset = GSON.fromJson(reader, TrainerPreset.class);
                        presets.put(identifier, preset);

                    } catch (IOException | JsonParseException e) {
                        CobblemonTrainerBattle.LOGGER.error("Error occurred while loading {}", identifier);
                    }
                });

        return presets;
    }

    private boolean isValidTrainer(Map.Entry<Identifier, TrainerPreset> trainer, List<ShowdownPokemon> team) {
        if (team.isEmpty()) {
            CobblemonTrainerBattle.LOGGER.error("Unknown team : {}", toTrainerId(trainer));
            return false;
        }

        if (!isPartySizeCompatible(new BattleFormatFactory(toTrainerPreset(trainer).battleFormat).create(), team)) {
            CobblemonTrainerBattle.LOGGER.error("Illegal party size");
            return false;
        }

        return true;
    }

    private boolean isPartySizeCompatible(BattleFormat format, List<ShowdownPokemon> team) {
        return new MinimumPartySizePredicate.BattleFormatPredicate(format).test(team.size());
    }

    private String toSimplePath(Map.Entry<Identifier, TrainerPreset> trainer) {
        return trainer.getKey().getPath().replace(PRESET_DIR + "/", "");
    }

    private String toTrainerId(Map.Entry<Identifier, TrainerPreset> trainer) {
        String trainerId = trainer.getKey().getPath();
        trainerId = trainerId.replace(PRESET_DIR + "/", "");
        trainerId = trainerId.replace(".json", "");

        return trainerId;
    }

    private TrainerPreset toTrainerPreset(Map.Entry<Identifier, TrainerPreset> preset) {
        return preset.getValue();
    }

    private String toTeamId(Identifier team) {
        String teamId = team.toString();
        teamId = teamId.replace(TEAM_DIR + "/", "");
        teamId = teamId.replace(".json", "");

        if (!teamId.contains(":")) {
            teamId = CobblemonTrainerBattle.MOD_ID + ":" + teamId;
        }

        return teamId;
    }
}
