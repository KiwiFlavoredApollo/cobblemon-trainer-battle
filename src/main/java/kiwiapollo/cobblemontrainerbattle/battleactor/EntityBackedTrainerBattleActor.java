package kiwiapollo.cobblemontrainerbattle.battleactor;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.api.battles.model.actor.EntityBackedBattleActor;
import com.cobblemon.mod.common.api.battles.model.actor.FleeableBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import kotlin.Pair;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class EntityBackedTrainerBattleActor extends AIBattleActor implements EntityBackedBattleActor<TrainerEntity>, FleeableBattleActor {
    private final String trainerName;
    private final TrainerEntity trainerEntity;

    public EntityBackedTrainerBattleActor(
            String trainerName,
            UUID uuid,
            List<BattlePokemon> pokemonList,
            BattleAI artificialDecider,
            TrainerEntity trainerEntity) {
        super(uuid, pokemonList, artificialDecider);
        this.trainerName = trainerName;
        this.trainerEntity = trainerEntity;
    }

    @Override
    public TrainerEntity getEntity() {
        return this.trainerEntity;
    }

    @NotNull
    @Override
    public ActorType getType() {
        return ActorType.NPC;
    }

    @NotNull
    @Override
    public MutableText getName() {
        return Text.literal(this.trainerName);
    }

    @NotNull
    @Override
    public MutableText nameOwned(@NotNull String s) {
        return Text.literal(s).append(getName());
    }

    @Override
    public float getFleeDistance() {
        return CobblemonTrainerBattle.FLEE_DISTANCE;
    }

    @Nullable
    @Override
    public Pair<ServerWorld, Vec3d> getWorldAndPosition() {
        RegistryKey<World> entityWorldRegistryKey = trainerEntity.getEntityWorld().getRegistryKey();
        return new Pair<>(trainerEntity.getServer().getWorld(entityWorldRegistryKey), trainerEntity.getPos());
    }
}
