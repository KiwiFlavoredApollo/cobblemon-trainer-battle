package kiwiapollo.cobblemontrainerbattle.battleparticipant;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NormalBattlePlayer implements PlayerBattleParticipant {
    private final ServerPlayerEntity player;
    private PlayerPartyStore party;

    public NormalBattlePlayer(ServerPlayerEntity player) {
        this.player = player;
        this.party = Cobblemon.INSTANCE.getStorage().getParty(player);
    }

    public UUID getUuid() {
        return player.getUuid();
    }

    @Override
    public PlayerPartyStore getParty() {
        return party;
    }

    @Override
    public void setParty(PartyStore party) {
        this.party = (PlayerPartyStore) party;
    }

    public ServerPlayerEntity getPlayerEntity() {
        return player;
    }

    @Override
    public String getName() {
        return player.getGameProfile().getName();
    }

    public List<BattlePokemon> getBattleTeam() {
        UUID leadingPokemon = party.toGappyList().stream()
                .filter(Objects::nonNull)
                .filter(pokemon -> !pokemon.isFainted())
                .findFirst().get().getUuid();

        return party.toBattleTeam(false, false, leadingPokemon);
    }

    @Override
    public void sendInfoMessage(MutableText message) {
        this.player.sendMessage(message.formatted(Formatting.WHITE));
    }

    @Override
    public void sendErrorMessage(MutableText message) {
        this.player.sendMessage(message.formatted(Formatting.RED));
    }
}
