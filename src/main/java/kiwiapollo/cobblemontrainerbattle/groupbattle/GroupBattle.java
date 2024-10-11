package kiwiapollo.cobblemontrainerbattle.groupbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.PlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.VirtualTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.commands.GroupBattleCommand;
import kiwiapollo.cobblemontrainerbattle.commands.GroupBattleFlatCommand;
import kiwiapollo.cobblemontrainerbattle.common.InvalidResourceStateExceptionMessageFactory;
import kiwiapollo.cobblemontrainerbattle.common.PostBattleAction;
import kiwiapollo.cobblemontrainerbattle.common.ResourceValidator;
import kiwiapollo.cobblemontrainerbattle.common.TrainerGroup;
import kiwiapollo.cobblemontrainerbattle.exceptions.*;
import kiwiapollo.cobblemontrainerbattle.common.PlayerValidator;
import kotlin.Unit;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GroupBattle {
    public static final int FLAT_LEVEL = 100;
    public static Map<UUID, GroupBattleSession> sessions = new HashMap<>();
    public static Map<UUID, PokemonBattle> trainerBattles = new HashMap<>();

    public static int startSession(CommandContext<ServerCommandSource> context) {
        try {
            new GroupBattleSessionValidator(context.getSource().getPlayer()).assertNotExistValidSession();

            String groupResourcePath = StringArgumentType.getString(context, "group");
            new ResourceValidator().assertExistValidGroupResource(groupResourcePath);

            Identifier identifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, groupResourcePath);
            GroupBattle.sessions.put(context.getSource().getPlayer().getUuid(), new GroupBattleSession(identifier));

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
            new PlayerValidator(context.getSource().getPlayer()).assertPlayerNotBusyWithPokemonBattle();

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

        } catch (BusyWithPokemonBattleException e) {
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
            playerValidator.assertPlayerNotBusyWithPokemonBattle();
            playerValidator.assertNotEmptyPlayerParty();
            playerValidator.assertNotFaintPlayerParty();
            playerValidator.assertPlayerPartyAtOrAboveRelativeLevelThreshold();

            Identifier nextTrainerIdentifier = getNextTrainerIdentifier(context.getSource().getPlayer());

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new PlayerBattleActorFactory().createWithStatusQuo(context.getSource().getPlayer())),
                    new BattleSide(new VirtualTrainerBattleActorFactory(context.getSource().getPlayer()).createWithStatusQuo(nextTrainerIdentifier)),
                    false

            ).ifSuccessful(pokemonBattle -> {
                GroupBattleSession session = sessions.get(context.getSource().getPlayer().getUuid());
                UUID playerUuid = context.getSource().getPlayer().getUuid();
                trainerBattles.put(playerUuid, pokemonBattle);

                String trainerName = Paths.get(nextTrainerIdentifier.getPath())
                        .getFileName().toString().replace(".json", "");

                context.getSource().getServer().sendMessage(
                        Text.translatable("command.cobblemontrainerbattle.groupbattle.startbattle.success", trainerName));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new GroupBattleCommand().getLiteral(),
                        context.getSource().getPlayer().getGameProfile().getName(), trainerName));

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

        } catch (BusyWithPokemonBattleException e) {
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
            playerValidator.assertPlayerNotBusyWithPokemonBattle();
            playerValidator.assertNotEmptyPlayerParty();

            Identifier nextTrainerIdentifier = getNextTrainerIdentifier(context.getSource().getPlayer());

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new PlayerBattleActorFactory().createWithFlatLevelFullHealth(context.getSource().getPlayer(), FLAT_LEVEL)),
                    new BattleSide(new VirtualTrainerBattleActorFactory(context.getSource().getPlayer()).createWithFlatLevelFullHealth(nextTrainerIdentifier, FLAT_LEVEL)),
                    false

            ).ifSuccessful(pokemonBattle -> {
                GroupBattleSession session = sessions.get(context.getSource().getPlayer().getUuid());
                UUID playerUuid = context.getSource().getPlayer().getUuid();
                trainerBattles.put(playerUuid, pokemonBattle);

                String trainerName = Paths.get(nextTrainerIdentifier.getPath())
                        .getFileName().toString().replace(".json", "");

                context.getSource().getPlayer().sendMessage(
                        Text.translatable("command.cobblemontrainerbattle.groupbattleflat.startbattle.success", trainerName));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new GroupBattleFlatCommand().getLiteral(),
                        context.getSource().getPlayer().getGameProfile().getName(), trainerName));

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

        } catch (BusyWithPokemonBattleException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player is busy with Pokemon battle: %s", player.getGameProfile().getName()));
            return 0;
        }
    }

    public static Identifier getNextTrainerIdentifier(ServerPlayerEntity player) throws DefeatedAllTrainersException {
        try {
            GroupBattleSession session = sessions.get(player.getUuid());
            TrainerGroup trainerGroup = CobblemonTrainerBattle.trainerGroups.get(session.trainerGroupIdentifier);
            return Identifier.of(
                    CobblemonTrainerBattle.NAMESPACE,
                    trainerGroup.trainers.get(session.defeatedTrainerCount)
            );

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
        TrainerGroup trainerGroup = CobblemonTrainerBattle.trainerGroups.get(session.trainerGroupIdentifier);
        PostBattleAction onVictory = trainerGroup.onVictory;

        try {
            CobblemonTrainerBattle.economy.addBalance(context.getSource().getPlayer(), onVictory.balance);
        } catch (NullPointerException ignored) {

        }

        try {
            onVictory.commands.forEach(command -> executeCommand(command, context.getSource().getPlayer()));
        } catch (NullPointerException ignored) {

        }
    }

    private static void onDefeatGroupBattleSession(CommandContext<ServerCommandSource> context) {
        GroupBattleSession session = sessions.get(context.getSource().getPlayer().getUuid());
        TrainerGroup trainerGroup = CobblemonTrainerBattle.trainerGroups.get(session.trainerGroupIdentifier);
        PostBattleAction onDefeat = trainerGroup.onDefeat;

        try {
            CobblemonTrainerBattle.economy.removeBalance(context.getSource().getPlayer(), onDefeat.balance);
        } catch (NullPointerException ignored) {

        }

        try {
            onDefeat.commands.forEach(command -> executeCommand(command, context.getSource().getPlayer()));
        } catch (NullPointerException ignored) {

        }
    }

    private static void executeCommand(String command, ServerPlayerEntity player) {
        command = command.replace("%player%", player.getGameProfile().getName());

        MinecraftServer server = player.getCommandSource().getServer();
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

        server.getCommandManager().execute(dispatcher.parse(command, server.getCommandSource()), command);
    }
}
