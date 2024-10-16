package kiwiapollo.cobblemontrainerbattle.battleparticipants;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FlatBattlePlayer implements PlayerBattleParticipant {
    private final ServerPlayerEntity player;
    private PartyStore party;

    public FlatBattlePlayer(ServerPlayerEntity player, int level) {
        this.player = player;
        this.party = cloneParty(player, level);
    }

    private static PartyStore cloneParty(ServerPlayerEntity player, int level) {
        PartyStore original = Cobblemon.INSTANCE.getStorage().getParty(player);
        PartyStore clone = new PartyStore(player.getUuid());

        for (Pokemon pokemon : original.toGappyList().stream().filter(Objects::nonNull).toList()) {
            clone.add(pokemon.clone(true, true));
        }

        clone.toGappyList().stream().filter(Objects::nonNull).forEach(Pokemon::heal);
        clone.toGappyList().stream().filter(Objects::nonNull).forEach(pokemon -> pokemon.setLevel(level));

        return clone;
    }

    public UUID getUuid() {
        return player.getUuid();
    }

    @Override
    public PartyStore getParty() {
        return party;
    }

    @Override
    public void setParty(PartyStore party) {
        this.party = party;
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
