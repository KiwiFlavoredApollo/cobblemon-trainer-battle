package kiwiapollo.cobblemontrainerbattle.battleparticipant;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;

import java.util.List;
import java.util.UUID;

public interface BattleParticipant {
    String getName();

    UUID getUuid();

    PartyStore getParty();

    void setParty(PartyStore party);

    List<BattlePokemon> getBattleTeam();

    BattleActor createBattleActor();

    void onVictory();

    void onDefeat();
}
