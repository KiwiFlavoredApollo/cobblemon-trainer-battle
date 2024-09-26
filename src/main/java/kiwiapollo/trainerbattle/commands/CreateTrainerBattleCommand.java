package kiwiapollo.trainerbattle.commands;

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
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.trainerbattle.TrainerBattle;
import kiwiapollo.trainerbattle.exceptions.InvalidPlayerPartyException;
import kiwiapollo.trainerbattle.exceptions.NotRunByPlayerException;
import kotlin.Unit;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class CreateTrainerBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public CreateTrainerBattleCommand() {
        super("create");

        this.requires(new TrainerCommandPredicate(
                String.format("%s.%s", TrainerBattle.NAMESPACE, getLiteral())
        )).executes(this::createTrainerBattle);
    }

    public int createTrainerBattle(CommandContext<ServerCommandSource> context) {
        try {
            assertRunByPlayer(context);
            assertNotEmptyPlayerParty(context.getSource().getPlayer());
            assertNotAllFaintPokemon(context.getSource().getPlayer());

            PlayerPartyStore playerPartyStore =
                    Cobblemon.INSTANCE.getStorage().getParty(context.getSource().getPlayer());
            PlayerBattleActor playerBattleActor = new PlayerBattleActor(
                    context.getSource().getPlayer().getUuid(),
                    playerPartyStore.toBattleTeam(
                            true,
                            true,
                            playerPartyStore.toGappyList().stream().findFirst().get().getUuid()
                    )
            );

            Pokemon pokemon = PokemonSpecies.INSTANCE.getByIdentifier(
                    Identifier.of("cobblemon", "pikachu")
            ).create(10);
            TrainerBattleActor trainerBattleActor = new TrainerBattleActor(
                    "MyTrainer",
                    UUID.randomUUID(),
                    List.of(
                            new BattlePokemon(pokemon, pokemon, pokemonEntity -> Unit.INSTANCE)
                    ),
                    new RandomBattleAI()
            );

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(playerBattleActor),
                    new BattleSide(trainerBattleActor),
                    false
            );

            return Command.SINGLE_SUCCESS;

        } catch (NotRunByPlayerException e) {
            return 0;

        } catch (InvalidPlayerPartyException e) {
            return 0;
        }
    }

    private void assertRunByPlayer(CommandContext<ServerCommandSource> context) throws NotRunByPlayerException {
        if (!context.getSource().isExecutedByPlayer()) {
            throw new NotRunByPlayerException();
        }
    }

    private void assertNotEmptyPlayerParty(ServerPlayerEntity player) throws InvalidPlayerPartyException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        if (playerPartyStore.toGappyList().isEmpty()) {
            throw new InvalidPlayerPartyException();
        }
    }

    private void assertNotAllFaintPokemon(ServerPlayerEntity player) throws InvalidPlayerPartyException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream();
        if (pokemons.allMatch(Pokemon::isFainted)) {
            throw new InvalidPlayerPartyException();
        }
    }
}
