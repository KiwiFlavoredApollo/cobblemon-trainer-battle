package kiwiapollo.cobblemontrainerbattle.battle.predicate;

import com.cobblemon.mod.common.Cobblemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class PlayerNotBusyPredicate implements MessagePredicate<ServerPlayerEntity> {
    private PlayerNotBusyPredicate() {

    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.player_not_busy");
    }

    @Override
    public boolean test(ServerPlayerEntity player) {
        return Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) == null;
    }

    public static class ServerPlayerEntityPredicate implements MessagePredicate<ServerPlayerEntity> {
        private final PlayerNotBusyPredicate predicate;

        public ServerPlayerEntityPredicate() {
            this.predicate = new PlayerNotBusyPredicate();
        }

        @Override
        public MutableText getErrorMessage() {
            return predicate.getErrorMessage();
        }

        @Override
        public boolean test(ServerPlayerEntity player) {
            return predicate.test(player);
        }
    }

    public static class PlayerBattleParticipantPredicate implements MessagePredicate<PlayerBattleParticipant> {
        private final PlayerNotBusyPredicate predicate;

        public PlayerBattleParticipantPredicate() {
            this.predicate = new PlayerNotBusyPredicate();
        }

        @Override
        public MutableText getErrorMessage() {
            return predicate.getErrorMessage();
        }

        @Override
        public boolean test(PlayerBattleParticipant player) {
            return predicate.test(player.getPlayerEntity());
        }
    }
}
