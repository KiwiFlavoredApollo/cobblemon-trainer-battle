package kiwiapollo.cobblemontrainerbattle.common;

public class BattleCondition {
    public boolean isRematchAllowedAfterVictory;
    public int minimumPartyLevel;
    public int maximumPartyLevel;

    public BattleCondition() {
        this.isRematchAllowedAfterVictory = true;
        this.minimumPartyLevel = 0;
        this.maximumPartyLevel = 100;
    }

    public BattleCondition(
            boolean isRematchAllowedAfterVictory,
            int minimumPartyLevel,
            int maximumPartyLevel
    ) {
        this.isRematchAllowedAfterVictory = isRematchAllowedAfterVictory;
        this.minimumPartyLevel = minimumPartyLevel;
        this.maximumPartyLevel = maximumPartyLevel;
    }
}
