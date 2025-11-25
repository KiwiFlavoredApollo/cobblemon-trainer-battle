package kiwiapollo.cobblemontrainerbattle.command.rentalpokemon;

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

public abstract class PokemonStatusPrinter {
    protected void printPokemonStatus(PartyStore party, ServerPlayerEntity player) {
        for (int i = 0; i < party.occupied(); i++) {
            Pokemon pokemon = party.get(i);

            player.sendMessage(Text.literal("[" + (i + 1) + "] ").append(getPokemonSpecies(pokemon)).formatted(Formatting.YELLOW));
            player.sendMessage(Text.translatable("cobblemon.ui.info.ability").append(" : ").append(getPokemonAbility(pokemon)));
            player.sendMessage(Text.translatable("cobblemon.ui.info.nature").append(" : ").append(getPokemonNature(pokemon)));
            player.sendMessage(Text.translatable("cobblemon.ui.moves").append(" : ").append(getPokemonMoveSet(pokemon.getMoveSet())));
            player.sendMessage(Text.translatable("cobblemon.ui.stats.ivs").append(" : ").append(getPokemonStats(pokemon.getIvs())));
            player.sendMessage(Text.translatable("cobblemon.ui.stats.evs").append(" : ").append(getPokemonStats(pokemon.getEvs())));
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
