package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.api.battles.model.actor.FleeableBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kotlin.Pair;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class VirtualTrainerBattleActor extends AIBattleActor implements FleeableBattleActor {
    private static int FLEE_DISTANCE = 20;
    private final String trainerName;
    private final ServerWorld battleStartingWorld;
    private final Vec3d battleStartingPosition;

    public VirtualTrainerBattleActor(
            String trainerName,
            @NotNull UUID gameId,
            @NotNull List<? extends BattlePokemon> pokemonList,
            @NotNull BattleAI battleAI,
            ServerPlayerEntity opposingPlayer) {
        super(gameId, pokemonList, battleAI);
        this.trainerName = trainerName;
        this.battleStartingWorld = opposingPlayer.getServerWorld();
        this.battleStartingPosition = opposingPlayer.getPos();
    }

    @NotNull
    @Override
    public ActorType getType() {
        return ActorType.NPC;
    }

    @NotNull
    @Override
    public MutableText getName() {
        return Text.literal(this.trainerName);
    }

    @NotNull
    @Override
    public MutableText nameOwned(@NotNull String s) {
        return Text.literal(s).append(getName());
    }

    @Override
    public float getFleeDistance() {
        return FLEE_DISTANCE;
    }

    @Nullable
    @Override
    public Pair<ServerWorld, Vec3d> getWorldAndPosition() {
        return new Pair<>(battleStartingWorld, battleStartingPosition);
    }
}
