package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class AnchorEntity extends TrainerEntity {
    public AnchorEntity(EntityType<? extends PathAwareEntity> type, World world, String trainer) {
        super(type, world, trainer);
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new LookAtEntityGoal(this, ServerPlayerEntity.class, 4));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return source.isOf(DamageTypes.GENERIC_KILL);
    }

    @Override
    protected void pushAway(Entity entity) {

    }

    @Override
    public void pushAwayFrom(Entity entity) {
        
    }
}
