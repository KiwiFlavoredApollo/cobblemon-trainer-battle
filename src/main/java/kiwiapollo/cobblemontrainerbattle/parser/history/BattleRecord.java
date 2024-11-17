package kiwiapollo.cobblemontrainerbattle.parser.history;

public interface BattleRecord {
    int getVictoryCount();

    void setVictoryCount(int count);

    int getDefeatCount();

    void setDefeatCount(int count);
}
