package kiwiapollo.cobblemontrainerbattle.entities;

import com.cobblemon.mod.common.Cobblemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.NormalBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.EntityBackedTrainer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerIdentifierFactory;
import kiwiapollo.cobblemontrainerbattle.parser.PlayerHistory;
import kiwiapollo.cobblemontrainerbattle.parser.PlayerHistoryRegistry;
import kiwiapollo.cobblemontrainerbattle.parser.ProfileRegistries;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.StandardTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerProfile;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.exception.BusyWithPokemonBattleException;
import kiwiapollo.cobblemontrainerbattle.postbattle.PostBattleActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.postbattle.BattleResultHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
    private static final String TEXTURE_PARENTS = "textures/entity/trainer/slim/";
    private static final List<String> TEXTURE_FILES = List.of(
            "red_piikapiika.png",
            "green_piikapiika.png",
            "leaf_piikapiika.png",
            "alola_leaf_piikapiika.png",
            "silver_piikapiika.png",
            "black_hilbert_piikapiika.png",
            "white_hilda_piikapiika.png",

            "blacksmith_roxie_idkgraceorsmth.png",
            "cherry_blossom_garden_selene_idkgraceorsmth.png",
            "diner_waitress_mia_idkgraceorsmth.png"
    );
    private static final List<Identifier> TEXTURES = TEXTURE_FILES.stream()
            .map(file -> TEXTURE_PARENTS + file)
            .map(path -> Identifier.of(CobblemonTrainerBattle.NAMESPACE, path)).toList();

    public static final int FLEE_DISTANCE = 20;

    private Identifier trainer;
    private Identifier texture;
    private TrainerBattle trainerBattle;

    public TrainerEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);

        this.trainer = getRandomTrainer();
        this.texture = getRandomTexture();
        this.trainerBattle = null;
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

    private Identifier getRandomTrainer() {
        try {
            return new RandomTrainerIdentifierFactory().createSpawningAllowed();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
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
            BattleResultHandler battleResultHandler = new PostBattleActionSetHandler((ServerPlayerEntity) player, trainerProfile.onVictory(), trainerProfile.onDefeat());

            TrainerBattle trainerBattle = new StandardTrainerBattle(
                    playerBattleParticipant,
                    trainerBattleParticipant,
                    battleResultHandler
            );
            trainerBattle.start();

            CobblemonTrainerBattle.trainerBattleRegistry.put(player.getUuid(), trainerBattle);
            this.trainerBattle = trainerBattle;

        } catch (BattleStartException ignored) {
            this.setAiDisabled(false);
        }

        CobblemonTrainerBattle.INTERACT_TRAINER_CRITERION.trigger((ServerPlayerEntity) player, new ItemStack(Items.AIR), this);

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
            addPlayerKillRecord(player);
            CobblemonTrainerBattle.KILL_TRAINER_CRITERION.trigger(player, this, damageSource);
        }

        if(isPokemonBattleExist()) {
            UUID battleId = trainerBattle.getBattleId();
            Cobblemon.INSTANCE.getBattleRegistry().getBattle(battleId).end();
        }

        super.onDeath(damageSource);
    }

    public void onDefeat() {
        dropDefeatLoot();
        discard();
    }

    private void dropDefeatLoot() {
        Identifier identifier = this.getLootTable();
        LootTable lootTable = this.getWorld().getServer().getLootManager().getLootTable(identifier);

        LootContextParameterSet.Builder builder = (new LootContextParameterSet.Builder((ServerWorld)this.getWorld()))
                .add(LootContextParameters.THIS_ENTITY, this)
                .add(LootContextParameters.ORIGIN, this.getPos())
                .add(LootContextParameters.DAMAGE_SOURCE, getWorld().getDamageSources().generic());

        LootContextParameterSet lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
        lootTable.generateLoot(lootContextParameterSet, this.getLootTableSeed(), this::dropStack);
    }

    private void addPlayerKillRecord(ServerPlayerEntity player) {
        if (!PlayerHistoryRegistry.containsKey(player.getUuid())) {
            PlayerHistoryRegistry.put(player.getUuid(), new PlayerHistory());
        }

        PlayerHistoryRegistry.get(player.getUuid()).addPlayerKill(trainer);
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
