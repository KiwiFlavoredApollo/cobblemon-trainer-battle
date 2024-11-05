package kiwiapollo.cobblemontrainerbattle.groupbattle;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.factory.BattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.factory.GroupBattleParticipantFactoryBuilder;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.factory.FlatGroupBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.factory.NormalGroupBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.common.*;
import kiwiapollo.cobblemontrainerbattle.exception.*;
import kiwiapollo.cobblemontrainerbattle.exception.battlecondition.RematchNotAllowedException;
import kiwiapollo.cobblemontrainerbattle.parser.TrainerGroupProfileStorage;
import kiwiapollo.cobblemontrainerbattle.postbattle.RecordedBattleResultHandler;
import kiwiapollo.cobblemontrainerbattle.postbattle.PostBattleActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.postbattle.BatchedBattleResultHandler;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.postbattle.BattleResultHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.io.FileNotFoundException;
import java.util.*;

public class GroupBattle {
    public static final int FLAT_LEVEL = 100;
    private static Map<UUID, GroupBattleSession> sessions = new HashMap<>();

    public static int startNormalGroupBattleSession(CommandContext<ServerCommandSource> context) {
        return startSession(context, new NormalGroupBattleParticipantFactory.Builder());
    }

    public static int startFlatGroupBattleSession(CommandContext<ServerCommandSource> context) {
        return startSession(context, new FlatGroupBattleParticipantFactory.Builder(FLAT_LEVEL));
    }

    private static int startSession(
            CommandContext<ServerCommandSource> context,
            GroupBattleParticipantFactoryBuilder battleParticipantFactoryBuilder
    ) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();
            Identifier group = new Identifier(StringArgumentType.getString(context, "group"));

            ResourceValidator.assertTrainerGroupExist(group);
            SessionValidator.assertSessionNotExist(sessions, player);

            TrainerGroupProfile profile = TrainerGroupProfileStorage.get(group);
            List<Identifier> trainer = profile.trainers.stream().map(Identifier::new).toList();

            BattleConditionValidator.assertRematchAllowedAfterVictory(player, group, profile.condition);

            BattleResultHandler battleResultHandler = new BatchedBattleResultHandler(
                    new RecordedBattleResultHandler(player, group),
                    new PostBattleActionSetHandler(player, profile.onVictory, profile.onDefeat)
            );

            BattleParticipantFactory battleParticipantFactory = battleParticipantFactoryBuilder.addCondition(profile.condition).build();

            GroupBattleSession session = new GroupBattleSession(
                    player,
                    trainer,
                    battleResultHandler,
                    battleParticipantFactory
            );

            sessions.put(player.getUuid(), session);

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

            SessionValidator.assertSessionExist(sessions, player);
            PlayerValidator.assertPlayerNotBusyWithPokemonBattle(player);

            GroupBattleSession session = sessions.get(player.getUuid());
            session.onSessionStop();

            sessions.remove(player.getUuid());

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

            SessionValidator.assertSessionExist(sessions, player);

            GroupBattleSession session = sessions.get(player.getUuid());
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

    public static void removeDisconnectedPlayerSession(ServerPlayNetworkHandler handler, MinecraftServer server) {
        ServerPlayerEntity player = handler.getPlayer();

        if (!GroupBattle.sessions.containsKey(player.getUuid())) {
            return;
        }

        GroupBattle.sessions.get(player.getUuid()).onSessionStop();
        GroupBattle.sessions.remove(player.getUuid());
    }
}
