package kiwiapollo.cobblemontrainerbattle.entities;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.SpecificTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainerEntity extends PathAwareEntity {
    private static final List<Identifier> TEXTURES = List.of(
            Identifier.of("minecraft", "textures/entity/player/wide/steve.png"),
            Identifier.of("minecraft", "textures/entity/player/wide/alex.png"),
            Identifier.of("minecraft", "textures/entity/player/wide/zuri.png")
    );

    private final String trainerResourcePath;
    private final Identifier texture;

    public TrainerEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);

        this.trainerResourcePath = getRandomTrainerResourcePath();
        this.texture = getRandomTexture();
    }

    private String getRandomTrainerResourcePath() {
        List<String> trainerFilePaths = new ArrayList<>(
                CobblemonTrainerBattle.trainerFiles.keySet().stream().toList());
        Collections.shuffle(trainerFilePaths);
        return trainerFilePaths.get(0);
    }

    private Identifier getRandomTexture() {
        List<Identifier> textures = new ArrayList<>(TEXTURES);
        Collections.shuffle(textures);
        return textures.get(0);
    }

    public Identifier getTexture() {
        return texture;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(2, new LookAroundGoal(this));
        this.goalSelector.add(3, new AttackGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (this.getWorld().isClient) {
            player.playSound(SoundEvents.ENTITY_VILLAGER_YES, 1.0F, 1.0F);
            return ActionResult.SUCCESS;
        }

        if (player instanceof ServerPlayerEntity) {
            this.setVelocity(0, 0, 0);
            this.setAiDisabled(true);
            this.velocityDirty = true;

            Trainer trainer = new SpecificTrainerFactory().create((ServerPlayerEntity) player, trainerResourcePath);
            EntityBackedTrainerBattle.startSpecificTrainerBattleWithStatusQuo((ServerPlayerEntity) player, trainer, this);
        }

        return super.interactMob(player, hand);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean isDamaged = super.damage(source, amount);
        boolean isLivingEntityAttacker = source.getAttacker() instanceof LivingEntity;

        if (isDamaged && isLivingEntityAttacker) {
            this.setTarget((LivingEntity) source.getAttacker());
        }

        return isDamaged;
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
    }
}
