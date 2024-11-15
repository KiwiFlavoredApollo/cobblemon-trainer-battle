package kiwiapollo.cobblemontrainerbattle.parser.profile;

import com.google.gson.Gson;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.groupbattle.TrainerGroupProfile;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TrainerGroupProfileLoader implements SimpleSynchronousResourceReloadListener {
    private static final String GROUP_DIR = "groups";

    @Override
    public Identifier getFabricId() {
        return Identifier.of(CobblemonTrainerBattle.MOD_ID, "trainer_group_profile_loader");
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        TrainerGroupProfileStorage.getProfileRegistry().clear();
        resourceManager.findResources(GROUP_DIR, this::isJsonFile).forEach(((identifier, resource) -> {
            try (InputStream inputStream = resourceManager.getResourceOrThrow(identifier).getInputStream()) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                TrainerGroupProfile trainerGroupProfile = new Gson().fromJson(bufferedReader, TrainerGroupProfile.class);

                assertTrainerGroupProfileValid(trainerGroupProfile);

                TrainerGroupProfileStorage.getProfileRegistry().put(toTrainerGroupIdentifier(identifier), trainerGroupProfile);

            } catch (IOException | NullPointerException | IllegalArgumentException ignored) {
                CobblemonTrainerBattle.LOGGER.error("An error occurred while loading {}", identifier.toString());
            }
        }));
    }

    private void assertTrainerGroupProfileValid(TrainerGroupProfile profile) {
        if (profile.trainers.isEmpty()) {
            throw new IllegalArgumentException();
        }

        boolean isExistAllTrainers = profile.trainers.stream()
                .map(Identifier::new)
                .allMatch(TrainerProfileStorage.getProfileRegistry()::containsKey);
        if (!isExistAllTrainers) {
            throw new IllegalArgumentException();
        }
    }

    private Identifier toTrainerGroupIdentifier(Identifier group) {
        String prefix = String.format("^%s/", GROUP_DIR);
        String suffix = "\\.json$";
        String path = group.getPath().replaceAll(prefix, "").replaceAll(suffix, "");
        return Identifier.of("group", path);
    }

    private boolean isJsonFile(Identifier file) {
        return file.toString().endsWith(".json");
    }
}
