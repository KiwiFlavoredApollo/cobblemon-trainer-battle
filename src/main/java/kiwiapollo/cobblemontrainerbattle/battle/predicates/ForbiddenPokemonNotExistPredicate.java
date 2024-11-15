package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

public class ForbiddenPokemonNotExistPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final List<Pokemon> forbidden;

    public ForbiddenPokemonNotExistPredicate(List<Pokemon> forbidden) {
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

        for (Pokemon f : forbidden) {
            for (Pokemon p : player.getParty().toGappyList().stream().filter(Objects::nonNull).toList()) {
                boolean isSpeciesEqual = p.getSpecies().equals(f.getSpecies());
                boolean isFormEqual = p.getForm().equals(f.getForm());

                if (isSpeciesEqual && isFormEqual) {
                    return false;
                }
            }
        }
        return true;
    }
}
