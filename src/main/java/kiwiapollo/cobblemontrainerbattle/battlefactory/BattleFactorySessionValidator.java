package kiwiapollo.cobblemontrainerbattle.battlefactory;

import kiwiapollo.cobblemontrainerbattle.exceptions.*;
import net.minecraft.server.network.ServerPlayerEntity;

import java.time.Duration;
import java.time.Instant;

public class BattleFactorySessionValidator {
    private final ServerPlayerEntity player;

    public BattleFactorySessionValidator(ServerPlayerEntity player) {
        this.player = player;
    }

    public void assertExistValidSession() throws NotExistValidSessionException {
        if (!isExistValidSession()) {
            throw new NotExistValidSessionException();
        }
    }

    public void assertNotExistValidSession() throws ExistValidSessionException {
        if (isExistValidSession()) {
            throw new ExistValidSessionException();
        }
    }

    private boolean isExistValidSession() {
        if (!BattleFactory.sessions.containsKey(player.getUuid())) {
            return false;
        }

        BattleFactorySession session = BattleFactory.sessions.get(player.getUuid());
        return Instant.now().isBefore(session.timestamp.plus(Duration.ofHours(24)));
    }

    public void assertNotPlayerDefeated() throws DefeatedToTrainerException {
        if (BattleFactory.sessions.get(player.getUuid()).isDefeated) {
            throw new DefeatedToTrainerException();
        }
    }

    public void assertNotDefeatedAllTrainers() throws DefeatedAllTrainersException {
        if (isDefeatedAllTrainers(player)) {
            throw new DefeatedAllTrainersException();
        };
    }

    public static boolean isDefeatedAllTrainers(ServerPlayerEntity player) {
        BattleFactorySession session = BattleFactory.sessions.get(player.getUuid());
        return session.defeatedTrainerCount == session.trainersToDefeat.size();
    }

    public void assertExistDefeatedTrainer() throws NotExistDefeatedTrainerException {
        if (!isExistDefeatedTrainer(player)) {
            throw new NotExistDefeatedTrainerException();
        }
    }

    public void assertNotExistDefeatedTrainer() throws ExistDefeatedTrainerException {
        if (isExistDefeatedTrainer(player)) {
            throw new ExistDefeatedTrainerException();
        }
    }

    private static boolean isExistDefeatedTrainer(ServerPlayerEntity player) {
        return BattleFactory.sessions.get(player.getUuid()).defeatedTrainerCount != 0;
    }

    public void assertNotPlayerTradedPokemon() throws TradedPokemonException {
        if (BattleFactory.sessions.get(player.getUuid()).isTradedPokemon) {
            throw new TradedPokemonException();
        }
    }
}
