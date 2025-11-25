package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.template.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StaticTrainerEntity extends TrainerEntity {
    public StaticTrainerEntity(EntityType<? extends PathAwareEntity> type, World world) {
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

    public static class Factory implements EntityType.EntityFactory<StaticTrainerEntity>  {
        private final RandomTrainerFactory identifier;

        public Factory() {
            this.identifier = new RandomTrainerFactory(TrainerTemplate::isSpawningAllowed);
        }

        @Override
        public StaticTrainerEntity create(EntityType<StaticTrainerEntity> type, World world) {
            StaticTrainerEntity entity = new StaticTrainerEntity(type, world);
            entity.setTrainer(identifier.create());
            return entity;
        }
    }
}
