package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.battle.postbattle.PostBattleActionSet;
import net.minecraft.sound.SoundEvent;

import java.util.List;

public record TrainerProfile(
        String name,
        List<ShowdownPokemon> team,
        boolean isSpawningAllowed,
        boolean isRematchAllowed,
        int maximumPartyLevel,
        int minimumPartyLevel,
        PostBattleActionSet onVictory,
        PostBattleActionSet onDefeat
) {}
