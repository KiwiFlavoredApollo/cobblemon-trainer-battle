package kiwiapollo.cobblemontrainerbattle.item;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Objects;

public class StoredPokeBall extends Item {
    public StoredPokeBall() {
        super(new Settings().maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (world.isClient()) {
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

    private boolean hasPokemon(ItemStack stack) {
        try {
            StoredPokeBall.getPokemon(stack);
            return true;

        } catch (NullPointerException | IllegalStateException ignored) {
            return false;
        }
    }

    public static Pokemon getPokemon(ItemStack stack) {
        return new Pokemon().loadFromNBT(Objects.requireNonNull(stack.getSubNbt(PokeBallNbt.POKEMON))).clone(true, true);
    }
}
