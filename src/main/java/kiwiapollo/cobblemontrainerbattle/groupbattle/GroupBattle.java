package kiwiapollo.cobblemontrainerbattle.groupbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
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
import kiwiapollo.cobblemontrainerbattle.common.InvalidResourceStateExceptionMessageFactory;
import kiwiapollo.cobblemontrainerbattle.common.ResourceValidator;
import kiwiapollo.cobblemontrainerbattle.exceptions.*;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.PlayerValidator;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.SpecificTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import kotlin.Unit;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GroupBattle {
    public static final int FLAT_LEVEL = 100;
    public static Map<UUID, GroupBattleSession> sessions = new HashMap<>();
    public static Map<UUID, PokemonBattle> trainerBattles = new HashMap<>();

    public static int startSession(CommandContext<ServerCommandSource> context) {
        try {
            GroupBattleSessionValidator validator = new GroupBattleSessionValidator(context.getSource().getPlayer());
            validator.assertNotExistValidSession();

            String groupResourcePath = StringArgumentType.getString(context, "group");
            ResourceValidator.assertValidGroupResource(groupResourcePath);

            GroupBattle.sessions.put(context.getSource().getPlayer().getUuid(), new GroupBattleSession(groupResourcePath));

            context.getSource().getPlayer().sendMessage(
                    Text.translatable("command.cobblemontrainerbattle.groupbattle.startsession.success"));
            CobblemonTrainerBattle.LOGGER.info(String.format("Started battle group session: %s",
                    context.getSource().getPlayer().getGameProfile().getName()));

            return Command.SINGLE_SUCCESS;

        } catch (ExistValidSessionException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.groupbattle.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Valid battle session exists: %s", player.getGameProfile().getName()));
            return 0;

        } catch (InvalidResourceStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = new InvalidResourceStateExceptionMessageFactory().create(e);
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("An error occurred while reading resource: %s", e.getResourcePath(), e.getMessage()));
            return 0;
        }
    }

    public static int stopSession(CommandContext<ServerCommandSource> context) {
        try {
            new GroupBattleSessionValidator(context.getSource().getPlayer()).assertExistValidSession();
            new PlayerValidator(context.getSource().getPlayer()).assertNotPlayerBusyWithPokemonBattle();

            onStopGroupBattleSession(context);
            GroupBattle.sessions.remove(context.getSource().getPlayer().getUuid());

            context.getSource().getPlayer().sendMessage(
                    Text.literal("command.cobblemontrainerbattle.groupbattle.stopsession.success"));
            CobblemonTrainerBattle.LOGGER.info(String.format("Stopped battle group session: %s",
                    context.getSource().getPlayer().getGameProfile().getName()));

            return Command.SINGLE_SUCCESS;

        } catch (NotExistValidSessionException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.groupbattle.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Valid battle session does not exist: %s", player.getGameProfile().getName()));
            return 0;

        } catch (BusyPlayerException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player is busy with Pokemon battle: %s", player.getGameProfile().getName()));
            return 0;
        }
    }

    public static int startBattleWithStatusQuo(CommandContext<ServerCommandSource> context) {
        try {
            GroupBattleSessionValidator sessionValidator = new GroupBattleSessionValidator(context.getSource().getPlayer());
            sessionValidator.assertExistValidSession();
            sessionValidator.assertNotDefeatedAllTrainers();
            sessionValidator.assertNotPlayerDefeated();

            PlayerValidator playerValidator = new PlayerValidator(context.getSource().getPlayer());
            playerValidator.assertNotPlayerBusyWithPokemonBattle();
            playerValidator.assertNotEmptyPlayerParty();
            playerValidator.assertNotFaintPlayerParty();
            playerValidator.assertPlayerPartyAtOrAboveRelativeLevelThreshold();


            String nextTrainerResourcePath = getNextTrainerResourcePath(context.getSource().getPlayer());
            Trainer trainer = new SpecificTrainerFactory().create(context.getSource().getPlayer(), nextTrainerResourcePath);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new StatusQuoPlayerBattleActorFactory().create(context.getSource().getPlayer())),
                    new BattleSide(new TrainerBattleActorFactory().create(trainer)),
                    false

            ).ifSuccessful(pokemonBattle -> {
                GroupBattleSession session = sessions.get(context.getSource().getPlayer().getUuid());
                UUID playerUuid = context.getSource().getPlayer().getUuid();
                trainerBattles.put(playerUuid, pokemonBattle);

                context.getSource().getServer().sendMessage(
                        Text.translatable("command.cobblemontrainerbattle.groupbattle.startbattle.success", trainer.name));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new GroupBattleCommand().getLiteral(),
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (NotExistValidSessionException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.groupbattle.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Valid battle session does not exists: %s", player.getGameProfile().getName()));
            return 0;

        }  catch (DefeatedAllTrainersException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.groupbattle.startbattle.defeated_all_trainers");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player has defeated all trainers: %s", player.getGameProfile().getName()));
            return 0;

        } catch (DefeatedToTrainerException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.groupbattle.startbattle.defeated_to_trainer");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player is defeated: %s", player.getGameProfile().getName()));
            return 0;

        } catch (EmptyPlayerPartyException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.empty_player_party");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player has no Pokemon: %s", player.getGameProfile().getName()));
            return 0;

        } catch (FaintedPlayerPartyException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.fainted_player_party");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Pokemons are all fainted: %s", player.getGameProfile().getName()));
            return 0;

        } catch (BusyPlayerException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player is busy with Pokemon battle: %s", player.getGameProfile().getName()));
            return 0;

        } catch (BelowRelativeLevelThresholdException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.below_relative_level_threshold");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Pokemon levels are below relative level threshold: %s", player.getGameProfile().getName()));
            return 0;
        }
    }

    public static int startBattleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context) {
        try {
            GroupBattleSessionValidator sessionValidator = new GroupBattleSessionValidator(context.getSource().getPlayer());
            sessionValidator.assertExistValidSession();
            sessionValidator.assertNotDefeatedAllTrainers();

            PlayerValidator playerValidator = new PlayerValidator(context.getSource().getPlayer());
            playerValidator.assertNotPlayerBusyWithPokemonBattle();
            playerValidator.assertNotEmptyPlayerParty();

            String nextTrainerResourcePath = getNextTrainerResourcePath(context.getSource().getPlayer());
            Trainer trainer = new SpecificTrainerFactory().create(context.getSource().getPlayer(), nextTrainerResourcePath);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new FlatLevelFullHealthPlayerBattleActorFactory().create(context.getSource().getPlayer(), FLAT_LEVEL)),
                    new BattleSide(new FlatLevelFullHealthTrainerBattleActorFactory().create(trainer, FLAT_LEVEL)),
                    false

            ).ifSuccessful(pokemonBattle -> {
                GroupBattleSession session = sessions.get(context.getSource().getPlayer().getUuid());
                UUID playerUuid = context.getSource().getPlayer().getUuid();
                trainerBattles.put(playerUuid, pokemonBattle);

                context.getSource().getPlayer().sendMessage(
                        Text.translatable("command.cobblemontrainerbattle.groupbattleflat.startbattle.success", trainer.name));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new GroupBattleFlatCommand().getLiteral(),
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (NotExistValidSessionException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.groupbattle.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Valid battle session does not exists: %s", player.getGameProfile().getName()));
            return 0;

        } catch (DefeatedAllTrainersException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.groupbattle.startbattle.defeated_all_trainers");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player has defeated all trainers: %s", player.getGameProfile().getName()));
            return 0;

        } catch (EmptyPlayerPartyException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.empty_player_party");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player has no Pokemon: %s", player.getGameProfile().getName()));
            return 0;

        } catch (BusyPlayerException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player is busy with Pokemon battle: %s", player.getGameProfile().getName()));
            return 0;
        }
    }

    public static String getNextTrainerResourcePath(ServerPlayerEntity player) throws DefeatedAllTrainersException {
        try {
            GroupBattleSession session = sessions.get(player.getUuid());
            GroupFile groupFile = CobblemonTrainerBattle.groupFiles.get(session.groupResourcePath);
            return groupFile.configuration.get("trainers").getAsJsonArray()
                    .get(session.defeatedTrainerCount).getAsString();

        } catch (IndexOutOfBoundsException e) {
            throw new DefeatedAllTrainersException();
        }
    }

    private static void onStopGroupBattleSession(CommandContext<ServerCommandSource> context) {
        if (GroupBattleSessionValidator.isDefeatedAllTrainers(context.getSource().getPlayer())) {
            onVictoryGroupBattleSession(context);
        } else {
            onDefeatGroupBattleSession(context);
        }
    }

    private static void onVictoryGroupBattleSession(CommandContext<ServerCommandSource> context) {
        GroupBattleSession session = sessions.get(context.getSource().getPlayer().getUuid());
        GroupFile groupFile = CobblemonTrainerBattle.groupFiles.get(session.groupResourcePath);

        if (!groupFile.configuration.has("onVictory")) {
            return;
        }

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

    private static void onDefeatGroupBattleSession(CommandContext<ServerCommandSource> context) {
        GroupBattleSession session = sessions.get(context.getSource().getPlayer().getUuid());
        GroupFile groupFile = CobblemonTrainerBattle.groupFiles.get(session.groupResourcePath);

        if (!groupFile.configuration.has("onDefeat")) {
            return;
        }

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
