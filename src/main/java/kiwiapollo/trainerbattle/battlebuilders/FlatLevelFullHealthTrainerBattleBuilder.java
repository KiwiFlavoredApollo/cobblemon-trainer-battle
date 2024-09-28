package kiwiapollo.trainerbattle.battlebuilders;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.trainerbattle.TrainerBattle;
import kiwiapollo.trainerbattle.battleactors.FlatLevelFullHealthPlayerBattleActorFactory;
import kiwiapollo.trainerbattle.battleactors.RandomTrainerBattleActorFactory;
import kiwiapollo.trainerbattle.exceptions.EmptyPlayerPartyException;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;

public class FlatLevelFullHealthTrainerBattleBuilder {
    public void build(ServerPlayerEntity player) {
        try {
            assertNotEmptyPlayerParty(player);

            Cobblemon.INSTANCE.getStorage().getParty(player).forEach(Pokemon::recall);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new FlatLevelFullHealthPlayerBattleActorFactory().create(player)),
                    new BattleSide(new RandomTrainerBattleActorFactory().create(100)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                TrainerBattle.TRAINER_BATTLES.add(pokemonBattle);
                return Unit.INSTANCE;
            });

        } catch (EmptyPlayerPartyException e) {
            TrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
            TrainerBattle.LOGGER.error(String.format("%s has no Pokemon", player.getGameProfile().getName()));
        }
    }

    private void assertNotEmptyPlayerParty(ServerPlayerEntity player) throws EmptyPlayerPartyException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        if (playerPartyStore.toGappyList().isEmpty()) {
            throw new EmptyPlayerPartyException();
        }
    }
}
