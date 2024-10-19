package kiwiapollo.cobblemontrainerbattle.entities;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactor.EntityBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.*;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerIdentifierFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.SafetyCheckedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.TrainerProfile;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.exception.BusyWithPokemonBattleException;
import kiwiapollo.cobblemontrainerbattle.resulthandler.ResultActionHandler;
import kiwiapollo.cobblemontrainerbattle.resulthandler.ResultHandler;
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
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.*;
import java.util.stream.StreamSupport;

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
            return new RandomTrainerIdentifierFactory().create();
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
            TrainerBattleParticipant trainerBattleParticipant = new EntityBackedNormalBattleTrainer(trainer, this, (ServerPlayerEntity) player);

            TrainerProfile trainerProfile = CobblemonTrainerBattle.trainerProfileRegistry.get(trainer);
            ResultHandler resultHandler = new ResultActionHandler((ServerPlayerEntity) player, trainerProfile.onVictory(), trainerProfile.onDefeat());

            TrainerBattle trainerBattle = new SafetyCheckedTrainerBattle(
                    playerBattleParticipant,
                    trainerBattleParticipant,
                    resultHandler
            );
            trainerBattle.start();

            CobblemonTrainerBattle.trainerBattleRegistry.put(player.getUuid(), trainerBattle);
            this.trainerBattle = trainerBattle;

        } catch (BattleStartException ignored) {
            this.setAiDisabled(false);
        }

        return super.interactMob(player, hand);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        try {
            assertNotBusyWithPokemonBattle();

            boolean isDamaged = super.damage(source, amount);
            boolean isLivingEntityAttacker = source.getAttacker() instanceof LivingEntity;

            if (isDamaged && isLivingEntityAttacker) {
                this.setTarget((LivingEntity) source.getAttacker());
            }

            return isDamaged;

        } catch (BusyWithPokemonBattleException e) {
            return false;
        }
    }

    private void assertNotBusyWithPokemonBattle() throws BusyWithPokemonBattleException {
        try {
            getPokemonBattle();
            throw new BusyWithPokemonBattleException();

        } catch (NoSuchElementException ignored) {

        }
    }

    private PokemonBattle getPokemonBattle() {
        try {
            UUID battleId = trainerBattle.getBattleId();
            return Objects.requireNonNull(Cobblemon.INSTANCE.getBattleRegistry().getBattle(battleId));

        } catch (NullPointerException e) {
            throw new NoSuchElementException();
        }
    }

    private List<BattleActor> getBattleActorsOf(PokemonBattle pokemonBattle) {
        return StreamSupport.stream(pokemonBattle.getActors().spliterator(), false).toList();
    }

    private boolean isEntityOf(BattleActor battleActor) {
        try {
            return ((EntityBackedTrainerBattleActor) battleActor).getEntity().equals(this);

        } catch (NullPointerException | ClassCastException e) {
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
        try {
            getPokemonBattle().end();
            super.onDeath(damageSource);

        } catch (NoSuchElementException e) {
            super.onDeath(damageSource);
        }
    }

    public void setTrainer(Identifier trainer) {
        this.trainer = trainer;
    }

    public void setTexture(Identifier texture) {
        this.texture = texture;
    }
}
