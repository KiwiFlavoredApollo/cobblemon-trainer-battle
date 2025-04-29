package kiwiapollo.cobblemontrainerbattle.entity;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.status.PersistentStatus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HostileTrainerEntity extends TrainerEntity {
    public HostileTrainerEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public boolean tryAttack(Entity target) {
        applyRandomPersistentStatus((ServerPlayerEntity) target);
        return super.tryAttack(target);
    }

    private void applyRandomPersistentStatus(ServerPlayerEntity player) {
        try {
            PartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player.getUuid());
            List<Pokemon> random = new ArrayList<>(party.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .filter(pokemon -> !pokemon.isFainted())
                    .filter(pokemon -> !hasPersistentStatus(pokemon)).toList());
            Collections.shuffle(random);
            Pokemon pokemon = random.get(0);
            PersistentStatus status = new RandomPersistentStatusFactory().create();
            pokemon.applyStatus(status);

            player.sendMessage(Text.translatable(status.getApplyMessage(), pokemon.getDisplayName()).formatted(Formatting.RED));

        } catch (ClassCastException | NullPointerException | IndexOutOfBoundsException | NoPokemonStoreException ignored) {

        }
    }

    private boolean hasPersistentStatus(Pokemon pokemon) {
        try {
            return !pokemon.getStatus().isExpired();

        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public void onPlayerVictory() {
        dropDefeatedInBattleLoot();
        discard();
    }

    @Override
    public void onPlayerDefeat() {
        super.onPlayerDefeat();

        ServerPlayerEntity player = getTrainerBattle().getPlayer().getPlayerEntity();
        setRandomPartyPokemonFaint(player);
    }

    private void setRandomPartyPokemonFaint(ServerPlayerEntity player) {
        try {
            PartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player.getUuid());
            List<Pokemon> random = new ArrayList<>(party.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .filter(pokemon -> !pokemon.isFainted()).toList());
            Collections.shuffle(random);
            Pokemon pokemon = random.get(0);
            pokemon.setCurrentHealth(0);
            player.sendMessage(Text.translatable("cobblemon.battle.fainted", pokemon.getDisplayName()).formatted(Formatting.RED));

        } catch (NullPointerException | NoPokemonStoreException ignored) {

        }
    }
}
