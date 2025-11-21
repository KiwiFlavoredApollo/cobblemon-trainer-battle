package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant;

import com.cobblemon.mod.common.api.storage.party.PartyStore;
import net.minecraft.text.Text;

import java.util.UUID;

public interface BattleParticipant {
    Text getName();

    UUID getUuid();

    PartyStore getParty();
}
