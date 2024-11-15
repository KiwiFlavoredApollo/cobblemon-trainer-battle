package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.BattleParticipant;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

public interface PlayerBattleParticipant extends BattleParticipant {
    ServerPlayerEntity getPlayerEntity();

    void sendInfoMessage(MutableText message);

    void sendErrorMessage(MutableText message);
}
