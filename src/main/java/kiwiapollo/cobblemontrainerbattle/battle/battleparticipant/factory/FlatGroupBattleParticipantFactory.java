package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.factory;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.FlatBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.FlatBattleTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.groupbattle.TrainerGroupProfile;
import kiwiapollo.cobblemontrainerbattle.battle.session.Session;
import kiwiapollo.cobblemontrainerbattle.exception.AllTrainerDefeatedException;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerGroupProfileStorage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;

public class FlatGroupBattleParticipantFactory implements SessionBattleParticipantFactory {
    private static final int LEVEL = 100;

    private final FlatGroupBattleSessionTrainerFactory sessionTrainerFactory;
    private final ServerPlayerEntity player;

    public FlatGroupBattleParticipantFactory(Identifier group, ServerPlayerEntity player) {
        this.sessionTrainerFactory = new FlatGroupBattleSessionTrainerFactory(group, player);
        this.player = player;
    }

    @Override
    public PlayerBattleParticipant createPlayer(Session session) {
        return new FlatBattlePlayer(player, LEVEL);
    }

    @Override
    public TrainerBattleParticipant createTrainer(Session session) {
        return sessionTrainerFactory.create(session);
    }

    private static class FlatGroupBattleSessionTrainerFactory implements SessionTrainerFactory {
        private final List<Identifier> trainers;
        private final ServerPlayerEntity player;

        public FlatGroupBattleSessionTrainerFactory(Identifier group, ServerPlayerEntity player) {
            TrainerGroupProfile profile = TrainerGroupProfileStorage.getProfileRegistry().get(group);
            this.trainers = profile.trainers.stream().map(Identifier::tryParse).toList();
            this.player = player;
        }

        @Override
        public FlatBattleTrainer create(Session session) {
            try {
                return new FlatBattleTrainer(getNextTrainer(session.getStreak()), player, LEVEL);
            } catch (IndexOutOfBoundsException e) {
                throw new AllTrainerDefeatedException(e);
            }
        }

        private Identifier getNextTrainer(int index) {
            return trainers.get(index);
        }
    }
}
