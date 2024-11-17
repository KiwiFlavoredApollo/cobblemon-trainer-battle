package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.factory;

import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerProfileStorage;
import net.minecraft.util.Identifier;

public abstract class BattleFactorySessionTrainerFactory implements SessionTrainerFactory {
    private static final int POKEMON_COUNT = 3;

    protected boolean hasMinimumPokemon(Identifier trainer) {
        return TrainerProfileStorage.getProfileRegistry().get(trainer).team().size() > POKEMON_COUNT;
    }
}
