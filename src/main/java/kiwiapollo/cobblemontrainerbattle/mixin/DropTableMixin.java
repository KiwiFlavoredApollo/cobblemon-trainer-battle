package kiwiapollo.cobblemontrainerbattle.mixin;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.drop.DropTable;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.TrainerBattleActor;
import kotlin.ranges.IntRange;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

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
        try {
            UUID battleId = ((PokemonEntity) entity).getBattleId();
            PokemonBattle battle = Cobblemon.INSTANCE.getBattleRegistry().getBattle(battleId);
            List<BattleActor> actors = StreamSupport.stream(battle.getActors().spliterator(), false).toList();
            return actors.stream().anyMatch(actor -> actor instanceof TrainerBattleActor);

        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }
}
