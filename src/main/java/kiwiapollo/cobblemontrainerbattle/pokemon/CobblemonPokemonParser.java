package kiwiapollo.cobblemontrainerbattle.pokemon;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.PokemonStats;
import net.minecraft.registry.Registries;

import java.util.List;
import java.util.Map;

public class CobblemonPokemonParser {
    public ShowdownPokemon toShowdownPokemon(Pokemon pokemon) {
        return new ShowdownPokemon(
                getNickName(pokemon),
                pokemon.getSpecies().getResourceIdentifier().toString(),
                pokemon.getForm().getName(),
                pokemon.getShiny(),
                Registries.ITEM.getId(pokemon.heldItem().getItem()).toString(),
                pokemon.getAbility().getName(),
                toShowdownGender(pokemon.getGender()),
                pokemon.getNature().getName().toString(),
                pokemon.getLevel(),
                toShowdownStats(pokemon.getEvs()),
                toShowdownStats(pokemon.getIvs()),
                toShowdownMoveSet(pokemon.getMoveSet())
        );
    }

    private String getNickName(Pokemon pokemon) {
        try {
            return pokemon.getNickname().getString();

        } catch (NullPointerException e) {
            return "";
        }
    }

    private String toShowdownGender(Gender gender) {
        return switch(gender) {
            case MALE -> "M";
            case FEMALE -> "F";
            case GENDERLESS -> "";
        };
    }

    private Map<String, Integer> toShowdownStats(PokemonStats cobblemonStats) {
        return Map.of(
                "hp", cobblemonStats.getOrDefault(Stats.HP),
                "atk", cobblemonStats.getOrDefault(Stats.ATTACK),
                "def", cobblemonStats.getOrDefault(Stats.DEFENCE),
                "spa", cobblemonStats.getOrDefault(Stats.SPECIAL_ATTACK),
                "spd", cobblemonStats.getOrDefault(Stats.SPECIAL_DEFENCE),
                "spe", cobblemonStats.getOrDefault(Stats.SPEED)
        );
    }

    private List<String> toShowdownMoveSet(MoveSet cobblemonMoveSet) {
        return cobblemonMoveSet.getMoves().stream().map(Move::getName).toList();
    }
}
