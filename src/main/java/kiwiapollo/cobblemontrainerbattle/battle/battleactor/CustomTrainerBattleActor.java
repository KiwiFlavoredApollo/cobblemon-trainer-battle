package kiwiapollo.cobblemontrainerbattle.battle.battleactor;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.api.battles.model.actor.EntityBackedBattleActor;
import com.cobblemon.mod.common.api.battles.model.actor.FleeableBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.gamerule.CustomGameRule;
import kotlin.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomTrainerBattleActor extends AIBattleActor implements EntityBackedBattleActor<LivingEntity>, FleeableBattleActor, BattleResultHandler {
    private final Text name;
    private final LivingEntity entity;
    private final ServerWorld world;
    private final Vec3d position;
    private final Runnable onPlayerVictory;
    private final Runnable onPlayerDefeat;

    public CustomTrainerBattleActor(
            Text name,
            List<BattlePokemon> pokemon,
            BattleAI battleAI,
            LivingEntity entity,
            Runnable onPlayerVictory,
            Runnable onPlayerDefeat
    ) {
        super(entity.getUuid(), pokemon, battleAI);
        this.name = name;
        this.entity = entity;
        this.world = (ServerWorld) entity.getWorld();
        this.position = entity.getPos();
        this.onPlayerVictory = onPlayerVictory;
        this.onPlayerDefeat = onPlayerDefeat;
    }

    @Override
    public LivingEntity getEntity() {
        return this.entity;
    }

    @NotNull
    @Override
    public ActorType getType() {
        return ActorType.NPC;
    }

    @NotNull
    @Override
    public MutableText getName() {
        return this.name.copy();
    }

    @NotNull
    @Override
    public MutableText nameOwned(@NotNull String s) {
        return Text.literal(s).append(getName());
    }

    @Override
    public float getFleeDistance() {
        return world.getGameRules().getInt(CustomGameRule.TRAINER_FLEE_DISTANCE_IN_BLOCKS);
    }

    @Nullable
    @Override
    public Pair<ServerWorld, Vec3d> getWorldAndPosition() {
        return new Pair<>(world, position);
    }

    @Override
    public void onPlayerVictory() {
        onPlayerVictory.run();
    }

    @Override
    public void onPlayerDefeat() {
        onPlayerDefeat.run();
    }
}
