package kiwiapollo.trainerbattle.utilities;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.actor.TrainerBattleActor;
import com.cobblemon.mod.common.battles.ai.RandomBattleAI;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.trainerbattle.exceptions.EmptyPlayerPartyException;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.UUID;

public class FlatLevelFullHealthTrainerBattleBuilder {
    public void build(ServerPlayerEntity player) {
        try {
            assertNotEmptyPlayerParty(player);

            PlayerPartyStore playerPartyStore =
                    Cobblemon.INSTANCE.getStorage().getParty(player);
            PlayerBattleActor playerBattleActor = new PlayerBattleActor(
                    player.getUuid(),
                    getPlayerPartyBattleTeam(playerPartyStore)
            );

            TrainerBattleActor trainerBattleActor = new TrainerBattleActor(
                    "MyTrainer",
                    UUID.randomUUID(),
                    getTrainerPartyBattleTeam(),
                    new RandomBattleAI()
            );

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(playerBattleActor),
                    new BattleSide(trainerBattleActor),
                    false
            );

        } catch (EmptyPlayerPartyException e) {
            return;
        }
    }

    private List<? extends BattlePokemon> getTrainerPartyBattleTeam() {
        Pokemon pikachu = PokemonSpecies.INSTANCE.getByIdentifier(
                Identifier.of("cobblemon", "pikachu")
        ).create(100);

        return List.of(
                new BattlePokemon(pikachu, pikachu, pokemonEntity -> Unit.INSTANCE)
        );
    }

    private List<? extends BattlePokemon> getPlayerPartyBattleTeam(PlayerPartyStore playerPartyStore) {
        List<BattlePokemon> playerPartyBattleTeam = playerPartyStore.toBattleTeam(
                true,
                true,
                playerPartyStore.toGappyList().get(0).getUuid()
        );

        for (BattlePokemon pokemon : playerPartyBattleTeam) {
            pokemon.getEffectedPokemon().heal();
            pokemon.getEffectedPokemon().setLevel(100);
        };

        return playerPartyBattleTeam;
    }

    private void assertNotEmptyPlayerParty(ServerPlayerEntity player) throws EmptyPlayerPartyException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        if (playerPartyStore.toGappyList().isEmpty()) {
            throw new EmptyPlayerPartyException();
        }
    }
}
