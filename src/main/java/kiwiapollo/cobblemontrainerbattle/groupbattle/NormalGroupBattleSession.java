package kiwiapollo.cobblemontrainerbattle.groupbattle;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerGroupProfileStorage;
import kiwiapollo.cobblemontrainerbattle.predicates.*;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.session.SessionNormalTrainerBattle;
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
