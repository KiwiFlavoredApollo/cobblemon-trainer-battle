package kiwiapollo.cobblemontrainerbattle.mixin;

import com.cobblemon.mod.common.api.drop.DropTable;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(DropTable.class)
public class DropTableMixin {
    @Inject(method = "drop", at = @At("HEAD"))
    public void cancelDrop(
            LivingEntity entity,
            ServerWorld world,
            Vec3d pos,
            ServerPlayerEntity player,
            int amount,
            CallbackInfo callbackInfo
    ) {
        try {
            PokemonEntity pokemonEntity = (PokemonEntity) entity;

            if (!pokemonEntity.getPokemon().isWild()) {
                throw new AssertionError();
            }

            if (!CobblemonTrainerBattle.sessions.containsKey(pokemonEntity.getBattleId())) {
                throw new AssertionError();
            }

            callbackInfo.cancel();

        } catch (ClassCastException | AssertionError ignored) {

        }
    }
}
