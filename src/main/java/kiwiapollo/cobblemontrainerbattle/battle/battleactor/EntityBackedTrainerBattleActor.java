package kiwiapollo.cobblemontrainerbattle.battle.battleactor;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.api.battles.model.actor.EntityBackedBattleActor;
import com.cobblemon.mod.common.api.battles.model.actor.FleeableBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;
import kotlin.Pair;
import net.minecraft.entity.LivingEntity;
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

public class EntityBackedTrainerBattleActor extends AIBattleActor implements EntityBackedBattleActor<LivingEntity>, FleeableBattleActor {
    private final String name;
    private final LivingEntity entity;

    public EntityBackedTrainerBattleActor(
            String name,
            UUID uuid,
            List<BattlePokemon> pokemon,
            BattleAI battleAI,
            LivingEntity entity
    ) {
        super(uuid, pokemon, battleAI);
        this.name = name;
        this.entity = entity;
    }

    @Override
    public LivingEntity getEntity() {
        return this.entity;
    }

    @NotNull
    @Override
    public ActorType getType() {
        return ActorType.NPC;
    }

    @NotNull
    @Override
    public MutableText getName() {
        return Text.literal(this.name);
    }

    @NotNull
    @Override
    public MutableText nameOwned(@NotNull String s) {
        return Text.literal(s).append(getName());
    }

    @Override
    public float getFleeDistance() {
        return TrainerEntity.FLEE_DISTANCE;
    }

    @Nullable
    @Override
    public Pair<ServerWorld, Vec3d> getWorldAndPosition() {
        RegistryKey<World> entityWorldRegistryKey = entity.getEntityWorld().getRegistryKey();
        return new Pair<>(entity.getServer().getWorld(entityWorldRegistryKey), entity.getPos());
    }
}
