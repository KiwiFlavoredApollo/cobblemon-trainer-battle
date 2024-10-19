package kiwiapollo.cobblemontrainerbattle.groupbattle;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.factory.FlatGroupBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.factory.NormalGroupBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.factory.BattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.common.*;
import kiwiapollo.cobblemontrainerbattle.exception.*;
import kiwiapollo.cobblemontrainerbattle.exception.battlecondition.RematchNotAllowedException;
import kiwiapollo.cobblemontrainerbattle.postbattle.RecordedBattleResultHandler;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.postbattle.BattleResultHandler;
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
    public static Map<UUID, GroupBattleSession> sessionRegistry = new HashMap<>();

    public static int startNormalGroupBattleSession(CommandContext<ServerCommandSource> context) {
        return startSession(context, new NormalGroupBattleParticipantFactory());
    }

    public static int startFlatGroupBattleSession(CommandContext<ServerCommandSource> context) {
        return startSession(context, new FlatGroupBattleParticipantFactory(FLAT_LEVEL));
    }

    private static int startSession(CommandContext<ServerCommandSource> context, BattleParticipantFactory battleParticipantFactory) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            Identifier identifier = new Identifier(StringArgumentType.getString(context, "group"));

            ResourceValidator.assertTrainerGroupExist(identifier);
            SessionValidator.assertSessionNotExist(sessionRegistry, player);

            TrainerGroupProfile trainerGroupProfile = CobblemonTrainerBattle.trainerGroupProfileRegistry.get(identifier);
            List<Identifier> trainersToDefeat = trainerGroupProfile.trainers.stream().map(Identifier::new).toList();

            BattleConditionValidator.assertRematchAllowedAfterVictory(player, identifier, trainerGroupProfile.condition);

            BattleResultHandler battleResultHandler = new RecordedBattleResultHandler(
                    player,
                    identifier,
                    trainerGroupProfile.onVictory,
                    trainerGroupProfile.onDefeat
            );

            GroupBattleSession session = new GroupBattleSession(
                    player,
                    trainersToDefeat,
                    trainerGroupProfile.condition,
                    battleResultHandler,
                    battleParticipantFactory
            );

            sessionRegistry.put(player.getUuid(), session);

            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.groupbattle.startsession.success"));
            CobblemonTrainerBattle.LOGGER.info("Started group battle session: {}", player.getGameProfile().getName());

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.groupbattle.common.valid_session_exist");
            player.sendMessage(message.formatted(Formatting.RED));

            return 0;

        } catch (FileNotFoundException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.resource.group_not_found");
            player.sendMessage(message.formatted(Formatting.RED));

            return 0;

        } catch (RematchNotAllowedException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.condition.is_rematch_allowed_after_victory.groupbattle");
            player.sendMessage(message.formatted(Formatting.RED));

            return 0;
        }
    }

    public static int stopSession(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            SessionValidator.assertSessionExist(sessionRegistry, player);
            PlayerValidator.assertPlayerNotBusyWithPokemonBattle(player);

            GroupBattleSession session = sessionRegistry.get(player.getUuid());
            session.onSessionStop();

            sessionRegistry.remove(player.getUuid());

            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.groupbattle.stopsession.success"));
            CobblemonTrainerBattle.LOGGER.info("Stopped group battle session: {}", player.getGameProfile().getName());

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.groupbattle.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));

            return 0;

        } catch (BusyWithPokemonBattleException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.trainerbattle.busy_with_pokemon_battle");
            player.sendMessage(message.formatted(Formatting.RED));

            return 0;
        }
    }

    public static int startBattle(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            SessionValidator.assertSessionExist(sessionRegistry, player);

            GroupBattleSession session = sessionRegistry.get(player.getUuid());
            session.startBattle();

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.groupbattle.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));

            return 0;

        } catch (BattleStartException e) {
            return 0;
        }
    }
}
