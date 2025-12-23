package kiwiapollo.cobblemontrainerbattle.entity;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.status.PersistentStatus;
import com.cobblemon.mod.common.pokemon.status.statuses.persistent.*;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.gamerule.CustomGameRule;
import kiwiapollo.cobblemontrainerbattle.template.RandomTrainerSelector;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplateStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TrainerEntity extends AbstractPokemonTrainerEntity implements Angerable {
    private static final String TRAINER_NBT_KEY = "Trainer";

    private static final TrackedData<String> TEXTURE = DataTracker.registerData(TrainerEntity.class, TrackedDataHandlerRegistry.STRING);

    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);

    private Identifier trainer;
    private @Nullable UUID angryAt;
    private int angerTime;

    public TrainerEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);

        this.trainer = TrainerTemplate.PLAYER_RED;
    }

    /**
     * If TrainerEntity is spawned by Mob Spawner, TRAINER and TEXTURE are not initialized.
     * EntityTag NBT of a Trainer Spawn Egg is not passed to SpawnData NBT of a Mob Spawner.
     */
    @Override
    @Nullable
    public EntityData initialize(
            ServerWorldAccess world,
            LocalDifficulty difficulty,
            SpawnReason spawnReason,
            @Nullable EntityData entityData,
            @Nullable NbtCompound entityNbt
    ) {
        setTrainer(new RandomTrainerSelector(template -> {
            boolean result = true;

            result &= template.isSpawnAllowed();
            result &= !template.getTeam().isEmpty();

            return result;
        }).select());

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        this.getDataTracker().startTracking(TEXTURE, TrainerTexture.RED.toString());
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
    public boolean damage(DamageSource source, float amount) {
        if (isBusyWithPokemonBattle()) {
            return false;
        }

        return super.damage(source, amount);
    }

    private boolean isBusyWithPokemonBattle() {
        try {
            return Objects.nonNull(Cobblemon.INSTANCE.getBattleRegistry().getBattle(getBattleId()));

        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (!getServer().getGameRules().getBoolean(CustomGameRule.DO_TRAINER_APPLY_STATUS_CONDITION)) {
            return super.tryAttack(target);
        }

        if (!(target instanceof ServerPlayerEntity player)) {
            return super.tryAttack(target);
        }

        try {
            Pokemon pokemon = selectRandomPokemon(player);
            PersistentStatus status = selectRandomStatusCondition();
            pokemon.applyStatus(status);

        } catch (IndexOutOfBoundsException ignored) {

        };

        return super.tryAttack(target);
    }

    private Pokemon selectRandomPokemon(ServerPlayerEntity player) {
        List<Pokemon> random = new ArrayList<>(Cobblemon.INSTANCE.getStorage()
                .getParty(player).toGappyList().stream()
                .filter(Objects::nonNull)
                .filter(pokemon -> !pokemon.isFainted())
                .filter(pokemon -> !hasStatusCondition(pokemon))
                .toList());
        Collections.shuffle(random);
        return random.get(0);
    }

    private boolean hasStatusCondition(Pokemon pokemon) {
        try {
            return !pokemon.getStatus().isExpired();

        } catch (NullPointerException e) {
            return false;
        }
    }

    private PersistentStatus selectRandomStatusCondition() {
        List<PersistentStatus> random = new ArrayList<>(List.of(
                new ParalysisStatus(),
                new BurnStatus(),
                new FrozenStatus(),
                new SleepStatus(),
                new PoisonStatus(),
                new PoisonBadlyStatus()
        ));
        Collections.shuffle(random);
        return random.get(0);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString(TRAINER_NBT_KEY, trainer.toString());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        try {
            super.readCustomDataFromNbt(nbt);
            setTrainer(toDefaultedIdentifier(nbt.getString(TRAINER_NBT_KEY)));

        } catch (NullPointerException e) {
            discard();
        }
    }

    private Identifier toDefaultedIdentifier(String string) {
        if (string.contains(String.valueOf(Identifier.NAMESPACE_SEPARATOR))) {
            return Identifier.tryParse(string);

        } else {
            return Identifier.of(CobblemonTrainerBattle.MOD_ID, string);
        }
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

    @Override
    public Identifier getTrainer() {
        return trainer;
    }

    @Override
    public void setTrainer(Identifier trainer) {
        this.trainer = trainer;
        this.getDataTracker().set(TEXTURE, getTexture(trainer).toString());
    }

    @Nullable
    private Identifier getTexture(Identifier trainer) {
        try {
            return TrainerTemplateStorage.getInstance().get(trainer).getTexture();

        } catch (NullPointerException e) {
            return TrainerTexture.RED;
        }
    }

    @Override
    public Identifier getTexture() {
        return Identifier.tryParse(getDataTracker().get(TEXTURE));
    }

    @Override
    public void onPlayerVictory() {
        dropDefeatedInBattleLoot();
        discard();
    }

    private void dropDefeatedInBattleLoot() {
        Identifier identifier = this.getLootTable();
        LootTable lootTable = this.getWorld().getServer().getLootManager().getLootTable(identifier);
        LootContextParameterSet.Builder builder = (new LootContextParameterSet.Builder((ServerWorld)this.getWorld()))
                .add(LootContextParameters.THIS_ENTITY, this)
                .add(LootContextParameters.ORIGIN, this.getPos())
                .add(LootContextParameters.DAMAGE_SOURCE, getWorld().getDamageSources().generic());

        LootContextParameterSet lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
        lootTable.generateLoot(lootContextParameterSet, this.getLootTableSeed(), this::dropStack);
    }

    @Override
    public void onPlayerDefeat() {
        setAiDisabled(false);
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
    }
}
