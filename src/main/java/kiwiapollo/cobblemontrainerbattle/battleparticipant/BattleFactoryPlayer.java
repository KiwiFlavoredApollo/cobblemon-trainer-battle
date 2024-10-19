package kiwiapollo.cobblemontrainerbattle.battleparticipant;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.battleactor.DisposableBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.battlefactory.RandomPartyFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BattleFactoryPlayer implements PlayerBattleParticipant {
    private final ServerPlayerEntity player;
    private PartyStore party;

    public BattleFactoryPlayer(ServerPlayerEntity player) {
        this.player = player;
        this.party = RandomPartyFactory.create(player);
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
        return party.toGappyList().stream().filter(Objects::nonNull).map(DisposableBattlePokemonFactory::create).toList();
    }

    @Override
    public BattleActor createBattleActor() {
        return new PlayerBattleActor(
                getUuid(),
                getBattleTeam()
        );
    }

    @Override
    public void onVictory() {

    }

    @Override
    public void onDefeat() {

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
