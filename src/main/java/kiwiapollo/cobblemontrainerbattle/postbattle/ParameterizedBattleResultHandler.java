package kiwiapollo.cobblemontrainerbattle.postbattle;

public class ParameterizedBattleResultHandler implements BattleResultHandler {
    private final Runnable onVictory;
    private final Runnable onDefeat;

    public ParameterizedBattleResultHandler(Runnable onVictory, Runnable onDefeat) {
        this.onVictory = onVictory;
        this.onDefeat = onDefeat;
    }

    @Override
    public void onVictory() {
        onVictory.run();
    }

    @Override
    public void onDefeat() {
        onDefeat.run();
    }
}
