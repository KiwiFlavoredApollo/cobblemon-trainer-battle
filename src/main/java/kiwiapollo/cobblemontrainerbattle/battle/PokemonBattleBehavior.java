package kiwiapollo.cobblemontrainerbattle.battle;

import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;

import java.util.UUID;

public interface PokemonBattleBehavior {
    void start() throws BattleStartException;

    UUID getBattleId();
}
