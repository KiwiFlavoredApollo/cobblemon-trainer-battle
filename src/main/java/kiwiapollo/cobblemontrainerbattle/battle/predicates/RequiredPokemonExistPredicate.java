package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemon;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

public class RequiredPokemonExistPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final List<ShowdownPokemon> required;

    public RequiredPokemonExistPredicate(List<ShowdownPokemon> required) {
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

        for (Pokemon p : player.getParty().toGappyList().stream().filter(Objects::nonNull).toList()) {
            for (ShowdownPokemon r : required) {
                boolean isSpeciesEqual = normalize(p.getSpecies().getName()).equals(normalize(r.species));
                boolean isFormEqual = p.getForm().getName().equals(r.form);

                if (isSpeciesEqual && isFormEqual) {
                    return true;
                }
            }
        }
        return false;
    }

    private String normalize(String species) {
        String normalized = species;
        normalized = normalized.toLowerCase();
        normalized = normalized.replace("cobblemon:", "");
        return normalized;
    }
}
