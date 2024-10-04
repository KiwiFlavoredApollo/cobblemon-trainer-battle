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
import kiwiapollo.cobblemontrainerbattle.common.CommandConditionType;
import kiwiapollo.cobblemontrainerbattle.exceptions.CommandConditionNotSatisfiedException;
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

    public static int startSession(CommandContext<ServerCommandSource> context) {
        try {
            assertNotExistValidSession(context.getSource().getPlayer());

            List<Trainer> trainersToDefeat = new ArrayList<>();
            for (int i = 0; i < 21; i++) {
                trainersToDefeat.add(new BattleFactoryRandomTrainerFactory().create(context.getSource().getPlayer()));
            }

            BattleFactory.SESSIONS.put(context.getSource().getPlayer().getUuid(), new BattleFactorySession(trainersToDefeat));

            context.getSource().getPlayer().sendMessage(Text.literal("Battle Factory session has started"));
            showPartyPokemons(context);
            CobblemonTrainerBattle.LOGGER.info(String.format("%s: Started Battle Factory session",
                    context.getSource().getPlayer().getGameProfile().getName()));

            return Command.SINGLE_SUCCESS;

        } catch (CommandConditionNotSatisfiedException e) {
            Text message = switch (e.getCommandConditionType()) {
                case VALID_SESSION_EXISTS ->
                        Text.translatable("command.cobblemontrainerbattle.valid_group_battle_session_exist");
                default ->
                        Text.translatable("command.cobblemontrainerbattle.default_error");
            };
            context.getSource().getPlayer().sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    public static int stopSession(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertNotPlayerBusyWithPokemonBattle(context.getSource().getPlayer());

            BattleFactory.SESSIONS.remove(context.getSource().getPlayer().getUuid());

            context.getSource().getPlayer().sendMessage(Text.literal("Battle Factory session has stopped"));
            CobblemonTrainerBattle.LOGGER.info(String.format("%s: Stopped Battle Factory session",
                    context.getSource().getPlayer().getGameProfile().getName()));

            return Command.SINGLE_SUCCESS;

        } catch (CommandConditionNotSatisfiedException e) {
            Text message = switch (e.getCommandConditionType()) {
                case VALID_SESSION_NOT_EXISTS ->
                        Text.translatable("command.cobblemontrainerbattle.valid_group_battle_session_not_exist");
                default ->
                        Text.translatable("command.cobblemontrainerbattle.default_error");
            };
            context.getSource().getPlayer().sendMessage(message.copy().formatted(Formatting.RED));
            return 0;

        }
    }

    public static int startBattle(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertNotPlayerDefeated(context.getSource().getPlayer());
            assertNotPlayerBusyWithPokemonBattle(context.getSource().getPlayer());
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
                        Text.literal("Battle Factory Pokemon Battle has started"));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new BattleFactoryCommand().getLiteral(),
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (CommandConditionNotSatisfiedException e) {
            Text message = switch (e.getCommandConditionType()) {
                case VALID_SESSION_NOT_EXISTS ->
                        Text.translatable("command.cobblemontrainerbattle.valid_group_battle_session_not_exist");
                case DEFEATED_TO_TRAINER ->
                        Text.translatable("command.cobblemontrainerbattle.unable_to_continue_session_defeated_to_trainer");
                case DEFEATED_ALL_TRAINERS ->
                        Text.translatable("command.cobblemontrainerbattle.unable_to_continue_session_defeated_all_trainers");
                case BUSY_WITH_POKEMON_BATTLE ->
                        Text.translatable("command.cobblemontrainerbattle.busy_with_pokemon_battle");
                default ->
                        Text.translatable("command.cobblemontrainerbattle.default_error");
            };
            context.getSource().getPlayer().sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
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

        } catch (CommandConditionNotSatisfiedException e) {
            Text message = switch (e.getCommandConditionType()) {
                case VALID_SESSION_NOT_EXISTS ->
                        Text.translatable("command.cobblemontrainerbattle.valid_group_battle_session_not_exist");
                case DEFEATED_TO_TRAINER ->
                        Text.translatable("command.cobblemontrainerbattle.unable_to_continue_session_defeated_to_trainer");
                case DEFEATED_NO_TRAINER ->
                        Text.translatable("command.cobblemontrainerbattle.unable_to_trade_pokemon_defeated_trainer_not_exist");
                case POKEMON_TRADED ->
                        Text.translatable("command.cobblemontrainerbattle.unable_to_trade_pokemon_defeated_trainer_not_exist");
                default ->
                        Text.translatable("command.cobblemontrainerbattle.default_error");
            };
            context.getSource().getPlayer().sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
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

        } catch (CommandConditionNotSatisfiedException e) {
            Text message = switch (e.getCommandConditionType()) {
                case VALID_SESSION_NOT_EXISTS ->
                        Text.translatable("command.cobblemontrainerbattle.valid_group_battle_session_not_exist");
                case DEFEATED_NO_TRAINER ->
                        Text.translatable("command.cobblemontrainerbattle.unable_to_trade_pokemon_defeated_trainer_not_exist");
                default ->
                        Text.translatable("command.cobblemontrainerbattle.default_error");
            };
            context.getSource().getPlayer().sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    public static int showPartyPokemons(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            printPokemons(context, SESSIONS.get(context.getSource().getPlayer().getUuid()).partyPokemons);
            return Command.SINGLE_SUCCESS;

        } catch (CommandConditionNotSatisfiedException e) {
            Text message = switch (e.getCommandConditionType()) {
                case VALID_SESSION_NOT_EXISTS ->
                        Text.translatable("command.cobblemontrainerbattle.valid_group_battle_session_not_exist");
                default ->
                        Text.translatable("command.cobblemontrainerbattle.default_error");
            };
            context.getSource().getPlayer().sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
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

        } catch (CommandConditionNotSatisfiedException e) {
            Text message = switch (e.getCommandConditionType()) {
                case VALID_SESSION_NOT_EXISTS ->
                        Text.translatable("command.cobblemontrainerbattle.valid_group_battle_session_not_exist");
                case DEFEATED_ANY_TRAINERS ->
                        Text.translatable("command.cobblemontrainerbattle.unable_to_reroll_pokemons_defeated_trainer_exist");
                default ->
                        Text.translatable("command.cobblemontrainerbattle.default_error");
            };
            context.getSource().getPlayer().sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    public static int showWinningStreak(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());

            BattleFactorySession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
            context.getSource().getPlayer().sendMessage(
                    Text.literal(String.format("Winning streak: %d", session.defeatedTrainerCount)));

            return Command.SINGLE_SUCCESS;

        } catch (CommandConditionNotSatisfiedException e) {
            Text message = switch (e.getCommandConditionType()) {
                case VALID_SESSION_NOT_EXISTS ->
                        Text.translatable("command.cobblemontrainerbattle.valid_group_battle_session_not_exist");
                default ->
                        Text.translatable("command.cobblemontrainerbattle.default_error");
            };
            context.getSource().getPlayer().sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    private static void assertNotPlayerDefeated(ServerPlayerEntity player)
            throws CommandConditionNotSatisfiedException {
        BattleFactorySession session = SESSIONS.get(player.getUuid());
        if (session.isDefeated) {
            throw new CommandConditionNotSatisfiedException(
                    CommandConditionType.DEFEATED_TO_TRAINER,
                    String.format("Player is defeated: %s", player.getGameProfile().getName())
            );
        }
    }

    private static void assertNotDefeatedAllTrainers(ServerPlayerEntity player)
            throws CommandConditionNotSatisfiedException {
        if (isDefeatedAllTrainers(player)) {
            throw new CommandConditionNotSatisfiedException(
                    CommandConditionType.DEFEATED_ALL_TRAINERS,
                    String.format("Player has defeated all trainers: %s", player.getGameProfile().getName())
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
            throws CommandConditionNotSatisfiedException {
        if (!isExistValidSession(player)) {
            throw new CommandConditionNotSatisfiedException(
                    CommandConditionType.VALID_SESSION_NOT_EXISTS,
                    String.format("Valid battle session does not exists: %s", player.getGameProfile().getName())
            );
        }
    }

    private static void assertNotExistValidSession(ServerPlayerEntity player)
            throws CommandConditionNotSatisfiedException {
        if (isExistValidSession(player)) {
            throw new CommandConditionNotSatisfiedException(
                    CommandConditionType.VALID_SESSION_EXISTS,
                    String.format("Valid battle session exists: %s", player.getGameProfile().getName())
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
            throws CommandConditionNotSatisfiedException {
        if (SESSIONS.get(player.getUuid()).isTradedPokemon) {
            throw new CommandConditionNotSatisfiedException(
                    CommandConditionType.POKEMON_TRADED,
                    String.format("Player has already traded a Pokemon: %s", player.getGameProfile().getName())
            );
        }
    }

    private static void assertExistDefeatedTrainer(ServerPlayerEntity player)
            throws CommandConditionNotSatisfiedException {
        if (!isExistDefeatedTrainers(player)) {
            throw new CommandConditionNotSatisfiedException(
                    CommandConditionType.DEFEATED_NO_TRAINER,
                    String.format("Player has no defeated trainers: %s", player.getGameProfile().getName())
            );
        }
    }

    private static void assertNotExistDefeatedTrainers(ServerPlayerEntity player)
            throws CommandConditionNotSatisfiedException {
        if (isExistDefeatedTrainers(player)) {
            throw new CommandConditionNotSatisfiedException(
                    CommandConditionType.DEFEATED_ANY_TRAINERS,
                    String.format("Player has defeated trainers: %s", player.getGameProfile().getName())
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

    private static void assertNotPlayerBusyWithPokemonBattle(ServerPlayerEntity player)
            throws CommandConditionNotSatisfiedException {
        if (Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) != null) {
            throw new CommandConditionNotSatisfiedException(
                    CommandConditionType.BUSY_WITH_POKEMON_BATTLE,
                    String.format("Player is busy with Pokemon battle: %s", player.getGameProfile().getName())
            );
        }
    }
}
