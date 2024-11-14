package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.postbattle.PostBattleActionSet;
import net.minecraft.sound.SoundEvent;

import java.util.List;

public record TrainerProfile(
        String name,
        List<ShowdownPokemon> team,
        boolean isSpawningAllowed,
        boolean isRematchAllowed,
        int maximumPartyLevel,
        int minimumPartyLevel,
        SoundEvent battleTheme,
        PostBattleActionSet onVictory,
        PostBattleActionSet onDefeat
) {}
