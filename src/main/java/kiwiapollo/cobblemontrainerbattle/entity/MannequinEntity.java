package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.template.RandomTrainerSelector;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplateStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MannequinEntity extends AbstractPokemonTrainerEntity {
    private static final String TRAINER_NBT_KEY = "Trainer";

    private static final TrackedData<String> TEXTURE = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.STRING);

    private Identifier trainer;

    public MannequinEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);

        this.trainer = TrainerTemplate.PLAYER_LEAF;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        this.getDataTracker().startTracking(TEXTURE, TrainerTexture.LEAF.toString());
    }

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
            return TrainerTexture.LEAF;
        }
    }

    @Override
    public Identifier getTexture() {
        return Identifier.tryParse(getDataTracker().get(TEXTURE));
    }

    @Override
    public void onPlayerVictory() {

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
