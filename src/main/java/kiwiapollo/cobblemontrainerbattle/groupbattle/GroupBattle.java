package kiwiapollo.cobblemontrainerbattle.groupbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.FlatLevelFullHealthPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.StatusQuoPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.FlatLevelFullHealthTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.TrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.commands.GroupBattleCommand;
import kiwiapollo.cobblemontrainerbattle.commands.GroupBattleFlatCommand;
import kiwiapollo.cobblemontrainerbattle.common.InvalidPlayerStateType;
import kiwiapollo.cobblemontrainerbattle.common.InvalidResourceState;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidPlayerStateException;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidResourceStateException;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.SpecificTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerFileParser;
import kotlin.Unit;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class GroupBattle {
    public static final int FLAT_LEVEL = 100;
    public static Map<UUID, GroupBattleSession> SESSIONS = new HashMap<>();

    public static int startSession(CommandContext<ServerCommandSource> context) {
        try {
            assertNotExistValidSession(context.getSource().getPlayer());

            String groupResourcePath = StringArgumentType.getString(context, "group");
            assertValidGroupResource(groupResourcePath);

            GroupBattle.SESSIONS.put(context.getSource().getPlayer().getUuid(), new GroupBattleSession(groupResourcePath));

            context.getSource().getPlayer().sendMessage(
                    Text.translatable("command.cobblemontrainerbattle.groupbattle.startsession.success"));
            CobblemonTrainerBattle.LOGGER.info(String.format("Started battle group session: %s",
                    context.getSource().getPlayer().getGameProfile().getName()));

            return Command.SINGLE_SUCCESS;

        } catch (InvalidPlayerStateException e) {
            if (!e.getInvalidPlayerStateType().equals(InvalidPlayerStateType.VALID_SESSION_EXIST)) {
                throw new RuntimeException(e);
            }

            context.getSource().getPlayer().sendMessage(
                    Text.translatable("command.cobblemontrainerbattle.groupbattle.common.valid_session_exist")
                            .formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;

        } catch (InvalidResourceStateException e) {
            Text message = switch (e.getInvalidResourceState()) {
                case NOT_FOUND ->
                        Text.translatable("command.cobblemontrainerbattle.common.resource.not_found", e.getResourcePath());
                case CANNOT_BE_READ ->
                        Text.translatable("command.cobblemontrainerbattle.common.resource.cannot_be_read", e.getResourcePath());
                case CONTAINS_INVALID_VALUE ->
                        Text.translatable("command.cobblemontrainerbattle.groupbattle.resource.no_trainer", e.getResourcePath());
                case CONTAINS_NO_VALUE ->
                        Text.translatable("command.cobblemontrainerbattle.groupbattle.resource.trainers_not_found", e.getResourcePath());
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

            onGroupBattleSessionStop(context);
            GroupBattle.SESSIONS.remove(context.getSource().getPlayer().getUuid());

            context.getSource().getPlayer().sendMessage(
                    Text.literal("command.cobblemontrainerbattle.groupbattle.stopsession.success"));
            CobblemonTrainerBattle.LOGGER.info(String.format("Stopped battle group session: %s",
                    context.getSource().getPlayer().getGameProfile().getName()));

            return Command.SINGLE_SUCCESS;

        } catch (InvalidPlayerStateException e) {
            if (!e.getInvalidPlayerStateType().equals(InvalidPlayerStateType.VALID_SESSION_NOT_EXIST)) {
                throw new RuntimeException(e);
            }

            context.getSource().getPlayer().sendMessage(
                    Text.translatable("command.cobblemontrainerbattle.groupbattle.common.valid_session_not_exist")
                            .formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    private static void onGroupBattleSessionStop(CommandContext<ServerCommandSource> context) {
        if (isDefeatedAllTrainers(context.getSource().getPlayer())) {
            onGroupBattleVictory(context);
        } else {
            onGroupBattleDefeat(context);
        }
    }

    public static int startBattleWithStatusQuo(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertNotPlayerDefeated(context.getSource().getPlayer());
            assertNotPlayerBusyWithPokemonBattle(context.getSource().getPlayer());
            assertNotEmptyPlayerParty(context.getSource().getPlayer());
            assertNotFaintPlayerParty(context.getSource().getPlayer());
            assertPlayerPartyAtOrAboveRelativeLevelThreshold(context.getSource().getPlayer());
            assertNotDefeatedAllTrainers(context.getSource().getPlayer());

            String nextTrainerResourcePath = getNextTrainerResourcePath(context.getSource().getPlayer());
            Trainer trainer = new SpecificTrainerFactory().create(context.getSource().getPlayer(), nextTrainerResourcePath);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new StatusQuoPlayerBattleActorFactory().create(context.getSource().getPlayer())),
                    new BattleSide(new TrainerBattleActorFactory().create(trainer)),
                    false

            ).ifSuccessful(pokemonBattle -> {
                UUID playerUuid = context.getSource().getPlayer().getUuid();
                CobblemonTrainerBattle.trainerBattles.put(playerUuid, pokemonBattle);
                GroupBattle.SESSIONS.get(playerUuid).battleUuid = pokemonBattle.getBattleId();

                context.getSource().getServer().sendMessage(
                        Text.translatable("command.cobblemontrainerbattle.groupbattle.startbattle.success", trainer.name));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new GroupBattleCommand().getLiteral(),
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (InvalidPlayerStateException e) {
            Text message = switch (e.getInvalidPlayerStateType()) {
                case VALID_SESSION_NOT_EXIST ->
                        Text.translatable("command.cobblemontrainerbattle.groupbattle.common.valid_session_not_exist");
                case DEFEATED_TO_TRAINER ->
                        Text.translatable("command.cobblemontrainerbattle.groupbattle.startbattle.defeated_to_trainer");
                case DEFEATED_ALL_TRAINERS ->
                        Text.translatable("command.cobblemontrainerbattle.groupbattle.startbattle.defeated_all_trainers");
                case EMPTY_PLAYER_PARTY ->
                        Text.translatable("command.cobblemontrainerbattle.common.empty_player_party");
                case FAINTED_PLAYER_PARTY ->
                        Text.translatable("command.cobblemontrainerbattle.common.fainted_player_party");
                case BELOW_RELATIVE_LEVEL_THRESHOLD ->
                        Text.translatable("command.cobblemontrainerbattle.common.below_relative_level_threshold");
                case BUSY_WITH_POKEMON_BATTLE ->
                        Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle");
                default -> throw new RuntimeException(e);
            };
            context.getSource().getPlayer().sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    private static void assertNotDefeatedAllTrainers(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        if (isDefeatedAllTrainers(player)) {
            throw new InvalidPlayerStateException(
                    String.format("Player has defeated all trainers: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.DEFEATED_ALL_TRAINERS
            );
        };
    }

    private static boolean isDefeatedAllTrainers(ServerPlayerEntity player) {
        try {
            GroupBattleSession session = SESSIONS.get(player.getUuid());

            GroupFile groupFile = CobblemonTrainerBattle.groupFiles.get(session.groupResourcePath);
            int groupTrainerCount = groupFile.configuration.get("trainers").getAsJsonArray().size();

            return session.defeatedTrainerCount == groupTrainerCount;

        } catch (NullPointerException | ClassCastException | IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    public static int startBattleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertNotPlayerDefeated(context.getSource().getPlayer());
            assertNotPlayerBusyWithPokemonBattle(context.getSource().getPlayer());
            assertNotEmptyPlayerParty(context.getSource().getPlayer());

            String nextTrainerResourcePath = getNextTrainerResourcePath(context.getSource().getPlayer());
            Trainer trainer = new SpecificTrainerFactory().create(context.getSource().getPlayer(), nextTrainerResourcePath);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new FlatLevelFullHealthPlayerBattleActorFactory().create(context.getSource().getPlayer(), FLAT_LEVEL)),
                    new BattleSide(new FlatLevelFullHealthTrainerBattleActorFactory().create(trainer, FLAT_LEVEL)),
                    false

            ).ifSuccessful(pokemonBattle -> {
                UUID playerUuid = context.getSource().getPlayer().getUuid();
                CobblemonTrainerBattle.trainerBattles.put(playerUuid, pokemonBattle);
                GroupBattle.SESSIONS.get(playerUuid).battleUuid = pokemonBattle.getBattleId();

                context.getSource().getPlayer().sendMessage(
                        Text.translatable("command.cobblemontrainerbattle.groupbattleflat.startbattle.success", trainer.name));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new GroupBattleFlatCommand().getLiteral(),
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (InvalidPlayerStateException e) {
            Text message = switch (e.getInvalidPlayerStateType()) {
                case VALID_SESSION_NOT_EXIST ->
                        Text.translatable("command.cobblemontrainerbattle.groupbattle.common.valid_session_not_exist");
                case DEFEATED_TO_TRAINER ->
                        Text.translatable("command.cobblemontrainerbattle.groupbattle.startbattle.defeated_to_trainer");
                case DEFEATED_ALL_TRAINERS ->
                        Text.translatable("command.cobblemontrainerbattle.groupbattle.startbattle.defeated_all_trainers");
                case EMPTY_PLAYER_PARTY ->
                        Text.translatable("command.cobblemontrainerbattle.common.empty_player_party");
                case BUSY_WITH_POKEMON_BATTLE ->
                        Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle");
                default -> throw new RuntimeException(e);
            };
            context.getSource().getPlayer().sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    public static String getNextTrainerResourcePath(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        try {
            GroupBattleSession session = SESSIONS.get(player.getUuid());
            GroupFile groupFile = CobblemonTrainerBattle.groupFiles.get(session.groupResourcePath);
            return groupFile.configuration.get("trainers").getAsJsonArray()
                    .get(session.defeatedTrainerCount).getAsString();

        } catch (IndexOutOfBoundsException e) {
            throw new InvalidPlayerStateException(
                    String.format("Player has defeated all trainers: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.DEFEATED_ALL_TRAINERS
            );
        }
    }

    private static void assertValidGroupResource(String groupResourcePath) throws InvalidResourceStateException {
        try {
            GroupFile groupFile = CobblemonTrainerBattle.groupFiles.get(groupResourcePath);
            if (groupFile == null) {
                throw new InvalidResourceStateException(
                        String.format("Resource not found: %s", groupResourcePath),
                        InvalidResourceState.NOT_FOUND,
                        groupResourcePath
                );
            }

            if (groupFile.configuration.get("trainers").getAsJsonArray().isEmpty()) {
                throw new InvalidResourceStateException(
                        String.format("No trainers: %s", groupResourcePath),
                        InvalidResourceState.CONTAINS_NO_VALUE,
                        groupResourcePath
                );
            }

            if (!groupFile.configuration.get("trainers").getAsJsonArray().asList().stream()
                    .map(JsonElement::getAsString)
                    .allMatch(CobblemonTrainerBattle.trainerFiles::containsKey)) {
                throw new InvalidResourceStateException(
                        String.format("One or more trainers are unknown: %s", groupResourcePath),
                        InvalidResourceState.CONTAINS_INVALID_VALUE,
                        groupResourcePath
                );
            };

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException | ClassCastException e) {
            throw new InvalidResourceStateException(
                    String.format("Unable to read resource: %s", groupResourcePath),
                    InvalidResourceState.CANNOT_BE_READ,
                    groupResourcePath
            );
        }
    }

    private static void assertNotPlayerDefeated(ServerPlayerEntity player) throws InvalidPlayerStateException {
        GroupBattleSession session = SESSIONS.get(player.getUuid());
        if (session.isDefeated) {
            throw new InvalidPlayerStateException(
                    String.format("Player is defeated: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.DEFEATED_TO_TRAINER
            );
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
        if (!SESSIONS.containsKey(player.getUuid())) {
            return false;
        }

        GroupBattleSession session = SESSIONS.get(player.getUuid());
        return Instant.now().isBefore(session.timestamp.plus(Duration.ofHours(24)));
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

    private static void assertNotEmptyPlayerParty(ServerPlayerEntity player) throws InvalidPlayerStateException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        if (playerPartyStore.toGappyList().stream().allMatch(Objects::isNull)) {
            throw new InvalidPlayerStateException(
                    String.format("Player has no Pokemon: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.EMPTY_PLAYER_PARTY
            );
        }
    }

    private static void assertPlayerPartyAtOrAboveRelativeLevelThreshold(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);
        if (pokemons.map(Pokemon::getLevel).allMatch(level -> level < TrainerFileParser.RELATIVE_LEVEL_THRESHOLD)) {
            throw new InvalidPlayerStateException(
                    String.format("Pokemon levels are below relative level threshold", player.getGameProfile().getName()),
                    InvalidPlayerStateType.BELOW_RELATIVE_LEVEL_THRESHOLD
            );
        }
    }

    private static void assertNotFaintPlayerParty(ServerPlayerEntity player) throws InvalidPlayerStateException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);
        if (pokemons.allMatch(Pokemon::isFainted)) {
            throw new InvalidPlayerStateException(
                    String.format("Pokemons are all fainted: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.FAINTED_PLAYER_PARTY
            );
        }
    }


    private static void onGroupBattleVictory(CommandContext<ServerCommandSource> context) {
        GroupBattleSession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
        GroupFile groupFile = CobblemonTrainerBattle.groupFiles.get(session.groupResourcePath);
        JsonObject onVictory = groupFile.configuration.get("onVictory").getAsJsonObject();

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

    private static void onGroupBattleDefeat(CommandContext<ServerCommandSource> context) {
        GroupBattleSession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
        GroupFile groupFile = CobblemonTrainerBattle.groupFiles.get(session.groupResourcePath);
        JsonObject onDefeat = groupFile.configuration.get("onDefeat").getAsJsonObject();

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
