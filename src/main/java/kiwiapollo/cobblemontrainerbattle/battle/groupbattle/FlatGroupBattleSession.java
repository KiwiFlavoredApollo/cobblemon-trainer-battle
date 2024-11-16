package kiwiapollo.cobblemontrainerbattle.battle.groupbattle;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.*;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerGroupProfileStorage;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.session.SessionFlatTrainerBattle;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;

public class FlatGroupBattleSession extends GroupBattleSession {
    private final List<MessagePredicate<PlayerBattleParticipant>> predicates;

    public FlatGroupBattleSession(ServerPlayerEntity player, Identifier group) {
        super(player, group, SessionFlatTrainerBattle::new);

        TrainerGroupProfile profile = TrainerGroupProfileStorage.getProfileRegistry().get(group);
        this.predicates = List.of(
                new RematchAllowedPredicate(group, profile.isRematchAllowed),
                new RequiredLabelExistPredicate(profile.requiredLabel),
                new RequiredPokemonExistPredicate(profile.requiredPokemon),
                new RequiredHeldItemExistPredicate(profile.requiredHeldItem),
                new RequiredAbilityExistPredicate(profile.requiredAbility),
                new RequiredMoveExistPredicate(profile.requiredMove),
                new ForbiddenLabelNotExistPredicate(profile.forbiddenLabel),
                new ForbiddenPokemonNotExistPredicate(profile.forbiddenPokemon),
                new ForbiddenHeldItemNotExistPredicate(profile.forbiddenHeldItem),
                new ForbiddenAbilityNotExistPredicate(profile.forbiddenAbility),
                new ForbiddenMoveNotExistPredicate(profile.forbiddenMove)
        );
    }

    @Override
    public List<MessagePredicate<PlayerBattleParticipant>> getBattlePredicates() {
        return predicates;
    }
}
