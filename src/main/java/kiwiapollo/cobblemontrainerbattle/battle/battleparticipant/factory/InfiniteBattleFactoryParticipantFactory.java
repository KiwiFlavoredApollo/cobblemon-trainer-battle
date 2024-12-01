package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.factory;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.BattleFactoryPlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.BattleFactoryTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.session.Session;
import kiwiapollo.cobblemontrainerbattle.common.BattleFactoryRandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class InfiniteBattleFactoryParticipantFactory implements SessionBattleParticipantFactory {
    private static final int LEVEL = 100;

    private final ServerPlayerEntity player;
    private final SessionTrainerFactory sessionTrainerFactory;

    public InfiniteBattleFactoryParticipantFactory(ServerPlayerEntity player) {
        this.player = player;
        this.sessionTrainerFactory = new InfiniteBattleFactorySessionTrainerFactory(player);
    }

    @Override
    public PlayerBattleParticipant createPlayer(Session session) {
        return new BattleFactoryPlayer(player, LEVEL);
    }

    @Override
    public TrainerBattleParticipant createTrainer(Session session) {
        return sessionTrainerFactory.create(session);
    }

    private static class InfiniteBattleFactorySessionTrainerFactory implements SessionTrainerFactory {
        private final ServerPlayerEntity player;
        private final SimpleFactory<Identifier> factory;

        public InfiniteBattleFactorySessionTrainerFactory(ServerPlayerEntity player) {
            this.player = player;
            this.factory = new BattleFactoryRandomTrainerFactory();
        }

        @Override
        public BattleFactoryTrainer create(Session session) {
            return new BattleFactoryTrainer(getNextTrainer(), player, LEVEL);
        }

        private Identifier getNextTrainer() {
            return factory.create();
        }
    }
}
