package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.factory;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.BattleFactoryPlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.BattleFactoryTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.session.Session;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerFactory;
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

    private static class InfiniteBattleFactorySessionTrainerFactory extends BattleFactorySessionTrainerFactory {
        private final ServerPlayerEntity player;

        public InfiniteBattleFactorySessionTrainerFactory(ServerPlayerEntity player) {
            this.player = player;
        }

        @Override
        public BattleFactoryTrainer create(Session session) {
            return new BattleFactoryTrainer(getNextTrainer(), player, LEVEL);
        }

        private Identifier getNextTrainer() {
            return new RandomTrainerFactory(super::hasMinimumPokemon).create();
        }
    }
}
