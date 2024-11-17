package kiwiapollo.cobblemontrainerbattle.battle.battlefactory;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.factory.NormalBattleFactoryParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.postbattle.DefeatActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.battle.postbattle.VictoryActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.parser.history.BattleRecord;
import kiwiapollo.cobblemontrainerbattle.parser.history.PlayerHistoryManager;
import kiwiapollo.cobblemontrainerbattle.parser.profile.MiniGameProfileStorage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class NormalBattleFactorySession extends BattleFactorySession {
    private final VictoryActionSetHandler sessionVictoryHandler;
    private final DefeatActionSetHandler sessionDefeatHandler;

    public NormalBattleFactorySession(ServerPlayerEntity player) {
        super(new NormalBattleFactoryParticipantFactory(player));

        BattleFactoryProfile profile = MiniGameProfileStorage.getBattleFactoryProfile();
        this.sessionVictoryHandler = new VictoryActionSetHandler(player, profile.onVictory);
        this.sessionDefeatHandler = new DefeatActionSetHandler(player, profile.onDefeat);
    }

    @Override
    public void onSessionStop() {
        if (isAllTrainerDefeated()) {
            sessionVictoryHandler.run();
            updateVictoryRecord();

        } else {
            sessionDefeatHandler.run();
            updateDefeatRecord();
        }
    }

    private BattleRecord getBattleRecord() {
        return (BattleRecord) PlayerHistoryManager.get(player.getUuid()).get(Identifier.tryParse("minigame:battlefactory"));
    }

    private void updateVictoryRecord() {
        getBattleRecord().setVictoryCount(getBattleRecord().getVictoryCount() + 1);
    }

    private void updateDefeatRecord() {
        getBattleRecord().setDefeatCount(getBattleRecord().getDefeatCount() + 1);
    }
}
