package kiwiapollo.cobblemontrainerbattle.postbattle;

import java.util.Arrays;
import java.util.List;

public class BatchedBattleResultHandler implements BattleResultHandler {
    private final List<BattleResultHandler> handlers;

    public BatchedBattleResultHandler(BattleResultHandler... handlers) {
        this.handlers = Arrays.stream(handlers).toList();
    }

    @Override
    public void onVictory() {
        handlers.forEach(BattleResultHandler::onVictory);
    }

    @Override
    public void onDefeat() {
        handlers.forEach(BattleResultHandler::onDefeat);
    }
}
