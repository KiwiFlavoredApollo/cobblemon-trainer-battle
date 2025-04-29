package kiwiapollo.cobblemontrainerbattle.entity;

import com.cobblemon.mod.common.Cobblemon;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.EntityBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import kiwiapollo.cobblemontrainerbattle.global.history.EntityRecord;
import kiwiapollo.cobblemontrainerbattle.global.history.PlayerHistory;
import kiwiapollo.cobblemontrainerbattle.global.history.PlayerHistoryStorage;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public abstract class TrainerEntity extends PathAwareEntity implements TrainerBehavior {
    public static final int FLEE_DISTANCE = 20;
    private static final String FALLBACK_TRAINER = "radicalred/player_red";
    private static final TrackedData<String> TRAINER = DataTracker.registerData(TrainerEntity.class, TrackedDataHandlerRegistry.STRING);

    private TrainerBattle trainerBattle;

    public TrainerEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);

        this.trainerBattle = null;
    }

    /**
     * When TrainerEntity is spawned by Mob Spawner, trainer and texture fields are not initialized.
     * Seems like Mob Spawners do not call constructors when creating entities.
     */
    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if (spawnReason.equals(SpawnReason.SPAWNER)) {
            RandomSpawnableTrainerFactory factory = new RandomSpawnableTrainerFactory(trainer -> true);
            getDataTracker().set(TRAINER, factory.create());
        }

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(TRAINER, FALLBACK_TRAINER);
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

        return super.damage(source, amount);
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

    @Override
    public void setTrainer(String trainer) {
        this.getDataTracker().set(TRAINER, trainer);
    }

    @Override
    public Identifier getTexture() {
        String trainer = getDataTracker().get(TRAINER);
        return TrainerStorage.getInstance().get(trainer).getTexture();
    }

    @Override
    public void onPlayerVictory() {

    }

    @Override
    public void onPlayerDefeat() {
        setAiDisabled(false);
    }

    protected void dropDefeatedInBattleLoot() {
        Identifier identifier = this.getLootTable();
        LootTable lootTable = this.getWorld().getServer().getLootManager().getLootTable(identifier);
        LootContextParameterSet.Builder builder = (new LootContextParameterSet.Builder((ServerWorld)this.getWorld()))
                .add(LootContextParameters.THIS_ENTITY, this)
                .add(LootContextParameters.ORIGIN, this.getPos())
                .add(LootContextParameters.DAMAGE_SOURCE, getWorld().getDamageSources().generic());

        LootContextParameterSet lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
        lootTable.generateLoot(lootContextParameterSet, this.getLootTableSeed(), this::dropStack);
    }

    protected TrainerBattle getTrainerBattle() {
        return trainerBattle;
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
    }
}
