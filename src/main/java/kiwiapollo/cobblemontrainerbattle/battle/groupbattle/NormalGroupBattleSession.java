package kiwiapollo.cobblemontrainerbattle.battle.groupbattle;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.MaximumPartyLevelPredicate;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.MessagePredicate;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.MinimumPartyLevelPredicate;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.RematchAllowedPredicate;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerGroupProfileStorage;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.session.SessionNormalTrainerBattle;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;

public class NormalGroupBattleSession extends GroupBattleSession {
    private final List<MessagePredicate<PlayerBattleParticipant>> predicates;

    public NormalGroupBattleSession(ServerPlayerEntity player, Identifier group) {
        super(player, group, SessionNormalTrainerBattle::new);

        TrainerGroupProfile profile = TrainerGroupProfileStorage.getProfileRegistry().get(group);
        this.predicates = List.of(
                new RematchAllowedPredicate(group, profile.isRematchAllowed),
                new MaximumPartyLevelPredicate(profile.maximumPartyLevel),
                new MinimumPartyLevelPredicate(profile.minimumPartyLevel)
        );
    }

    @Override
    public List<MessagePredicate<PlayerBattleParticipant>> getBattlePredicates() {
        return predicates;
    }
}
