package kiwiapollo.cobblemontrainerbattle.battlefactory;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.PokemonStats;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.BattleFactoryPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.BattleFactoryNameTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.commands.BattleFactoryCommand;
import kiwiapollo.cobblemontrainerbattle.common.InvalidBattleSessionState;
import kiwiapollo.cobblemontrainerbattle.common.InvalidPlayerState;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidBattleSessionStateException;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidPlayerStateException;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import kotlin.Unit;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class BattleFactory {
    public static final int LEVEL = 100;
    public static Map<UUID, BattleFactorySession> SESSIONS = new HashMap<>();

    public static int quickStart(CommandContext<ServerCommandSource> context) {
        try {
            assertNotExistValidSession(context.getSource().getPlayer());
            startSession(context);
            return startBattle(context);

        } catch (InvalidBattleSessionStateException e) {
            stopSession(context);
            startSession(context);
            return startBattle(context);
        }
    }

    public static int startSession(CommandContext<ServerCommandSource> context) {
        try {
            assertNotExistValidSession(context.getSource().getPlayer());

            List<Trainer> trainersToDefeat = new ArrayList<>();
            for (int i = 0; i < 21; i++) {
                trainersToDefeat.add(new BattleFactoryRandomTrainerFactory().create(context.getSource().getPlayer()));
            }

            BattleFactory.SESSIONS.put(context.getSource().getPlayer().getUuid(), new BattleFactorySession(trainersToDefeat));

            context.getSource().getPlayer().sendMessage(Text.literal("Battle Frontier session has started"));
            showPartyPokemons(context);
            CobblemonTrainerBattle.LOGGER.info(String.format("%s: Started Battle Frontier session",
                    context.getSource().getPlayer().getGameProfile().getName()));

            return Command.SINGLE_SUCCESS;

        } catch (InvalidBattleSessionStateException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal(getInvalidBattleSessionStateErrorMessage(e)).formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    public static int stopSession(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());

            BattleFactory.SESSIONS.remove(context.getSource().getPlayer().getUuid());

            context.getSource().getPlayer().sendMessage(Text.literal("Battle Frontier session has stopped"));
            CobblemonTrainerBattle.LOGGER.info(String.format("%s: Stopped Battle Frontier session",
                    context.getSource().getPlayer().getGameProfile().getName()));

            return Command.SINGLE_SUCCESS;

        } catch (InvalidBattleSessionStateException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal(getInvalidBattleSessionStateErrorMessage(e)).formatted(Formatting.RED));
            return -1;
        }
    }

    public static int startBattle(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertNotPlayerDefeated(context.getSource().getPlayer());
            assertNotPlayerBusyWithAnotherPokemonBattle(context.getSource().getPlayer());
            assertNotDefeatedAllTrainers(context.getSource().getPlayer());

            BattleFactorySession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
            Trainer trainer = session.trainersToDefeat.get(session.defeatedTrainerCount);
            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new BattleFactoryPlayerBattleActorFactory().create(context.getSource().getPlayer())),
                    new BattleSide(new BattleFactoryNameTrainerBattleActorFactory().create(trainer)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                CobblemonTrainerBattle.trainerBattles.put(context.getSource().getPlayer().getUuid(), pokemonBattle);
                UUID playerUuid = context.getSource().getPlayer().getUuid();
                BattleFactory.SESSIONS.get(playerUuid).battleUuid = pokemonBattle.getBattleId();

                context.getSource().getPlayer().sendMessage(
                        Text.literal("Battle Frontier Pokemon Battle has started"));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new BattleFactoryCommand().getLiteral(),
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (InvalidBattleSessionStateException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal(getInvalidBattleSessionStateErrorMessage(e)).formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return -1;

        } catch (InvalidPlayerStateException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal(getInvalidPlayerStateErrorMessage(e)).formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    public static int tradePokemons(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertNotPlayerDefeated(context.getSource().getPlayer());
            assertExistDefeatedTrainer(context.getSource().getPlayer());
            assertNotPlayerTradedPokemon(context.getSource().getPlayer());

            int playerslot = IntegerArgumentType.getInteger(context, "playerslot");
            int trainerslot = IntegerArgumentType.getInteger(context, "trainerslot");

            BattleFactorySession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
            Trainer lastDefeatedTrainer = session.trainersToDefeat.get(session.defeatedTrainerCount - 1);
            Pokemon trainerPokemon = lastDefeatedTrainer.pokemons.get(trainerslot - 1);
            Pokemon playerPokemon = session.partyPokemons.get(playerslot - 1);

            session.partyPokemons = new ArrayList<>(session.partyPokemons);
            session.partyPokemons.set(playerslot - 1, trainerPokemon.clone(true, true));
            session.isTradedPokemon = true;

            Text pokemonTradeMessage = Text.literal("Traded ")
                    .append(playerPokemon.getDisplayName())
                    .append(Text.literal(" for "))
                    .append(trainerPokemon.getDisplayName())
                    .formatted(Formatting.YELLOW);

            context.getSource().getPlayer().sendMessage(pokemonTradeMessage);
            CobblemonTrainerBattle.LOGGER.info(pokemonTradeMessage.getString());

            return Command.SINGLE_SUCCESS;

        } catch (InvalidBattleSessionStateException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal(getInvalidBattleSessionStateErrorMessage(e)).formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    public static int showTradeablePokemons(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertExistDefeatedTrainer(context.getSource().getPlayer());

            BattleFactorySession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
            Trainer lastDefeatedTrainer = session.trainersToDefeat.get(session.defeatedTrainerCount - 1);
            printPokemons(context, lastDefeatedTrainer.pokemons);

            return Command.SINGLE_SUCCESS;

        } catch (InvalidBattleSessionStateException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal(getInvalidBattleSessionStateErrorMessage(e)).formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    public static int showPartyPokemons(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            printPokemons(context, SESSIONS.get(context.getSource().getPlayer().getUuid()).partyPokemons);
            return Command.SINGLE_SUCCESS;

        } catch (InvalidBattleSessionStateException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal(getInvalidBattleSessionStateErrorMessage(e)).formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    public static int rerollPokemons(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertNotExistDefeatedTrainers(context.getSource().getPlayer());

            BattleFactorySession session = SESSIONS.get(context.getSource().getPlayer().getUuid());

            session.partyPokemons = List.of(
                    PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL),
                    PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL),
                    PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL)
            );
            context.getSource().getPlayer().sendMessage(Text.literal("Rerolled Pokemons"));
            showPartyPokemons(context);

            return Command.SINGLE_SUCCESS;

        } catch (InvalidBattleSessionStateException e) {
            if (e.getInvalidBattleSessionState().equals(InvalidBattleSessionState.ANY_TRAINER_DEFEATED)) {
                context.getSource().getPlayer().sendMessage(
                        Text.literal("You cannot reroll Pokemons after battling trainers").formatted(Formatting.RED));
                CobblemonTrainerBattle.LOGGER.error(e.getMessage());
                return -1;

            } else {
                context.getSource().getPlayer().sendMessage(
                        Text.literal(getInvalidBattleSessionStateErrorMessage(e)).formatted(Formatting.RED));
                CobblemonTrainerBattle.LOGGER.error(e.getMessage());
                return -1;
            }
        }
    }

    public static int showWinningStreak(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());

            BattleFactorySession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
            context.getSource().getPlayer().sendMessage(
                    Text.literal(String.format("Winning streak: %d", session.defeatedTrainerCount)));

            return Command.SINGLE_SUCCESS;

        } catch (InvalidBattleSessionStateException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal(getInvalidBattleSessionStateErrorMessage(e)).formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    private static void assertNotPlayerDefeated(ServerPlayerEntity player) throws InvalidBattleSessionStateException {
        BattleFactorySession session = SESSIONS.get(player.getUuid());
        if (session.isDefeated) {
            throw new InvalidBattleSessionStateException(
                    String.format("Player is defeated: %s", player.getGameProfile().getName()),
                    InvalidBattleSessionState.DEFEATED_TO_TRAINER
            );
        }
    }

    private static void assertNotDefeatedAllTrainers(ServerPlayerEntity player)
            throws InvalidBattleSessionStateException {
        if (isDefeatedAllTrainers(player)) {
            throw new InvalidBattleSessionStateException(
                    String.format("Player has defeated all trainers: %s", player.getGameProfile().getName()),
                    InvalidBattleSessionState.ALL_TRAINER_DEFEATED
            );
        };
    }

    private static boolean isDefeatedAllTrainers(ServerPlayerEntity player) {
        try {
            BattleFactorySession session = SESSIONS.get(player.getUuid());
            return session.defeatedTrainerCount == session.trainersToDefeat.size();

        } catch (NullPointerException | ClassCastException | IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    private static void assertExistValidSession(ServerPlayerEntity player)
            throws InvalidBattleSessionStateException {
        if (!isExistValidSession(player)) {
            throw new InvalidBattleSessionStateException(
                    String.format("Valid battle session does not exists: %s", player.getGameProfile().getName()),
                    InvalidBattleSessionState.SESSION_NOT_EXISTS
            );
        }
    }

    private static void assertNotExistValidSession(ServerPlayerEntity player)
            throws InvalidBattleSessionStateException {
        if (isExistValidSession(player)) {
            throw new InvalidBattleSessionStateException(
                    String.format("Valid battle session exists: %s", player.getGameProfile().getName()),
                    InvalidBattleSessionState.SESSION_EXISTS
            );
        }
    }

    private static boolean isExistValidSession(ServerPlayerEntity player) {
        if (!SESSIONS.containsKey(player.getUuid())) {
            return false;
        }

        BattleFactorySession session = SESSIONS.get(player.getUuid());
        return Instant.now().isBefore(session.timestamp.plus(Duration.ofHours(24)));
    }

    private static void assertNotPlayerTradedPokemon(ServerPlayerEntity player)
            throws InvalidBattleSessionStateException {
        if (SESSIONS.get(player.getUuid()).isTradedPokemon) {
            throw new InvalidBattleSessionStateException(
                    String.format("Player has already traded a Pokemon: %s", player.getGameProfile().getName()),
                    InvalidBattleSessionState.POKEMON_TRADED
            );
        }
    }

    private static void assertExistDefeatedTrainer(ServerPlayerEntity player)
            throws InvalidBattleSessionStateException {
        if (!isExistDefeatedTrainers(player)) {
            throw new InvalidBattleSessionStateException(
                    String.format("Player has no defeated trainers: %s", player.getGameProfile().getName()),
                    InvalidBattleSessionState.NO_TRAINER_DEFEATED
            );
        }
    }

    private static void assertNotExistDefeatedTrainers(ServerPlayerEntity player)
            throws InvalidBattleSessionStateException {
        if (isExistDefeatedTrainers(player)) {
            throw new InvalidBattleSessionStateException(
                    String.format("Player has defeated trainers: %s", player.getGameProfile().getName()),
                    InvalidBattleSessionState.ANY_TRAINER_DEFEATED
            );
        }
    }

    private static boolean isExistDefeatedTrainers(ServerPlayerEntity player) {
        return SESSIONS.get(player.getUuid()).defeatedTrainerCount != 0;
    }

    private static void printPokemons(CommandContext<ServerCommandSource> context, List<Pokemon> pokemons) {
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon pokemon = pokemons.get(i);

            context.getSource().getPlayer().sendMessage(
                    Text.literal("[" + (i + 1) + "] ").append(pokemon.getDisplayName()).formatted(Formatting.YELLOW));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("Ability ").append(Text.translatable(pokemon.getAbility().getDisplayName())));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("Nature ").append(Text.translatable(pokemon.getNature().getDisplayName())));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("MoveSet ").append(getPokemonMoveSetReport(pokemon.getMoveSet())));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("EVs ").append(Text.literal(getPokemonStatsReport(pokemon.getEvs()))));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("IVs ").append(Text.literal(getPokemonStatsReport(pokemon.getIvs()))));
        }
    }

    private static Text getPokemonMoveSetReport(MoveSet moveSet) {
        MutableText moveSetReport = Text.literal("");
        for (Move move : moveSet.getMoves()) {
            if (moveSetReport.equals(Text.literal(""))) {
                moveSetReport.append(move.getDisplayName());
            } else {
                moveSetReport.append(Text.literal(" / ")).append(move.getDisplayName());
            }
        }
        return moveSetReport;
    }

    private static String getPokemonStatsReport(PokemonStats stats) {
        return String.format("HP %d / ATK %d / DEF %d / SPA %d / SPD %d / SPE %d",
                stats.get(Stats.HP), stats.get(Stats.ATTACK), stats.get(Stats.DEFENCE),
                stats.get(Stats.SPECIAL_ATTACK), stats.get(Stats.SPECIAL_DEFENCE), stats.get(Stats.SPEED));
    };

    private static void assertNotPlayerBusyWithAnotherPokemonBattle(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        if (Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) != null) {
            throw new InvalidPlayerStateException(
                    String.format("Player is busy with another Pokemon battle: %s",
                            player.getGameProfile().getName()),
                    InvalidPlayerState.BUSY_WITH_ANOTHER_POKEMON_BATTLE
            );
        }
    }

    private static String getInvalidBattleSessionStateErrorMessage(InvalidBattleSessionStateException e) {
        if (e.getInvalidBattleSessionState().equals(InvalidBattleSessionState.SESSION_EXISTS)) {
            return "Active battle frontier session exist";
        }

        if (e.getInvalidBattleSessionState().equals(InvalidBattleSessionState.SESSION_NOT_EXISTS)) {
            return "Active battle frontier session does not exist";
        }

        if (e.getInvalidBattleSessionState().equals(InvalidBattleSessionState.DEFEATED_TO_TRAINER)) {
            return "You cannot continue battle frontier session due to being defeated";
        }

        if (e.getInvalidBattleSessionState().equals(InvalidBattleSessionState.ALL_TRAINER_DEFEATED)) {
            return "You have defeated all trainers";
        }

        if (e.getInvalidBattleSessionState().equals(InvalidBattleSessionState.ANY_TRAINER_DEFEATED)) {
            return "You have defeated trainers";
        }

        if (e.getInvalidBattleSessionState().equals(InvalidBattleSessionState.NO_TRAINER_DEFEATED)) {
            return "You have not defeated any trainers";
        }

        if (e.getInvalidBattleSessionState().equals(InvalidBattleSessionState.POKEMON_TRADED)) {
            return "You have already traded your Pokemon";
        }

        throw new RuntimeException(e);
    }

    private static String getInvalidPlayerStateErrorMessage(InvalidPlayerStateException e) {
        if (e.getInvalidPlayerState().equals(InvalidPlayerState.BUSY_WITH_ANOTHER_POKEMON_BATTLE)) {
            return "You cannot start trainer battle while on another";
        }

        throw new RuntimeException(e);
    }
}
