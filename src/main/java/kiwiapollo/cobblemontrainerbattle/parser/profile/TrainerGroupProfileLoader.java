package kiwiapollo.cobblemontrainerbattle.parser.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.groupbattle.TrainerGroupProfile;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;

public class TrainerGroupProfileLoader implements SimpleSynchronousResourceReloadListener {
    private static final String GROUP_DIR = "groups";
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(SoundEvent.class, new BattleThemeDeserializer())
            .registerTypeAdapter(ItemStack.class, new HeldItemDeserializer())
            .create();

    @Override
    public Identifier getFabricId() {
        return Identifier.of(CobblemonTrainerBattle.MOD_ID, "trainer_group_profile_loader");
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        TrainerGroupProfileStorage.getProfileRegistry().clear();
        resourceManager.findResources(GROUP_DIR, this::isJsonFile).forEach(((identifier, resource) -> {
            try (BufferedReader reader = resourceManager.getResourceOrThrow(identifier).getReader()) {
                TrainerGroupProfile trainerGroupProfile = GSON.fromJson(reader, TrainerGroupProfile.class);

                assertTrainerGroupProfileValid(trainerGroupProfile);

                TrainerGroupProfileStorage.getProfileRegistry().put(toTrainerGroupIdentifier(identifier), trainerGroupProfile);

            } catch (JsonParseException | IOException | NullPointerException | IllegalArgumentException ignored) {
                CobblemonTrainerBattle.LOGGER.error("Error occurred while loading {}", identifier.toString());
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
