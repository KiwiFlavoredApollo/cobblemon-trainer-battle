package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemon;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

public class ForbiddenPokemonNotExistPredicate implements MessagePredicate<PlayerBattleParticipant> {
    private final List<ShowdownPokemon> forbidden;

    public ForbiddenPokemonNotExistPredicate(List<ShowdownPokemon> forbidden) {
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

        for (Pokemon p : player.getParty().toGappyList().stream().filter(Objects::nonNull).toList()) {
            for (ShowdownPokemon f : forbidden) {
                boolean isSpeciesEqual = normalize(p.getSpecies().getName()).equals(normalize(f.species));
                boolean isFormEqual = p.getForm().getName().equals(f.form);

                if (isSpeciesEqual && isFormEqual) {
                    return false;
                }
            }
        }
        return true;
    }

    private String normalize(String species) {
        String normalized = species;
        normalized = normalized.toLowerCase();
        normalized = normalized.replaceAll("[-\\s]", "");
        normalized = normalized.replaceAll("^cobblemon:", "");
        return normalized;
    }
}
