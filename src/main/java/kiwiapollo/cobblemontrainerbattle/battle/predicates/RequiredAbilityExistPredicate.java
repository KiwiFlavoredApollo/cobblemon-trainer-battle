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

public class RequiredAbilityExistPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final List<String> required;

    public RequiredAbilityExistPredicate(List<String> required) {
        this.required = required.stream().filter(Objects::nonNull).toList();
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.literal("");
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        if (required.isEmpty()) {
            return true;
        }

        Set<String> abilities = player.getParty().toGappyList().stream()
                .filter(Objects::nonNull)
                .map(Pokemon::getAbility)
                .map(Ability::getName)
                .collect(Collectors.toSet());

        for (String p : abilities) {
            for (String r : required) {
                boolean isAbilityEqual = p.equals(r);

                if (isAbilityEqual) {
                    return true;
                }
            }
        }
        return false;
    }
}
