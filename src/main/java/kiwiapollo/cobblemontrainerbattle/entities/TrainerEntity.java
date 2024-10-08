package kiwiapollo.cobblemontrainerbattle.entities;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.EntityBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.exceptions.BusyWithPokemonBattleException;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.SpecificTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.StreamSupport;

public class TrainerEntity extends PathAwareEntity {
    private static final List<Identifier> TEXTURES = List.of(
            Identifier.of("minecraft", "textures/entity/player/wide/steve.png"),
            Identifier.of("minecraft", "textures/entity/player/wide/alex.png"),
            Identifier.of("minecraft", "textures/entity/player/wide/zuri.png")
    );

    private String trainerResourcePath;
    private Identifier texture;

    public TrainerEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);

        this.trainerResourcePath = getRandomTrainerResourcePath();
        this.texture = getRandomTexture();
    }

    public void synchronizeClient(ServerWorld world) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(this.getId());
        buf.writeString(this.trainerResourcePath);
        buf.writeIdentifier(this.texture);

        for (ServerPlayerEntity player : world.getPlayers()) {
            ServerPlayNetworking.send(player, TrainerEntityPackets.TRAINER_ENTITY_SYNC, buf);
        }
    }

    private String getRandomTrainerResourcePath() {
        try {
            List<String> trainerFilePaths = new ArrayList<>(
                    CobblemonTrainerBattle.trainerFiles.keySet().stream().toList());
            Collections.shuffle(trainerFilePaths);
            return trainerFilePaths.get(0);

        } catch (IndexOutOfBoundsException e) {
            return "";
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
            return EntityBackedTrainerBattle.trainerBattles.values().stream()
                    .filter(pokemonBattle -> getBattleActorsOf(pokemonBattle).stream().anyMatch(this::isEntityOf))
                    .findFirst().get();

        } catch (NullPointerException | NoSuchElementException e) {
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

    public void setTrainerResourcePath(String trainerResourcePath) {
        this.trainerResourcePath = trainerResourcePath;
    }

    public void setTexture(Identifier texture) {
        this.texture = texture;
    }
}
