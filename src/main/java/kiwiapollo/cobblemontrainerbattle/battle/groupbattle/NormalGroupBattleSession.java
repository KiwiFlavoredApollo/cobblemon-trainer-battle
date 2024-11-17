package kiwiapollo.cobblemontrainerbattle.battle.groupbattle;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.factory.NormalGroupBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.*;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerGroupProfileStorage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;

public class NormalGroupBattleSession extends GroupBattleSession {
    private final List<MessagePredicate<PlayerBattleParticipant>> predicates;

    public NormalGroupBattleSession(ServerPlayerEntity player, Identifier group) {
        super(player, group, new NormalGroupBattleParticipantFactory(group, player));

        TrainerGroupProfile profile = TrainerGroupProfileStorage.getProfileRegistry().get(group);
        this.predicates = List.of(
                new RematchAllowedPredicate(group, profile.isRematchAllowed),
                new MaximumPartyLevelPredicate(profile.maximumPartyLevel),
                new MinimumPartyLevelPredicate(profile.minimumPartyLevel),
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
