package kiwiapollo.cobblemontrainerbattle.groupbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.TrainerGroup;
import kiwiapollo.cobblemontrainerbattle.exceptions.*;
import net.minecraft.server.network.ServerPlayerEntity;

import java.time.Duration;
import java.time.Instant;

public class GroupBattleSessionValidator {
    private final ServerPlayerEntity player;

    public GroupBattleSessionValidator(ServerPlayerEntity player) {
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
        if (!GroupBattle.sessions.containsKey(player.getUuid())) {
            return false;
        }

        GroupBattleSession session = GroupBattle.sessions.get(player.getUuid());
        return Instant.now().isBefore(session.timestamp.plus(Duration.ofHours(24)));
    }

    public void assertNotPlayerDefeated() throws DefeatedToTrainerException {
        if (GroupBattle.sessions.get(player.getUuid()).isDefeated) {
            throw new DefeatedToTrainerException();
        }
    }

    public void assertNotDefeatedAllTrainers() throws DefeatedAllTrainersException {
        if (isDefeatedAllTrainers(player)) {
            throw new DefeatedAllTrainersException();
        }
    }

    public static boolean isDefeatedAllTrainers(ServerPlayerEntity player) {
        try {
            GroupBattleSession session = GroupBattle.sessions.get(player.getUuid());
            TrainerGroup trainerGroup = CobblemonTrainerBattle.trainerGroups.get(session.trainerGroupIdentifier);

            return session.defeatedTrainerCount == trainerGroup.trainers.size();

        } catch (NullPointerException | ClassCastException | IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }
}
