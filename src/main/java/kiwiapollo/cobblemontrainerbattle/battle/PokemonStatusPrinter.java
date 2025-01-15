package kiwiapollo.cobblemontrainerbattle.battle;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.PokemonStats;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Objects;

public class PokemonStatusPrinter {
    private final ServerPlayerEntity player;

    public PokemonStatusPrinter(ServerPlayerEntity player) {
        this.player = player;
    }

    public void print(PartyStore party) {
        List<Pokemon> pokemons = party.toGappyList().stream().filter(Objects::nonNull).toList();
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon pokemon = pokemons.get(i);

            player.sendMessage(Text.literal("[" + (i + 1) + "] ").append(getPokemonSpecies(pokemon)).formatted(Formatting.YELLOW));
            player.sendMessage(Text.literal("Ability ").append(getPokemonAbility(pokemon)));
            player.sendMessage(Text.literal("Nature ").append(getPokemonNature(pokemon)));
            player.sendMessage(Text.literal("MoveSet ").append(getPokemonMoveSet(pokemon.getMoveSet())));
            player.sendMessage(Text.literal("EVs ").append(getPokemonStats(pokemon.getEvs())));
            player.sendMessage(Text.literal("IVs ").append(getPokemonStats(pokemon.getIvs())));
        }
    }

    private MutableText getPokemonSpecies(Pokemon pokemon) {
        return pokemon.getSpecies().getTranslatedName();
    }

    private MutableText getPokemonAbility(Pokemon pokemon) {
        return Text.translatable(pokemon.getAbility().getDisplayName());
    }

    private MutableText getPokemonNature(Pokemon pokemon) {
        return Text.translatable(pokemon.getNature().getDisplayName());
    }

    private MutableText getPokemonMoveSet(MoveSet moveSet) {
        MutableText text = Text.literal("");
        for (Move move : moveSet.getMoves()) {
            if (text.equals(Text.literal(""))) {
                text.append(move.getDisplayName());
            } else {
                text.append(Text.literal(" / ")).append(move.getDisplayName());
            }
        }
        return text;
    }

    private MutableText getPokemonStats(PokemonStats stats) {
        return Text.literal("")
                .append(String.format("HP %d", stats.get(Stats.HP))).append(" / ")
                .append(String.format("ATK %d", stats.get(Stats.ATTACK))).append(" / ")
                .append(String.format("DEF %d", stats.get(Stats.DEFENCE))).append(" / ")
                .append(String.format("SPA %d", stats.get(Stats.SPECIAL_ATTACK))).append(" / ")
                .append(String.format("SPD %d", stats.get(Stats.SPECIAL_DEFENCE))).append(" / ")
                .append(String.format("SPE %d", stats.get(Stats.SPEED)));
    }
}
