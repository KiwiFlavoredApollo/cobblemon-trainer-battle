package kiwiapollo.cobblemontrainerbattle.groupbattle;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerGroupProfileStorage;
import kiwiapollo.cobblemontrainerbattle.predicates.*;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.SessionFlatTrainerBattle;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;

public class FlatGroupBattleSession extends GroupBattleSession {
    private static final int LEVEL = 100;

    private final List<MessagePredicate<PlayerBattleParticipant>> predicates;

    public FlatGroupBattleSession(ServerPlayerEntity player, Identifier group) {
        super(player, group, SessionFlatTrainerBattle::new);

        TrainerGroupProfile profile = TrainerGroupProfileStorage.getProfileRegistry().get(group);
        this.predicates = List.of(
                new RematchAllowedPredicate(group, profile.isRematchAllowed)
        );
    }

    @Override
    public List<MessagePredicate<PlayerBattleParticipant>> getBattlePredicates() {
        return predicates;
    }
}
