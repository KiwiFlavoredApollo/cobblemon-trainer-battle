package kiwiapollo.trainerbattle.battlefrontier;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.PokemonStats;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.trainerbattle.TrainerBattle;
import kiwiapollo.trainerbattle.exceptions.PokemonNotExistException;
import kiwiapollo.trainerbattle.exceptions.StartingPokemonsNotSelectedException;
import kiwiapollo.trainerbattle.exceptions.ValidBattleFrontierSessionExistException;
import kiwiapollo.trainerbattle.exceptions.ValidBattleFrontierSessionNotExistException;
import kiwiapollo.trainerbattle.battleactors.RandomPlayerBattleActorFactory;
import kiwiapollo.trainerbattle.battleactors.RandomTrainerBattleActorFactory;
import kiwiapollo.trainerbattle.common.Trainer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class BattleFrontier {
    public static Map<UUID, BattleFrontierSession> SESSIONS = new HashMap<>();

    public static void start(CommandContext<ServerCommandSource> context) {
        try {
            assertNotExistValidSession(context.getSource().getPlayer());

            BattleFrontier.SESSIONS.put(context.getSource().getPlayer().getUuid(), new BattleFrontierSession());

            context.getSource().getPlayer().sendMessage(Text.literal("Battle Frontier session is created"));
            context.getSource().getPlayer().sendMessage(Text.literal("Please select starting Pokemons"));

        } catch (ValidBattleFrontierSessionExistException e) {
            context.getSource().getPlayer().sendMessage(Text.literal("Battle Frontier session already exists"));
            TrainerBattle.LOGGER.error(String.format("%s : Valid Battle Frontier session exists",
                    context.getSource().getPlayer().getGameProfile().getName()));
        }
    }

    public static void stop(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());

            BattleFrontier.SESSIONS.remove(context.getSource().getPlayer().getUuid());

            context.getSource().getPlayer().sendMessage(Text.literal("Battle Frontier session is removed"));

        } catch (ValidBattleFrontierSessionNotExistException e) {
            TrainerBattle.LOGGER.error(String.format("%s : Valid Battle Frontier session does not exists",
                    context.getSource().getPlayer().getGameProfile().getName()));
        }
    }

    public static void battle(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertSelectedStartingPokemons(context.getSource().getPlayer());

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new RandomPlayerBattleActorFactory().create(context.getSource().getPlayer())),
                    new BattleSide(new RandomTrainerBattleActorFactory().create(30)),
                    false
            );

            context.getSource().getPlayer().sendMessage(Text.literal("Battle Frontier battle started"));

        } catch (ValidBattleFrontierSessionNotExistException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal("You must create Battle Frontier session first"));
            TrainerBattle.LOGGER.error(String.format("%s : Valid Battle Frontier session does not exists",
                    context.getSource().getPlayer().getGameProfile().getName()));

        } catch (StartingPokemonsNotSelectedException e) {
            context.getSource().getPlayer().sendMessage(Text.literal("Please select starting Pokemons"));
            TrainerBattle.LOGGER.error(String.format("%s : Starting Pokemons are not selected",
                    context.getSource().getPlayer().getGameProfile().getName()));
        }
    }

    private static void assertSelectedStartingPokemons(ServerPlayerEntity player)
            throws StartingPokemonsNotSelectedException {
        BattleFrontierSession session = SESSIONS.get(player.getUuid());
        if (session.partyPokemons.isEmpty()) {
            throw new StartingPokemonsNotSelectedException();
        }
    }

    private static void assertExistValidSession(ServerPlayerEntity player)
            throws ValidBattleFrontierSessionNotExistException {
        if (!isExistValidSession(player)) {
            throw new ValidBattleFrontierSessionNotExistException();
        }
    }

    private static void assertNotExistValidSession(ServerPlayerEntity player)
            throws ValidBattleFrontierSessionExistException {
        if (isExistValidSession(player)) {
            throw new ValidBattleFrontierSessionExistException();
        }
    }

    private static boolean isExistValidSession(ServerPlayerEntity player) {
        if (!SESSIONS.containsKey(player.getUuid())) {
            return false;
        }

        BattleFrontierSession session = SESSIONS.get(player.getUuid());
        return Instant.now().isBefore(session.timestamp.plus(Duration.ofHours(24)));
    }

    public static void tradePokemon(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());

            String playerPokemon = StringArgumentType.getString(context, "playerpokemon");
            assertExistPlayerPokemon(context, playerPokemon);

            String trainerPokemon = StringArgumentType.getString(context, "trainerpokemon");
            assertExistTrainerPokemon(context, trainerPokemon);

        } catch (ValidBattleFrontierSessionNotExistException e) {
            throw new RuntimeException(e);

        } catch (PokemonNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    private static void assertExistTrainerPokemon(CommandContext<ServerCommandSource> context, String pokemonName)
            throws PokemonNotExistException {
        BattleFrontierSession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
        String lastDefeatedTrainer = session.defeatedTrainers.get(session.defeatedTrainers.size() - 1);
        List<Pokemon> trainerPokemons = new Trainer(lastDefeatedTrainer).getPokemons();
        List<String> trainerPokemonNames = trainerPokemons.stream()
                .map(Pokemon::getSpecies)
                .map(Species::getName).toList();

        if (!trainerPokemonNames.contains(pokemonName)) {
            throw new PokemonNotExistException();
        };
    }

    private static void assertExistPlayerPokemon(CommandContext<ServerCommandSource> context, String pokemonName)
            throws PokemonNotExistException {
        BattleFrontierSession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
        List<String> partyPokemonNames = session.partyPokemons.stream()
                .map(Pokemon::getSpecies)
                .map(Species::getName).toList();

        if (!partyPokemonNames.contains(pokemonName)) {
            throw new PokemonNotExistException();
        };
    }

    public static void showTradeablePokemons(CommandContext<ServerCommandSource> context) {

    }

    public static void showPartyPokemons(CommandContext<ServerCommandSource> context) {
        printPokemons(context, SESSIONS.get(context.getSource().getPlayer().getUuid()).partyPokemons);
    }

    public static void showStartingPokemons(CommandContext<ServerCommandSource> context) {
        printPokemons(context, SESSIONS.get(context.getSource().getPlayer().getUuid()).startingPokemons);
    }

    private static void printPokemons(CommandContext<ServerCommandSource> context, List<Pokemon> pokemons) {
        pokemons.forEach(pokemon -> {
            context.getSource().getPlayer().sendMessage(pokemon.getDisplayName());
            context.getSource().getPlayer().sendMessage(
                    Text.literal("Ability" + pokemon.getAbility().getDisplayName()));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("Nature " + pokemon.getNature().getDisplayName()));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("MoveSet " + getPokemonMovesetReport(pokemon.getMoveSet())));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("EVs " + getPokemonStatsReport(pokemon.getEvs())));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("IVs " + getPokemonStatsReport(pokemon.getIvs())));
        });
    }

    private static String getPokemonMovesetReport(MoveSet moveset) {
        return switch (moveset.getMoves().size()) {
            case 1 -> String.format("%s", moveset.getMoves().toArray());
            case 2 -> String.format("%s / %s", moveset.getMoves().toArray());
            case 3 -> String.format("%s / %s / %s", moveset.getMoves().toArray());
            case 4 -> String.format("%s / %s / %s / %s", moveset.getMoves().toArray());
            default -> "";
        };
    }

    private static String getPokemonStatsReport(PokemonStats stats) {
        return String.format("HP %d / ATK %d / DEF %d / SPA %d / SPD %d / SPE %d",
                stats.get(Stats.HP), stats.get(Stats.ATTACK), stats.get(Stats.DEFENCE),
                stats.get(Stats.SPECIAL_ATTACK), stats.get(Stats.SPECIAL_DEFENCE), stats.get(Stats.SPEED));
    };

    public static void rerollStartingPokemons(CommandContext<ServerCommandSource> context) {
        BattleFrontierSession session = SESSIONS.get(context.getSource().getPlayer().getUuid());

        if (!session.partyPokemons.isEmpty()) return;

        session.startingPokemons = new StartingPokemonsFactory().create();
        session.partyPokemons = session.startingPokemons;

        context.getSource().getPlayer().sendMessage(Text.literal("Rerolled starting Pokemons"));
    }

    public static void confirmStartingPokemons(CommandContext<ServerCommandSource> context) {
        BattleFrontierSession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
        session.partyPokemons = session.startingPokemons;

        context.getSource().getPlayer().sendMessage(Text.literal("Confirmed starting Pokemons"));
    }
}
