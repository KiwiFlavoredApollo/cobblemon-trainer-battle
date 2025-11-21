package kiwiapollo.cobblemontrainerbattle.entity;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.status.PersistentStatus;
import com.cobblemon.mod.common.pokemon.status.statuses.persistent.*;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.EntityBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.NullTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.gamerule.CustomGameRule;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import kiwiapollo.cobblemontrainerbattle.global.history.EntityRecord;
import kiwiapollo.cobblemontrainerbattle.global.history.PlayerHistory;
import kiwiapollo.cobblemontrainerbattle.global.history.PlayerHistoryStorage;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplateStorage;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
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
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class TrainerEntity extends PathAwareEntity implements TrainerEntityBehavior {
    private static final String FALLBACK_TRAINER = "radicalred/player_red";
    private static final String FALLBACK_TEXTURE = TrainerTexture.RED.getIdentifier().toString();

    private static final TrackedData<String> TRAINER = DataTracker.registerData(TrainerEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> TEXTURE = DataTracker.registerData(TrainerEntity.class, TrackedDataHandlerRegistry.STRING);

    private static final String TRAINER_NBT_KEY = "Trainer";

    private TrainerBattle trainerBattle;

    public TrainerEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);

        this.trainerBattle = new NullTrainerBattle();
    }

    /**
     * If TrainerEntity is spawned by Mob Spawner, TRAINER and TEXTURE are not initialized.
     * EntityTag NBT of a Trainer Spawn Egg will not be passed to SpawnData NBT of a Mob Spawner.
     */
    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if (spawnReason.equals(SpawnReason.SPAWNER)) {
            setTrainer(new RandomSpawnableTrainerFactory(trainer -> true).create());
        }

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(TRAINER, FALLBACK_TRAINER);
        this.getDataTracker().startTracking(TEXTURE, FALLBACK_TEXTURE);
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
            if (hasTrainerBattle()) {
                return;
            }

            TrainerBattle trainerBattle = new EntityBackedTrainerBattle(
                    new PlayerBattleParticipantFactory(player, getLevelMode(toDefaultedIdentifier(getDataTracker().get(TRAINER)))).create(),
                    new TrainerBattleParticipantFactory(toDefaultedIdentifier(getDataTracker().get(TRAINER))).create(),
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

    private LevelMode getLevelMode(Identifier trainer) {
        return TrainerTemplateStorage.getInstance().get(trainer).getLevelMode();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (hasTrainerBattle()) {
            return false;
        }

        return super.damage(source, amount);
    }

    private boolean hasTrainerBattle() {
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
            EntityRecord record = (EntityRecord) history.getOrCreate(toDefaultedIdentifier(getDataTracker().get(TRAINER)));
            record.setKillCount(record.getKillCount() + 1);
            CustomCriteria.KILL_TRAINER_CRITERION.trigger(player);
        }

        if(hasTrainerBattle()) {
            UUID battleId = trainerBattle.getBattleId();
            Cobblemon.INSTANCE.getBattleRegistry().getBattle(battleId).end();
        }

        super.onDeath(damageSource);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString(TRAINER_NBT_KEY, getDataTracker().get(TRAINER));
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

    @Override
    public void setTrainer(Identifier trainer) {
        this.getDataTracker().set(TRAINER, trainer.toString());
        this.getDataTracker().set(TEXTURE, getTexture(trainer));
    }

    @Override
    public Identifier getTexture() {
        String texture = getDataTracker().get(TEXTURE);
        return Identifier.tryParse(Objects.requireNonNull(texture));
    }

    @Nullable
    private String getTexture(Identifier trainer) {
        try {
            return TrainerTemplateStorage.getInstance().get(trainer).getTexture().toString();

        } catch (NullPointerException e) {
            return FALLBACK_TEXTURE;
        }
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

    @Override
    public TrainerBattle getTrainerBattle() {
        UUID battleId = trainerBattle.getBattleId();
        Objects.requireNonNull(Cobblemon.INSTANCE.getBattleRegistry().getBattle(battleId));
        return trainerBattle;
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
    }

    private Identifier toDefaultedIdentifier(String string) {
        if (string.contains(String.valueOf(Identifier.NAMESPACE_SEPARATOR))) {
            return Identifier.tryParse(string);

        } else {
            return Identifier.of(CobblemonTrainerBattle.MOD_ID, string);
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (!(target instanceof ServerPlayerEntity player)) {
            return super.tryAttack(target);
        }

        if (isBusyWithPokemonBattle(player)) {
            return super.tryAttack(target);
        }

        if (!doTrainerApplyStatusCondition(player.getServer())) {
            return super.tryAttack(target);
        }

        applyRandomStatusConditionToRandomPokemon(player);
        return super.tryAttack(target);
    }

    private boolean isBusyWithPokemonBattle(ServerPlayerEntity player) {
        return Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) != null;
    }

    private boolean doTrainerApplyStatusCondition(MinecraftServer server) {
        return server.getGameRules().getBoolean(CustomGameRule.DO_TRAINER_APPLY_STATUS_CONDITION);
    }

    private void applyRandomStatusConditionToRandomPokemon(ServerPlayerEntity player) {
        try {
            Pokemon pokemon = selectRandomPokemon(player);
            PersistentStatus status = selectRandomStatus();
            pokemon.applyStatus(status);

            player.sendMessage(Text.translatable(status.getApplyMessage(), pokemon.getDisplayName()).formatted(Formatting.RED));

        } catch (NoPokemonStoreException | IndexOutOfBoundsException ignored) {

        }
    }

    private Pokemon selectRandomPokemon(ServerPlayerEntity player) throws NoPokemonStoreException {
        PartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player.getUuid());
        List<Pokemon> random = new ArrayList<>(party.toGappyList().stream().filter(this::canApplyStatusCondition).toList());
        Collections.shuffle(random);
        return random.get(0);
    }

    private boolean canApplyStatusCondition(Pokemon pokemon) {
        return !Objects.isNull(pokemon) && !isFainted(pokemon) && !hasStatusCondition(pokemon);
    }

    private boolean isFainted(Pokemon pokemon) {
        return pokemon.isFainted();
    }

    private boolean hasStatusCondition(Pokemon pokemon) {
        try {
            return !pokemon.getStatus().isExpired();

        } catch (NullPointerException e) {
            return false;
        }
    }

    private PersistentStatus selectRandomStatus() {
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
}
