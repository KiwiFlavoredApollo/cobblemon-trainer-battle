package kiwiapollo.cobblemontrainerbattle.parser.profile;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerOption;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerProfile;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class TrainerProfileLoader implements SimpleSynchronousResourceReloadListener {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(SoundEvent .class, new BattleThemeDeserializer())
            .registerTypeAdapter(ItemStack.class, new HeldItemDeserializer())
            .create();

    @Override
    public Identifier getFabricId() {
        return Identifier.of(CobblemonTrainerBattle.MOD_ID, "trainer_profile_loader");
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        TrainerProfileStorage.getProfileRegistry().clear();
        for (Map.Entry<Identifier, TrainerResource> entry : new TrainerResourceMapFactory(resourceManager).create().entrySet()) {
            try {
                Identifier identifier = entry.getKey();
                Resource teamResource = entry.getValue().team();
                Resource optionResource = entry.getValue().option();

                String name = Paths.get(identifier.getPath()).getFileName().toString();
                List<ShowdownPokemon> team = readTrainerTeamResource(teamResource);
                TrainerOption option = readTrainerOptionResource(optionResource);

                TrainerProfileStorage.getProfileRegistry().put(
                        identifier,
                        new TrainerProfile(
                                name,
                                team,
                                option.isSpawningAllowed,
                                option.isRematchAllowed,
                                option.maximumPartyLevel,
                                option.minimumPartyLevel,
                                option.requiredLabel,
                                option.requiredPokemon,
                                option.requiredHeldItem,
                                option.requiredAbility,
                                option.requiredMove,
                                option.forbiddenLabel,
                                option.forbiddenPokemon,
                                option.forbiddenHeldItem,
                                option.forbiddenAbility,
                                option.forbiddenMove,
                                option.battleTheme,
                                option.onVictory,
                                option.onDefeat
                        )
                );

            } catch (JsonParseException | IOException e) {
                Identifier teamIdentifier = entry.getKey();
                CobblemonTrainerBattle.LOGGER.error("Error occurred while loading {}", teamIdentifier.toString());
            }
        }
    }

    private List<ShowdownPokemon> readTrainerTeamResource(Resource resource) throws IOException, JsonParseException {
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return GSON.fromJson(bufferedReader, new TypeToken<List<ShowdownPokemon>>(){}.getType());
        }
    }

    private TrainerOption readTrainerOptionResource(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return GSON.fromJson(bufferedReader, TrainerOption.class);
        }
    }
}
