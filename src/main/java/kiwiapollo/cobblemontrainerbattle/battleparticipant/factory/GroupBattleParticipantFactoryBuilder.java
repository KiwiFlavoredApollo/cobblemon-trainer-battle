package kiwiapollo.cobblemontrainerbattle.battleparticipant.factory;

import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;

public interface GroupBattleParticipantFactoryBuilder {
    GroupBattleParticipantFactoryBuilder addCondition(BattleCondition condition);
    BattleParticipantFactory build();
}
