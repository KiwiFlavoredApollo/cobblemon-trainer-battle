package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.factory;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.NormalBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.NormalBattleTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.groupbattle.TrainerGroupProfile;
import kiwiapollo.cobblemontrainerbattle.battle.session.Session;
import kiwiapollo.cobblemontrainerbattle.exception.AllTrainerDefeatedException;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerGroupProfileStorage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;

public class NormalGroupBattleParticipantFactory implements SessionBattleParticipantFactory {
    private final NormalGroupBattleSessionTrainerFactory sessionTrainerFactory;
    private final ServerPlayerEntity player;

    public NormalGroupBattleParticipantFactory(Identifier group, ServerPlayerEntity player) {
        this.sessionTrainerFactory = new NormalGroupBattleSessionTrainerFactory(group, player);
        this.player = player;
    }

    @Override
    public PlayerBattleParticipant createPlayer(Session session) {
        return new NormalBattlePlayer(player);
    }

    @Override
    public TrainerBattleParticipant createTrainer(Session session) {
        return sessionTrainerFactory.create(session);
    }

    private static class NormalGroupBattleSessionTrainerFactory implements SessionTrainerFactory {
        private final List<Identifier> trainers;
        private final ServerPlayerEntity player;

        public NormalGroupBattleSessionTrainerFactory(Identifier group, ServerPlayerEntity player) {
            TrainerGroupProfile profile = TrainerGroupProfileStorage.getProfileRegistry().get(group);
            this.trainers = profile.trainers.stream().map(Identifier::tryParse).toList();
            this.player = player;
        }

        @Override
        public NormalBattleTrainer create(Session session) {
            try {
                return new NormalBattleTrainer(getNextTrainer(session.getStreak()), player);
            } catch (IndexOutOfBoundsException e) {
                throw new AllTrainerDefeatedException(e);
            }
        }

        private Identifier getNextTrainer(int index) {
            return trainers.get(index);
        }
    }
}
