package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.factory;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.BattleFactoryPlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.BattleFactoryTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.session.Session;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.exception.AllTrainerDefeatedException;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class NormalBattleFactoryParticipantFactory implements SessionBattleParticipantFactory {
    private static final int LEVEL = 100;

    private final ServerPlayerEntity player;
    private final SessionTrainerFactory sessionTrainerFactory;

    public NormalBattleFactoryParticipantFactory(ServerPlayerEntity player) {
        this.player = player;
        this.sessionTrainerFactory = new NormalBattleFactorySessionTrainerFactory(player);
    }

    @Override
    public PlayerBattleParticipant createPlayer(Session session) {
        return new BattleFactoryPlayer(player, LEVEL);
    }

    @Override
    public TrainerBattleParticipant createTrainer(Session session) {
        return sessionTrainerFactory.create(session);
    }

    private static class NormalBattleFactorySessionTrainerFactory extends BattleFactorySessionTrainerFactory {
        private static final int ROUND_COUNT = 7;

        private final ServerPlayerEntity player;
        private final List<Identifier> trainers;

        public NormalBattleFactorySessionTrainerFactory(ServerPlayerEntity player) {
            this.player = player;
            this.trainers = createRandomTrainers();
        }

        private List<Identifier> createRandomTrainers() {
            List<Identifier> trainers = new ArrayList<>();
            for (int i = 0; i < ROUND_COUNT; i++) {
                trainers.add(new RandomTrainerFactory(super::hasMinimumPokemon).create());
            }
            return trainers;
        }

        @Override
        public BattleFactoryTrainer create(Session session) {
            try {
                return new BattleFactoryTrainer(getNextTrainer(session.getDefeatedTrainersCount()), player, LEVEL);
            } catch (IndexOutOfBoundsException e) {
                throw new AllTrainerDefeatedException(e);
            }
        }

        private Identifier getNextTrainer(int round) {
            return trainers.get(round);
        }
    }
}
