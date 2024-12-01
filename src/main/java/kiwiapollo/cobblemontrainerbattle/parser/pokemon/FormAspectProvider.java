package kiwiapollo.cobblemontrainerbattle.parser.pokemon;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.aspect.AspectProvider;
import com.cobblemon.mod.common.pokemon.Pokemon;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class FormAspectProvider implements AspectProvider {
    public static final Map<String, Set<String>> FORM_ASPECTS = Map.ofEntries(
            Map.entry("Alola", Set.of("alolan")),
            Map.entry("Alola Bias", Set.of("region-bias-alola")),

            Map.entry("Galar", Set.of("galarian")),
            Map.entry("Galar Bias", Set.of("region-bias-galar")),

            Map.entry("Hisui", Set.of("hisuian")),
            Map.entry("Hisui Bias", Set.of("region-bias-hisui")),

            Map.entry("Paldea", Set.of("paldean")),
            Map.entry("Paldea Bias", Set.of("region-bias-paldea")),

            Map.entry("Paldea-Aqua", Set.of("paldean-breed-aqua")),
            Map.entry("Paldea-Blaze", Set.of("paldean-breed-blaze")),
            Map.entry("Paldea-Combat", Set.of("paldean-breed-combat")),

            Map.entry("Therian", Set.of("therian")),

            Map.entry("Zen", Set.of("zen_mode")),
            Map.entry("Galar-Zen", Set.of("galarian", "zen_mode"))
    );

    @Override
    public @NotNull Set<String> provide(@NotNull PokemonProperties pokemonProperties) {
        return Set.of();
    }

    @Override
    public @NotNull Set<String> provide(@NotNull Pokemon pokemon) {
        try {
            return Objects.requireNonNull(FORM_ASPECTS.get(pokemon.getForm().getName()));
        } catch (NullPointerException e) {
            return Set.of();
        }
    }

    @Override
    public @NotNull AspectProvider register() {
        return AspectProvider.Companion.register(this);
    }
}
