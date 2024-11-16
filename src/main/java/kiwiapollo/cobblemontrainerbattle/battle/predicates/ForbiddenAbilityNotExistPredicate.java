package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ForbiddenAbilityNotExistPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final List<String> forbidden;

    public ForbiddenAbilityNotExistPredicate(List<String> forbidden) {
        this.forbidden = forbidden.stream().filter(Objects::nonNull).toList();
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.literal("");
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        if (forbidden.isEmpty()) {
            return true;
        }

        Set<String> abilities = player.getParty().toGappyList().stream()
                .filter(Objects::nonNull)
                .map(Pokemon::getAbility)
                .map(Ability::getName)
                .collect(Collectors.toSet());

        for (String p : abilities) {
            for (String f : forbidden) {
                boolean isAbilityEqual = p.equals(f);

                if (isAbilityEqual) {
                    return false;
                }
            }
        }
        return true;
    }
}
