package kiwiapollo.cobblemontrainerbattle.entity;

import com.cobblemon.mod.common.Cobblemon;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.global.history.EntityRecord;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.EntityBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.global.history.PlayerHistory;
import kiwiapollo.cobblemontrainerbattle.global.history.PlayerHistoryStorage;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TrainerEntity extends PathAwareEntity {
    public static final int FLEE_DISTANCE = 20;
    private static final String FALLBACK_TRAINER = "cobblemontrainerbattle:radicalred/player_red";
    private static final String FALLBACK_TEXTURE = "cobblemontrainerbattle:textures/entity/trainer/slim/red_piikapiika.png";
    private static final TrackedData<String> TRAINER = DataTracker.registerData(TrainerEntity.class, TrackedDataHandlerRegistry.STRING);

    private final String trainer;
    private TrainerBattle trainerBattle;

    public TrainerEntity(EntityType<? extends PathAwareEntity> type, World world, String trainer) {
        super(type, world);

        this.trainer = trainer;
        this.trainerBattle = null;
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.goalSelector.add(2, new AttackGoal(this));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (this.getWorld().isClient) {
            return ActionResult.SUCCESS;
        }

        if (!(player instanceof ServerPlayerEntity)) {
            return ActionResult.FAIL;
        }

        startTrainerBattle((ServerPlayerEntity) player, hand);

        return super.interactMob(player, hand);
    }

    private void startTrainerBattle(ServerPlayerEntity player, Hand hand) {
        try {
            if (isTrainerBattleExist()) {
                return;
            }

            TrainerBattle trainerBattle = new EntityBackedTrainerBattle(
                    new PlayerBattleParticipantFactory(player, getLevelMode(getDataTracker().get(TRAINER))).create(),
                    new TrainerBattleParticipantFactory(getDataTracker().get(TRAINER)).create(),
                    this
            );
            trainerBattle.start();

            BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setTrainerBattle(trainerBattle);
            this.trainerBattle = trainerBattle;

            this.setVelocity(0, 0, 0);
            this.setAiDisabled(true);
            this.velocityDirty = true;

            Criteria.PLAYER_INTERACTED_WITH_ENTITY.trigger(player, player.getStackInHand(hand), this);

        } catch (BattleStartException ignored) {

        }
    }

    private LevelMode getLevelMode(String trainer) {
        return TrainerStorage.getInstance().get(trainer).getLevelMode();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (isTrainerBattleExist()) {
            return false;
        }

        boolean isDamaged = super.damage(source, amount);
        boolean isLivingEntityAttacker = source.getAttacker() instanceof LivingEntity;

        if (isDamaged && isLivingEntityAttacker && !source.isSourceCreativePlayer()) {
            this.setTarget((LivingEntity) source.getAttacker());
        }

        return isDamaged;
    }

    private boolean isTrainerBattleExist() {
        try {
            UUID battleId = trainerBattle.getBattleId();
            return Objects.nonNull(Cobblemon.INSTANCE.getBattleRegistry().getBattle(battleId));

        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (damageSource.getSource() instanceof ServerPlayerEntity player) {
            PlayerHistory history = PlayerHistoryStorage.getInstance().getOrCreate(player.getUuid());
            EntityRecord record = (EntityRecord) history.getOrCreate(getDataTracker().get(TRAINER));
            record.setKillCount(record.getKillCount() + 1);
            CustomCriteria.KILL_TRAINER_CRITERION.trigger(player);
        }

        if(isTrainerBattleExist()) {
            UUID battleId = trainerBattle.getBattleId();
            Cobblemon.INSTANCE.getBattleRegistry().getBattle(battleId).end();
        }

        super.onDeath(damageSource);
    }

    public void onPlayerDefeat() {
        setAiDisabled(false);
    }

    public void onPlayerVictory() {
        dropDefeatInBattleLoot();
        discard();
    }

    private void dropDefeatInBattleLoot() {
        Identifier identifier = this.getLootTable();
        LootTable lootTable = this.getWorld().getServer().getLootManager().getLootTable(identifier);
        LootContextParameterSet.Builder builder = (new LootContextParameterSet.Builder((ServerWorld)this.getWorld()))
                .add(LootContextParameters.THIS_ENTITY, this)
                .add(LootContextParameters.ORIGIN, this.getPos())
                .add(LootContextParameters.DAMAGE_SOURCE, getWorld().getDamageSources().generic());

        LootContextParameterSet lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
        lootTable.generateLoot(lootContextParameterSet, this.getLootTableSeed(), this::dropStack);
    }

    public PacketByteBuf createPacket() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(this.getId());
        buf.writeString(this.getDataTracker().get(TRAINER));
        return buf;
    }

    public Identifier getTexture() {
        return getTrainerTexture(getDataTracker().get(TRAINER));
    }

    private Identifier getTrainerTexture(String trainer) {
        try {
            return TrainerStorage.getInstance().get(trainer).getTexture();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Trainer", getDataTracker().get(TRAINER));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        try {
            super.readCustomDataFromNbt(nbt);
            getDataTracker().set(TRAINER, nbt.getString("Trainer"));

        } catch (NullPointerException e) {
            discard();
        }
    }

    /**
     * When TrainerEntity is spawned by Mob Spawner, trainer and texture fields are not initialized.
     * Seems like Mob Spawners do not call constructors when creating entities.
     */
    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if (spawnReason.equals(SpawnReason.SPAWNER)  && Objects.equals(trainer, "")) {
            RandomSpawnableTrainerFactory factory = new RandomSpawnableTrainerFactory(trainer -> true);
            getDataTracker().set(TRAINER, factory.create());
        }

        this.getDataTracker().set(TRAINER, trainer);

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TRAINER, FALLBACK_TRAINER);
    }

    public String getFallbackTexture() {
        return FALLBACK_TEXTURE;
    }
}
