package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.api.battles.model.actor.EntityBackedBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class EntityBackedTrainerBattleActor extends AIBattleActor implements EntityBackedBattleActor<TrainerEntity> {
    private final String trainerName;
    private final TrainerEntity trainerEntity;

    public EntityBackedTrainerBattleActor(
            String trainerName,
            UUID uuid,
            List<BattlePokemon> pokemonList,
            BattleAI artificialDecider,
            TrainerEntity trainerEntity) {
        super(uuid, pokemonList, artificialDecider);
        this.trainerName = trainerName;
        this.trainerEntity = trainerEntity;
    }

    @Override
    public TrainerEntity getEntity() {
        return this.trainerEntity;
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
}
