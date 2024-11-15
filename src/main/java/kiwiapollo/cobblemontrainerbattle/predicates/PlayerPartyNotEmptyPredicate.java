package kiwiapollo.cobblemontrainerbattle.predicates;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Objects;

public class PlayerPartyNotEmptyPredicate implements MessagePredicate<PlayerBattleParticipant> {
    @Override
    public MutableText getMessage() {
        return Text.translatable("command.cobblemontrainerbattle.trainerbattle.empty_player_party");
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        return player.getParty().toGappyList().stream().anyMatch(Objects::nonNull);
    }
}
