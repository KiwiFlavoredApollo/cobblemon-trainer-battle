package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;

import java.util.UUID;

public class NullTrainerBattle implements PokemonBattleBehavior {
    @Override
    public void start() throws BattleStartException {

    }

    @Override
    public UUID getBattleId() {
        throw new NullPointerException();
    }
}
