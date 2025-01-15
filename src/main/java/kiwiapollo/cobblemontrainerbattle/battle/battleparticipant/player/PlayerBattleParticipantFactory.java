package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerBattleParticipantFactory implements SimpleFactory<PlayerBattleParticipant> {
    private final ServerPlayerEntity player;
    private final LevelMode levelMode;

    public PlayerBattleParticipantFactory(ServerPlayerEntity player, LevelMode levelMode) {
        this.player = player;
        this.levelMode = levelMode;
    }

    @Override
    public PlayerBattleParticipant create() {
        return switch(levelMode) {
            case NORMAL -> new NormalLevelPlayer(player);
            case RELATIVE -> new NormalLevelPlayer(player);
            case FLAT -> new FlatLevelPlayer(player);
        };
    }
}
