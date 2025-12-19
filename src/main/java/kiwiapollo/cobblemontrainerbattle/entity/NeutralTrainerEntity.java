package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.template.RandomTrainerFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class NeutralTrainerEntity extends BattleEntity implements Angerable {
    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);

    private @Nullable UUID angryAt;
    private int angerTime;

    public NeutralTrainerEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
    }

    @Override
    public void onPlayerVictory() {
        dropDefeatedInBattleLoot();
        discard();
    }

    @Override
    public void onPlayerDefeat() {
        setAiDisabled(false);
    }

    @Override
    public int getAngerTime() {
        return angerTime;
    }

    @Override
    public void setAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }

    @Override
    public @Nullable UUID getAngryAt() {
        return angryAt;
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }

    public static class Factory implements EntityType.EntityFactory<NeutralTrainerEntity> {
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
        public NeutralTrainerEntity create(EntityType<NeutralTrainerEntity> type, World world) {
            NeutralTrainerEntity entity = new NeutralTrainerEntity(type, world);
            entity.setTrainer(identifier.create());
            return entity;
        }
    }
}
