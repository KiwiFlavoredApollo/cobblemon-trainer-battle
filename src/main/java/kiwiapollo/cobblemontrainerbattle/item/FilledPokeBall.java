package kiwiapollo.cobblemontrainerbattle.item;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.PokemonStats;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FilledPokeBall extends Item {
    public FilledPokeBall() {
        super(new Settings().maxCount(1));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        try {
            Pokemon pokemon = FilledPokeBall.getPokemon(stack);

            Text name = getPokemonSpecies(pokemon).formatted(Formatting.YELLOW);
            Text level = Text.translatable("cobblemon.ui.lv.number", getPokemonLevel(pokemon));

            tooltip.add(Text.literal("").append(name).append(" ").append(level));
            tooltip.add(Text.translatable("cobblemon.ui.info.ability").append(" : ").append(getPokemonAbility(pokemon)));
            tooltip.add(Text.translatable("cobblemon.ui.info.nature").append(" : ").append(getPokemonNature(pokemon)));
            tooltip.add(Text.translatable("cobblemon.ui.moves").append(" : ").append(getPokemonMoveSet(pokemon.getMoveSet())));
            tooltip.add(Text.translatable("cobblemon.ui.stats.ivs").append(" : ").append(getPokemonStats(pokemon.getIvs())));
            tooltip.add(Text.translatable("cobblemon.ui.stats.evs").append(" : ").append(getPokemonStats(pokemon.getEvs())));

        } catch (IllegalStateException | NullPointerException ignored) {

        }
    }

    private int getPokemonLevel(Pokemon pokemon) {
        return pokemon.getLevel();
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

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (world.isClient()) {
            return TypedActionResult.pass(stack);
        }

        if (isPlayerInBattle((ServerPlayerEntity) player)) {
            return TypedActionResult.pass(stack);
        }

        if (!hasPokemon(stack)) {
            return TypedActionResult.pass(stack);
        }

        Pokemon pokemon = getPokemon(stack);

        Cobblemon.INSTANCE.getStorage().getParty((ServerPlayerEntity) player).add(pokemon);

        if (!player.isCreative()) {
            stack.decrement(1);
        }

        return TypedActionResult.success(stack);
    }

    private boolean isPlayerInBattle(ServerPlayerEntity player) {
        return Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) != null;
    }

    private boolean hasPokemon(ItemStack stack) {
        try {
            FilledPokeBall.getPokemon(stack);
            return true;

        } catch (NullPointerException | IllegalStateException ignored) {
            return false;
        }
    }

    public static Pokemon getPokemon(ItemStack stack) {
        JsonObject pokemon = JsonParser.parseString(stack.getOrCreateNbt().getString(PokeBallNbt.POKEMON)).getAsJsonObject();
        return new Pokemon().loadFromJSON(pokemon).clone(true, true);
    }
}
