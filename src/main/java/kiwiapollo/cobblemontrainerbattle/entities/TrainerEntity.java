package kiwiapollo.cobblemontrainerbattle.entities;

import com.cobblemon.mod.common.Cobblemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.NormalBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.EntityBackedTrainer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.parser.PlayerHistoryRegistry;
import kiwiapollo.cobblemontrainerbattle.parser.ProfileRegistries;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.StandardTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattleRegistry;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerProfile;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.exception.BusyWithPokemonBattleException;
import kiwiapollo.cobblemontrainerbattle.postbattle.PostBattleActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.postbattle.BattleResultHandler;
import kiwiapollo.cobblemontrainerbattle.trainerpreset.TrainerEntityPreset;
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

    private Identifier trainer;
    private Identifier texture;
    private TrainerBattle trainerBattle;

    public TrainerEntity(EntityType<? extends PathAwareEntity> type, World world, Identifier trainer, Identifier texture) {
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
        buf.writeIdentifier(this.trainer);
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

        try {
            this.setVelocity(0, 0, 0);
            this.setAiDisabled(true);
            this.velocityDirty = true;

            PlayerBattleParticipant playerBattleParticipant = new NormalBattlePlayer((ServerPlayerEntity) player);
            TrainerBattleParticipant trainerBattleParticipant = new EntityBackedTrainer(trainer, this, (ServerPlayerEntity) player);

            TrainerProfile trainerProfile = ProfileRegistries.trainer.get(trainer);
            BattleResultHandler battleResultHandler = new PostBattleActionSetHandler(
                    (ServerPlayerEntity) player,
                    trainerProfile.onVictory(),
                    trainerProfile.onDefeat()
            );

            TrainerBattle trainerBattle = new StandardTrainerBattle(
                    playerBattleParticipant,
                    trainerBattleParticipant,
                    battleResultHandler
            );
            trainerBattle.start();

            TrainerBattleRegistry.put(player.getUuid(), trainerBattle);
            this.trainerBattle = trainerBattle;

        } catch (BattleStartException ignored) {
            this.setAiDisabled(false);
        }

        Criteria.PLAYER_INTERACTED_WITH_ENTITY.trigger((ServerPlayerEntity) player, player.getStackInHand(hand), this);

        return super.interactMob(player, hand);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        try {
            assertNotBusyWithPokemonBattle();

            boolean isDamaged = super.damage(source, amount);
            boolean isLivingEntityAttacker = source.getAttacker() instanceof LivingEntity;

            if (isDamaged && isLivingEntityAttacker && !source.isSourceCreativePlayer()) {
                this.setTarget((LivingEntity) source.getAttacker());
            }

            return isDamaged;

        } catch (BusyWithPokemonBattleException e) {
            return false;
        }
    }

    private void assertNotBusyWithPokemonBattle() throws BusyWithPokemonBattleException {
        if (isPokemonBattleExist()) {
            throw new BusyWithPokemonBattleException();
        }
    }

    private boolean isPokemonBattleExist() {
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
            PlayerHistoryRegistry.get(player.getUuid()).addPlayerKill(trainer);
            CustomCriteria.KILL_TRAINER_CRITERION.trigger(player);
        }

        if(isPokemonBattleExist()) {
            UUID battleId = trainerBattle.getBattleId();
            Cobblemon.INSTANCE.getBattleRegistry().getBattle(battleId).end();
        }

        super.onDeath(damageSource);
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {
        LootContextParameterSet.Builder builder = (new LootContextParameterSet.Builder((ServerWorld)this.getWorld()))
                .add(LootContextParameters.THIS_ENTITY, this)
                .add(LootContextParameters.ORIGIN, this.getPos())
                .add(LootContextParameters.DAMAGE_SOURCE, damageSource)
                .addOptional(LootContextParameters.KILLER_ENTITY, damageSource.getAttacker())
                .addOptional(LootContextParameters.DIRECT_KILLER_ENTITY, damageSource.getSource());

        if (causedByPlayer && this.attackingPlayer != null) {
            builder = builder.add(LootContextParameters.LAST_DAMAGE_PLAYER, this.attackingPlayer).luck(this.attackingPlayer.getLuck());
        }

        LootContextParameterSet lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
        getTrainerLootTable().generateLoot(lootContextParameterSet, this.getLootTableSeed(), this::dropStack);
    }

    private LootTable getTrainerLootTable() {
        try {
            return getCustomLootTable();

        } catch (IllegalStateException e) {
            return getDefaultLootTable();
        }
    }

    private LootTable getCustomLootTable() throws IllegalStateException {
        Identifier custom = Identifier.of(CobblemonTrainerBattle.MOD_ID, String.format("trainers/%s", trainer.getPath()));
        LootTable lootTable = this.getWorld().getServer().getLootManager().getLootTable(custom);

        if (lootTable.equals(LootTable.EMPTY)) {
            throw new IllegalStateException();
        }

        return lootTable;
    }

    private LootTable getDefaultLootTable() {
        Identifier defaults = Identifier.of(CobblemonTrainerBattle.MOD_ID, "trainers/defaults");
        return this.getWorld().getServer().getLootManager().getLootTable(defaults);
    }

    public void onDefeat() {
        dropDefeatLoot();
        discard();
    }

    private void dropDefeatLoot() {
        LootContextParameterSet.Builder builder = (new LootContextParameterSet.Builder((ServerWorld)this.getWorld()))
                .add(LootContextParameters.THIS_ENTITY, this)
                .add(LootContextParameters.ORIGIN, this.getPos())
                .add(LootContextParameters.DAMAGE_SOURCE, getWorld().getDamageSources().generic());

        LootContextParameterSet lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
        getTrainerLootTable().generateLoot(lootContextParameterSet, this.getLootTableSeed(), this::dropStack);
    }

    public void setTrainer(Identifier trainer) {
        this.trainer = trainer;
    }

    public void setTexture(Identifier texture) {
        this.texture = texture;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("trainer", trainer.toString());
        nbt.putString("texture", texture.toString());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        try {
            super.readCustomDataFromNbt(nbt);
            trainer = Objects.requireNonNull(Identifier.tryParse(nbt.getString("trainer")));
            texture = Objects.requireNonNull(Identifier.tryParse(nbt.getString("texture")));

        } catch (NullPointerException e) {
            discard();
        }
    }
}
