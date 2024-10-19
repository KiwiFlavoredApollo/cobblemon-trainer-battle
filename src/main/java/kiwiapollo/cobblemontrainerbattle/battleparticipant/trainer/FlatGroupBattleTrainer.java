package kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.UUID;

public class FlatGroupBattleTrainer implements TrainerBattleParticipant {
    private final TrainerBattleParticipant trainer;
    private final BattleCondition condition;

    public FlatGroupBattleTrainer(Identifier identifier, ServerPlayerEntity player, BattleCondition groupBattleCondition, int level) {
        this.trainer = new FlatBattleTrainer(identifier, player, level);
        this.condition = new BattleCondition();
    }

    @Override
    public Identifier getIdentifier() {
        return trainer.getIdentifier();
    }

    @Override
    public BattleAI getBattleAI() {
        return trainer.getBattleAI();
    }

    @Override
    public BattleCondition getBattleCondition() {
        return condition;
    }

    @Override
    public String getName() {
        return trainer.getName();
    }

    @Override
    public UUID getUuid() {
        return trainer.getUuid();
    }

    @Override
    public PartyStore getParty() {
        return trainer.getParty();
    }

    @Override
    public void setParty(PartyStore party) {
        trainer.setParty(party);
    }

    @Override
    public List<BattlePokemon> getBattleTeam() {
        return trainer.getBattleTeam();
    }

    @Override
    public BattleActor createBattleActor() {
        return trainer.createBattleActor();
    }

    @Override
    public void onVictory() {

    }

    @Override
    public void onDefeat() {

    }
}
