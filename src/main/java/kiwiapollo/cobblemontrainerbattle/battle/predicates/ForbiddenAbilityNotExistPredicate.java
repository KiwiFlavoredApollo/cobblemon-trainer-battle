package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

public class ForbiddenAbilityNotExistPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final List<Ability> forbidden;

    public ForbiddenAbilityNotExistPredicate(List<Ability> forbidden) {
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

        for (Ability f : forbidden) {
            for (Ability p : player.getParty().toGappyList().stream().filter(Objects::nonNull).map(Pokemon::getAbility).toList()) {
                boolean isAbilityEqual = p.getName().equals(f.getName());

                if (isAbilityEqual) {
                    return false;
                }
            }
        }
        return true;
    }
}
