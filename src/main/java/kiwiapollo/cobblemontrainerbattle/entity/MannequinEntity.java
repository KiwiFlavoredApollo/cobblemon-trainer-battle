package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.template.RandomTrainerFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MannequinEntity extends BattleEntity {
    public MannequinEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }

    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        setPersistent();

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new LookAtEntityGoal(this, ServerPlayerEntity.class, 4));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!source.isOf(DamageTypes.GENERIC_KILL)) {
            return false;
        }

        return super.damage(source, amount);
    }

    @Override
    protected void pushAway(Entity entity) {

    }

    @Override
    public void pushAwayFrom(Entity entity) {
        
    }

    @Override
    public void onPlayerVictory() {

    }

    @Override
    public void onPlayerDefeat() {
        setAiDisabled(false);
    }

    public static class Factory implements EntityType.EntityFactory<MannequinEntity>  {
        private final RandomTrainerFactory identifier;

        public Factory() {
            this.identifier = new RandomTrainerFactory(template -> {
                boolean result = true;

                result &= template.isSpawnAllowed();
                result &= !template.getTeam().isEmpty();

                return result;
            });
        }

        @Override
        public MannequinEntity create(EntityType<MannequinEntity> type, World world) {
            MannequinEntity entity = new MannequinEntity(type, world);
            entity.setTrainer(identifier.create());
            return entity;
        }
    }
}
