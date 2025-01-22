package kiwiapollo.cobblemontrainerbattle.mixin;

import com.cobblemon.mod.common.api.drop.DropTable;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContext;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import kotlin.ranges.IntRange;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@Mixin(DropTable.class)
public class DropTableMixin {
    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    public void cancelDrop(
            LivingEntity entity,
            ServerWorld world,
            Vec3d pos,
            ServerPlayerEntity player,
            IntRange amount,
            CallbackInfo callbackInfo
    ) {
        if (isTrainerBattle(entity, world)) {
            callbackInfo.cancel();
        }
    }

    private boolean isTrainerBattle(LivingEntity entity, ServerWorld world) {
        PokemonEntity pokemon = (PokemonEntity) entity;

        List<UUID> battleIds = new ArrayList<>();

        for (ServerPlayerEntity player : world.getPlayers()) {
            try {
                BattleContext context = BattleContextStorage.getInstance().getOrCreate(player.getUuid());
                battleIds.add(context.getTrainerBattle().getBattleId());

            } catch (NullPointerException ignored) {

            }
        }

        return battleIds.contains(pokemon.getBattleId());
    }
}
