package kiwiapollo.cobblemontrainerbattle.battle.predicate;

import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ForbiddenAbilityPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final List<String> forbidden;
    private String error;

    public ForbiddenAbilityPredicate(List<String> forbidden) {
        this.forbidden = forbidden.stream().filter(Objects::nonNull).toList();
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.forbidden_ability", error);
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        Set<String> party = player.getParty().toGappyList().stream()
                .filter(Objects::nonNull)
                .map(Pokemon::getAbility)
                .map(Ability::getName)
                .collect(Collectors.toSet());

        for (String f : forbidden) {
            if (party.contains(f)) {
                error = f;
                return false;
            }
        }
        return true;
    }
}
