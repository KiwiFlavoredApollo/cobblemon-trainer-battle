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
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
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
import net.minecraft.world.World;

import java.util.*;

public class TrainerEntity extends PathAwareEntity {
    public static final int FLEE_DISTANCE = 20;

    private String trainer;
    private Identifier texture;
    private TrainerBattle trainerBattle;

    public TrainerEntity(EntityType<? extends PathAwareEntity> type, World world, String trainer, Identifier texture) {
        super(type, world);

        this.trainer = trainer;
        this.texture = texture;
        this.trainerBattle = null;
    }

    public TrainerEntity(EntityType<? extends PathAwareEntity> type, World world, TrainerEntityPreset preset) {
        this(type, world, preset.trainer(), preset.texture());
    }

    public void synchronizeClient(ServerWorld world) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(this.getId());
        buf.writeString(this.trainer);
        buf.writeIdentifier(this.texture);

        for (ServerPlayerEntity player : world.getPlayers()) {
            ServerPlayNetworking.send(player, TrainerEntityPackets.TRAINER_ENTITY_SYNC, buf);
        }
    }

    public Identifier getTexture() {
        return texture;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.goalSelector.add(2, new AttackGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
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
                    new PlayerBattleParticipantFactory(player, getLevelMode(trainer)).create(),
                    new TrainerBattleParticipantFactory(trainer).create(),
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

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (damageSource.getSource() instanceof ServerPlayerEntity player) {
            PlayerHistory history = PlayerHistoryStorage.getInstance().getOrCreate(player.getUuid());
            EntityRecord record = (EntityRecord) history.getOrCreate(trainer);
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

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public void setTexture(Identifier texture) {
        this.texture = texture;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("trainer", trainer);
        nbt.putString("texture", texture.toString());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        try {
            super.readCustomDataFromNbt(nbt);
            trainer = nbt.getString("trainer");
            texture = Identifier.tryParse(nbt.getString("texture"));

        } catch (NullPointerException e) {
            discard();
        }
    }
}
