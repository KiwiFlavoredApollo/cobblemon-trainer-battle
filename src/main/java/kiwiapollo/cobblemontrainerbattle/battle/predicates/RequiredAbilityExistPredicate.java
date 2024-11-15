package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

public class RequiredAbilityExistPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final List<Ability> required;

    public RequiredAbilityExistPredicate(List<Ability> required) {
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

        for (Ability r : required) {
            for (Ability p : player.getParty().toGappyList().stream().filter(Objects::nonNull).map(Pokemon::getAbility).toList()) {
                boolean isAbilityEqual = p.getName().equals(r.getName());

                if (isAbilityEqual) {
                    return true;
                }
            }
        }
        return false;
    }
}
