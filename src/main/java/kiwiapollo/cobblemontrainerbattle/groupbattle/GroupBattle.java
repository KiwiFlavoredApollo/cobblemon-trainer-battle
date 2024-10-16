package kiwiapollo.cobblemontrainerbattle.groupbattle;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import kiwiapollo.cobblemontrainerbattle.common.*;
import kiwiapollo.cobblemontrainerbattle.exceptions.*;
import kiwiapollo.cobblemontrainerbattle.temp.GroupBattleSession;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.BattleResultHandler;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.ResultHandler;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.io.FileNotFoundException;
import java.util.*;

public class GroupBattle {
    public static final int FLAT_LEVEL = 100;
    public static Map<UUID, GroupBattleSession> sessions = new HashMap<>();

    public static int startSession(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();
            String groupResourcePath = StringArgumentType.getString(context, "group");

            ResourceValidator resourceValidator = new ResourceValidator();
            resourceValidator.assertExistResource(groupResourcePath);
            resourceValidator.assertValidGroupResource(groupResourcePath);

            if (sessions.containsKey(player.getUuid())) {
                throw new IllegalStateException();
            }

            Identifier identifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, groupResourcePath);

            List<Identifier> trainersToDefeat = new ArrayList<>();


            ResultHandler resultHandler = new BattleResultHandler(
                    player,
                    CobblemonTrainerBattle.battleFactoryConfiguration.onVictory,
                    CobblemonTrainerBattle.battleFactoryConfiguration.onDefeat
            );

            GroupBattleSession session = new GroupBattleSession(
                    player,
                    trainersToDefeat,
                    resultHandler
            );

            sessions.put(player.getUuid(), session);

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.groupbattle.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Valid battle session exists: %s", player.getGameProfile().getName()));
            return 0;

        } catch (FileNotFoundException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            String groupResourcePath = StringArgumentType.getString(context, "group");
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.resource.not_found", groupResourcePath);
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("An error occurred while reading resource: %s", groupResourcePath));
            return 0;

        } catch (IllegalArgumentException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            String groupResourcePath = StringArgumentType.getString(context, "group");
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.resource.cannot_be_read", groupResourcePath);
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("An error occurred while reading resource: %s", groupResourcePath));
            return 0;
        }
    }

    public static int stopSession(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            if (!sessions.containsKey(player.getUuid())) {
                throw new IllegalStateException();
            }

            PlayerValidator.assertPlayerNotBusyWithPokemonBattle(player);

            GroupBattleSession session = sessions.get(player.getUuid());
            session.onSessionStop();

            BattleFactory.sessions.remove(player.getUuid());

            context.getSource().getPlayer().sendMessage(Text.literal("command.cobblemontrainerbattle.groupbattle.stopsession.success"));
            CobblemonTrainerBattle.LOGGER.info("Stopped battle group session: {}", context.getSource().getPlayer().getGameProfile().getName());

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error("Valid battle session does not exist: {}", player.getGameProfile().getName());
            return 0;

        } catch (BusyWithPokemonBattleException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error("Player is busy with Pokemon battle: {}", player.getGameProfile().getName());
            return 0;
        }
    }

    public static int startNormalBattle(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            if (sessions.containsKey(player.getUuid())) {
                throw new IllegalStateException();
            }

            GroupBattleSession session = sessions.get(player.getUuid());
            session.startBattle();

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.groupbattle.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Valid battle session does not exists: %s", player.getGameProfile().getName()));
            return 0;

        } catch (BattleStartException e) {
            throw new RuntimeException(e);
        }
    }

    public static int startFlatBattle(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            if (sessions.containsKey(player.getUuid())) {
                throw new IllegalStateException();
            }

            GroupBattleSession session = sessions.get(player.getUuid());
            session.startBattle();

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.groupbattle.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Valid battle session does not exists: %s", player.getGameProfile().getName()));
            return 0;

        } catch (BattleStartException e) {
            throw new RuntimeException(e);
        }
    }
}
