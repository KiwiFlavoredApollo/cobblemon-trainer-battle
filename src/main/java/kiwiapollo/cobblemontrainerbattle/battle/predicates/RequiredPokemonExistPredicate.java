package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

public class RequiredPokemonExistPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final List<Pokemon> required;

    public RequiredPokemonExistPredicate(List<Pokemon> required) {
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

        for (Pokemon r : required) {
            for (Pokemon p : player.getParty().toGappyList().stream().filter(Objects::nonNull).toList()) {
                boolean isSpeciesEqual = p.getSpecies().equals(r.getSpecies());
                boolean isFormEqual = p.getForm().equals(r.getForm());

                if (isSpeciesEqual && isFormEqual) {
                    return true;
                }
            }
        }
        return false;
    }
}
