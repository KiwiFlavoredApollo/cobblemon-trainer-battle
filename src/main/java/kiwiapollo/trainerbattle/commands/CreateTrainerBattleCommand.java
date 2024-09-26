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
import kiwiapollo.trainerbattle.exceptions.NotExecutedByPlayerException;
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
            assertExecutedByPlayer(context);
            assertNotEmptyPlayerParty(context.getSource().getPlayer());
            assertNotAllFaintPokemon(context.getSource().getPlayer());

            PlayerPartyStore playerPartyStore =
                    Cobblemon.INSTANCE.getStorage().getParty(context.getSource().getPlayer());
            PlayerBattleActor playerBattleActor = new PlayerBattleActor(
                    context.getSource().getPlayer().getUuid(),
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

            return Command.SINGLE_SUCCESS;

        } catch (NotExecutedByPlayerException e) {
            return 0;

        } catch (InvalidPlayerPartyException e) {
            return 0;
        }
    }

    private List<? extends BattlePokemon> getTrainerPartyBattleTeam() {
        Pokemon pikachu = PokemonSpecies.INSTANCE.getByIdentifier(
                Identifier.of("cobblemon", "pikachu")
        ).create(20);

        return List.of(
                new BattlePokemon(pikachu, pikachu, pokemonEntity -> Unit.INSTANCE)
        );
    }

    private List<? extends BattlePokemon> getPlayerPartyBattleTeam(PlayerPartyStore playerPartyStore) {
        Pokemon firstNotFaintedPokemon = playerPartyStore.toGappyList().stream().filter(
                pokemon -> !pokemon.isFainted()
        ).findFirst().get();

        List<BattlePokemon> playerPartyBattleTeam = playerPartyStore.toBattleTeam(
                true,
                true,
                firstNotFaintedPokemon.getUuid()
        );

        for (BattlePokemon pokemon : playerPartyBattleTeam) {
            pokemon.getEffectedPokemon().heal();
            pokemon.getEffectedPokemon().setLevel(20);
        };

        return playerPartyBattleTeam;
    }

    private void assertExecutedByPlayer(CommandContext<ServerCommandSource> context) throws NotExecutedByPlayerException {
        if (!context.getSource().isExecutedByPlayer()) {
            throw new NotExecutedByPlayerException();
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
