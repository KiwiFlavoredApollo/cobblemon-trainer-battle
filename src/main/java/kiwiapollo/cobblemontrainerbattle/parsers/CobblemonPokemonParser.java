package kiwiapollo.cobblemontrainerbattle.parsers;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.PokemonStats;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;

import java.util.List;
import java.util.Map;

public class CobblemonPokemonParser {
    public SmogonPokemon toSmogonPokemon(Pokemon pokemon) {
        return new SmogonPokemon(
                getNickName(pokemon),
                pokemon.getSpecies().getResourceIdentifier().toString(),
                Registries.ITEM.getId(pokemon.heldItem().getItem()).toString(),
                pokemon.getAbility().getName(),
                toSmogonGender(pokemon.getGender()),
                pokemon.getNature().getName().toString(),
                pokemon.getLevel(),
                toSmogonStats(pokemon.getIvs()),
                toSmogonStats(pokemon.getEvs()),
                toSmogonMoveSet(pokemon.getMoveSet())
        );
    }

    private String getNickName(Pokemon pokemon) {
        try {
            return pokemon.getNickname().getString();

        } catch (NullPointerException e) {
            return "";
        }
    }

    private String toSmogonGender(Gender gender) {
        return switch(gender) {
            case MALE -> "M";
            case FEMALE -> "F";
            case GENDERLESS -> "";
        };
    }

    private Map<String, Integer> toSmogonStats(PokemonStats cobblemonStats) {
        return Map.of(
                "hp", cobblemonStats.getOrDefault(Stats.HP),
                "atk", cobblemonStats.getOrDefault(Stats.ATTACK),
                "def", cobblemonStats.getOrDefault(Stats.DEFENCE),
                "spa", cobblemonStats.getOrDefault(Stats.SPECIAL_ATTACK),
                "spd", cobblemonStats.getOrDefault(Stats.SPECIAL_DEFENCE),
                "spe", cobblemonStats.getOrDefault(Stats.SPEED)
        );
    }

    private List<String> toSmogonMoveSet(MoveSet cobblemonMoveSet) {
        return cobblemonMoveSet.getMoves().stream()
                .map(Move::getDisplayName)
                .map(MutableText::getString).toList();
    }
}
