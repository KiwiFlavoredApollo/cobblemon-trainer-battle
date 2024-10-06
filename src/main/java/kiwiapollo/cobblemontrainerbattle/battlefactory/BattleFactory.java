package kiwiapollo.cobblemontrainerbattle.battlefactory;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.PokemonStats;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.BattleFactoryPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.BattleFactorySpecificTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.commands.BattleFactoryCommand;
import kiwiapollo.cobblemontrainerbattle.common.InvalidPlayerStateType;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidPlayerStateException;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import kotlin.Unit;
import net.minecraft.server.MinecraftServer;
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
    public static Map<UUID, BattleFactorySession> sessions = new HashMap<>();
    public static Map<UUID, PokemonBattle> trainerBattles = new HashMap<>();

    public static int startSession(CommandContext<ServerCommandSource> context) {
        try {
            assertNotExistValidSession(context.getSource().getPlayer());

            List<Trainer> trainersToDefeat = new ArrayList<>();
            for (int i = 0; i < 21; i++) {
                trainersToDefeat.add(new BattleFactoryRandomTrainerFactory().create(context.getSource().getPlayer()));
            }

            BattleFactory.sessions.put(context.getSource().getPlayer().getUuid(), new BattleFactorySession(trainersToDefeat));

            context.getSource().getPlayer().sendMessage(
                    Text.translatable("command.cobblemontrainerbattle.battlefactory.startsession.success"));
            showPartyPokemon(context);
            CobblemonTrainerBattle.LOGGER.info(String.format("%s: Started Battle Factory session",
                    context.getSource().getPlayer().getGameProfile().getName()));

            return Command.SINGLE_SUCCESS;

        } catch (InvalidPlayerStateException e) {
            if (!e.getInvalidPlayerStateType().equals(InvalidPlayerStateType.VALID_SESSION_EXIST)) {
              throw new RuntimeException(e);
            }

            context.getSource().getPlayer().sendMessage(
                    Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_exist")
                            .formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    public static int stopSession(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertNotPlayerBusyWithPokemonBattle(context.getSource().getPlayer());

            onStopBattleFactorySession(context);
            BattleFactory.sessions.remove(context.getSource().getPlayer().getUuid());

            context.getSource().getPlayer().sendMessage(
                    Text.translatable("command.cobblemontrainerbattle.battlefactory.stopsession.success"));
            CobblemonTrainerBattle.LOGGER.info(String.format("%s: Stopped Battle Factory session",
                    context.getSource().getPlayer().getGameProfile().getName()));

            return Command.SINGLE_SUCCESS;

        } catch (InvalidPlayerStateException e) {
            if (!e.getInvalidPlayerStateType().equals(InvalidPlayerStateType.VALID_SESSION_EXIST)) {
                throw new RuntimeException(e);
            }

            context.getSource().getPlayer().sendMessage(
                    Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist")
                            .formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;

        }
    }

    private static void onStopBattleFactorySession(CommandContext<ServerCommandSource> context) {
        if (isDefeatedAllTrainers(context.getSource().getPlayer())) {
            onVictoryBattleFactorySession(context);
        } else {
            onDefeatBattleFactorySession(context);
        }
    }

    public static int startBattle(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertNotPlayerDefeated(context.getSource().getPlayer());
            assertNotPlayerBusyWithPokemonBattle(context.getSource().getPlayer());
            assertNotDefeatedAllTrainers(context.getSource().getPlayer());

            BattleFactorySession session = sessions.get(context.getSource().getPlayer().getUuid());
            Trainer trainer = session.trainersToDefeat.get(session.defeatedTrainerCount);
            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new BattleFactoryPlayerBattleActorFactory().create(context.getSource().getPlayer())),
                    new BattleSide(new BattleFactorySpecificTrainerBattleActorFactory().create(trainer)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                UUID playerUuid = context.getSource().getPlayer().getUuid();
                trainerBattles.put(playerUuid, pokemonBattle);

                context.getSource().getPlayer().sendMessage(
                        Text.translatable("command.cobblemontrainerbattle.battlefactory.startbattle.success", trainer.name));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new BattleFactoryCommand().getLiteral(),
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (InvalidPlayerStateException e) {
            Text message = switch (e.getInvalidPlayerStateType()) {
                case VALID_SESSION_NOT_EXIST ->
                        Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
                case DEFEATED_TO_TRAINER ->
                        Text.translatable("command.cobblemontrainerbattle.battlefactory.startbattle.defeated_to_trainer");
                case DEFEATED_ALL_TRAINERS ->
                        Text.translatable("command.cobblemontrainerbattle.battlefactory.startbattle.defeated_all_trainers");
                case BUSY_WITH_POKEMON_BATTLE ->
                        Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle");
                default -> throw new RuntimeException(e);
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

            BattleFactorySession session = sessions.get(context.getSource().getPlayer().getUuid());
            Trainer lastDefeatedTrainer = session.trainersToDefeat.get(session.defeatedTrainerCount - 1);
            Pokemon trainerPokemon = lastDefeatedTrainer.pokemons.get(trainerslot - 1);
            Pokemon playerPokemon = session.partyPokemons.get(playerslot - 1);

            session.partyPokemons = new ArrayList<>(session.partyPokemons);
            session.partyPokemons.set(playerslot - 1, trainerPokemon.clone(true, true));
            session.isTradedPokemon = true;

            MutableText pokemonTradeMessage = Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.success",
                    playerPokemon.getDisplayName(), trainerPokemon.getDisplayName()).formatted(Formatting.YELLOW);

            context.getSource().getPlayer().sendMessage(pokemonTradeMessage);
            CobblemonTrainerBattle.LOGGER.info(pokemonTradeMessage.getString());

            return Command.SINGLE_SUCCESS;

        } catch (InvalidPlayerStateException e) {
            Text message = switch (e.getInvalidPlayerStateType()) {
                case VALID_SESSION_NOT_EXIST ->
                        Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
                case DEFEATED_TO_TRAINER ->
                        Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.defeated_to_trainer");
                case DEFEATED_NO_TRAINER ->
                        Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.defeated_trainer_not_exist");
                case TRADED_POKEMON_WITH_TRAINER ->
                        Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.already_traded_pokemon");
                default -> throw new RuntimeException(e);
            };
            context.getSource().getPlayer().sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    public static int showTradeablePokemon(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertExistDefeatedTrainer(context.getSource().getPlayer());

            BattleFactorySession session = sessions.get(context.getSource().getPlayer().getUuid());
            Trainer lastDefeatedTrainer = session.trainersToDefeat.get(session.defeatedTrainerCount - 1);
            printPokemons(context, lastDefeatedTrainer.pokemons);

            return Command.SINGLE_SUCCESS;

        } catch (InvalidPlayerStateException e) {
            Text message = switch (e.getInvalidPlayerStateType()) {
                case VALID_SESSION_NOT_EXIST ->
                        Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
                case DEFEATED_NO_TRAINER ->
                        Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.defeated_trainer_not_exist");
                default -> throw new RuntimeException(e);
            };
            context.getSource().getPlayer().sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    public static int showPartyPokemon(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            printPokemons(context, sessions.get(context.getSource().getPlayer().getUuid()).partyPokemons);
            return Command.SINGLE_SUCCESS;

        } catch (InvalidPlayerStateException e) {
            if (!e.getInvalidPlayerStateType().equals(InvalidPlayerStateType.VALID_SESSION_NOT_EXIST)) {
                throw new RuntimeException(e);
            }

            context.getSource().getPlayer().sendMessage(
                    Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist")
                            .formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    public static int rerollPokemon(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertNotExistDefeatedTrainers(context.getSource().getPlayer());

            BattleFactorySession session = sessions.get(context.getSource().getPlayer().getUuid());

            session.partyPokemons = List.of(
                    PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL),
                    PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL),
                    PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL)
            );
            context.getSource().getPlayer().sendMessage(
                    Text.translatable("command.cobblemontrainerbattle.battlefactory.rerollpokemon.success"));
            showPartyPokemon(context);

            return Command.SINGLE_SUCCESS;

        } catch (InvalidPlayerStateException e) {
            Text message = switch (e.getInvalidPlayerStateType()) {
                case VALID_SESSION_NOT_EXIST ->
                        Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
                case DEFEATED_ANY_TRAINERS ->
                        Text.translatable("command.cobblemontrainerbattle.battlefactory.rerollpokemon.defeated_trainer_exist");
                default -> throw new RuntimeException(e);
            };

            context.getSource().getPlayer().sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    public static int showWinningStreak(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());

            BattleFactorySession session = sessions.get(context.getSource().getPlayer().getUuid());
            context.getSource().getPlayer().sendMessage(
                    Text.translatable("command.cobblemontrainerbattle.battlefactory.winningstreak.success",
                            session.defeatedTrainerCount));

            return Command.SINGLE_SUCCESS;

        } catch (InvalidPlayerStateException e) {
            if (!e.getInvalidPlayerStateType().equals(InvalidPlayerStateType.VALID_SESSION_NOT_EXIST)) {
                throw new RuntimeException(e);
            }

            context.getSource().getPlayer().sendMessage(
                    Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist")
                            .formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    private static void assertNotPlayerDefeated(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        BattleFactorySession session = sessions.get(player.getUuid());
        if (session.isDefeated) {
            throw new InvalidPlayerStateException(
                    String.format("Player is defeated: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.DEFEATED_TO_TRAINER
            );
        }
    }

    private static void assertNotDefeatedAllTrainers(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        if (isDefeatedAllTrainers(player)) {
            throw new InvalidPlayerStateException(
                    String.format("Player has defeated all trainers: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.DEFEATED_ALL_TRAINERS
            );
        }
    }

    private static boolean isDefeatedAllTrainers(ServerPlayerEntity player) {
        try {
            BattleFactorySession session = sessions.get(player.getUuid());
            return session.defeatedTrainerCount == session.trainersToDefeat.size();

        } catch (NullPointerException | ClassCastException | IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    private static void assertExistValidSession(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        if (!isExistValidSession(player)) {
            throw new InvalidPlayerStateException(
                    String.format("Valid battle session does not exists: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.VALID_SESSION_NOT_EXIST
            );
        }
    }

    private static void assertNotExistValidSession(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        if (isExistValidSession(player)) {
            throw new InvalidPlayerStateException(
                    String.format("Valid battle session exists: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.VALID_SESSION_EXIST
            );
        }
    }

    private static boolean isExistValidSession(ServerPlayerEntity player) {
        if (!sessions.containsKey(player.getUuid())) {
            return false;
        }

        BattleFactorySession session = sessions.get(player.getUuid());
        return Instant.now().isBefore(session.timestamp.plus(Duration.ofHours(24)));
    }

    private static void assertNotPlayerTradedPokemon(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        if (sessions.get(player.getUuid()).isTradedPokemon) {
            throw new InvalidPlayerStateException(
                    String.format("Player has already traded a Pokemon: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.TRADED_POKEMON_WITH_TRAINER
            );
        }
    }

    private static void assertExistDefeatedTrainer(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        if (!isExistDefeatedTrainers(player)) {
            throw new InvalidPlayerStateException(
                    String.format("Player has no defeated trainers: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.DEFEATED_NO_TRAINER
            );
        }
    }

    private static void assertNotExistDefeatedTrainers(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        if (isExistDefeatedTrainers(player)) {
            throw new InvalidPlayerStateException(
                    String.format("Player has defeated trainers: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.DEFEATED_ANY_TRAINERS
            );
        }
    }

    private static boolean isExistDefeatedTrainers(ServerPlayerEntity player) {
        return sessions.get(player.getUuid()).defeatedTrainerCount != 0;
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
    }

    private static void assertNotPlayerBusyWithPokemonBattle(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        if (Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) != null) {
            throw new InvalidPlayerStateException(
                    String.format("Player is busy with Pokemon battle: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.BUSY_WITH_POKEMON_BATTLE
            );
        }
    }

    private static void onVictoryBattleFactorySession(CommandContext<ServerCommandSource> context) {
        if (!CobblemonTrainerBattle.battleFactoryConfiguration.has("onVictory")) {
            return;
        }

        JsonObject onVictory = CobblemonTrainerBattle.battleFactoryConfiguration.get("onVictory").getAsJsonObject();

        if (onVictory.has("balance") && onVictory.get("balance").isJsonPrimitive()) {
            CobblemonTrainerBattle.ECONOMY.addBalance(
                    context.getSource().getPlayer(), onVictory.get("balance").getAsDouble());
        }

        if (onVictory.has("commands") && onVictory.get("commands").isJsonArray()) {
            onVictory.get("commands").getAsJsonArray().asList().stream()
                    .filter(JsonElement::isJsonPrimitive)
                    .map(JsonElement::getAsString)
                    .forEach(command -> {
                        executeCommand(context.getSource().getPlayer(), command);
                    });
        }
    }

    private static void onDefeatBattleFactorySession(CommandContext<ServerCommandSource> context) {
        if (!CobblemonTrainerBattle.battleFactoryConfiguration.has("onDefeat")) {
            return;
        }

        JsonObject onDefeat = CobblemonTrainerBattle.battleFactoryConfiguration.get("onDefeat").getAsJsonObject();

        if (onDefeat.has("balance") && onDefeat.get("balance").isJsonPrimitive()) {
            CobblemonTrainerBattle.ECONOMY.removeBalance(
                    context.getSource().getPlayer(), onDefeat.get("balance").getAsDouble());
        }

        if (onDefeat.has("commands") && onDefeat.get("commands").isJsonArray()) {
            onDefeat.get("commands").getAsJsonArray().asList().stream()
                    .filter(JsonElement::isJsonPrimitive)
                    .map(JsonElement::getAsString)
                    .forEach(command -> {
                        executeCommand(context.getSource().getPlayer(), command);
                    });
        }
    }

    private static void executeCommand(ServerPlayerEntity player, String command) {
        try {
            command = command.replace("%player%", player.getGameProfile().getName());

            MinecraftServer server = player.getCommandSource().getServer();
            CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

            server.getCommandManager().execute(
                    dispatcher.parse(command, server.getCommandSource()), command);

        } catch (UnsupportedOperationException e) {
            CobblemonTrainerBattle.LOGGER.error(
                    String.format("Error occurred while running command: %s", command));
        }
    }
}
