package kiwiapollo.cobblemontrainerbattle.groupbattle;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.predicates.*;
import kiwiapollo.cobblemontrainerbattle.session.SessionStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public class GroupBattle {
    private static final SessionStorage<GroupBattleSession> sessions = new SessionStorage<>();

    public static int startSession(CommandContext<ServerCommandSource> context, GroupBattleSessionFactory factory) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        Identifier group = new Identifier(StringArgumentType.getString(context, "group"));

        MessagePredicate<Identifier> isProfileExist = new TrainerGroupProfileExistPredicate();
        if (!isProfileExist.test(group)) {
            player.sendMessage(isProfileExist.getMessage().formatted(Formatting.RED));
            return 0;
        }

        List<MessagePredicate<ServerPlayerEntity>> predicates = List.of(
                new SessionNotExistPredicate(GroupBattle.getSessionStorage()),
                new PlayerNotBusyPredicate<>()
        );

        for (MessagePredicate<ServerPlayerEntity> predicate: predicates) {
            if (!predicate.test(player)) {
                player.sendMessage(predicate.getMessage().formatted(Formatting.RED));
                return 0;
            }
        }

        GroupBattleSession session = factory.create(player, group);

        GroupBattleSessionStorage.put(player.getUuid(), session);

        player.sendMessage(Text.translatable("command.cobblemontrainerbattle.groupbattle.startsession.success"));
        CobblemonTrainerBattle.LOGGER.info("Started group battle session: {}", player.getGameProfile().getName());

        return Command.SINGLE_SUCCESS;
    }

    public static int stopSession(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        List<MessagePredicate<ServerPlayerEntity>> predicates = List.of(
                new SessionExistPredicate(GroupBattle.getSessionStorage()),
                new PlayerNotBusyPredicate<>()
        );

        for (MessagePredicate<ServerPlayerEntity> predicate: predicates) {
            if (!predicate.test(player)) {
                player.sendMessage(predicate.getMessage().formatted(Formatting.RED));
                return 0;
            }
        }

        GroupBattleSession session = GroupBattle.getSessionStorage().get(player.getUuid());
        session.onSessionStop();

        GroupBattle.getSessionStorage().remove(player.getUuid());

        player.sendMessage(Text.translatable("command.cobblemontrainerbattle.groupbattle.stopsession.success"));
        CobblemonTrainerBattle.LOGGER.info("Stopped group battle session: {}", player.getGameProfile().getName());

        return Command.SINGLE_SUCCESS;
    }

    public static int startBattle(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            List<MessagePredicate<ServerPlayerEntity>> predicates = List.of(
                    new SessionExistPredicate(GroupBattle.getSessionStorage()),
                    new PlayerNotBusyPredicate<>()
            );

            for (MessagePredicate<ServerPlayerEntity> predicate: predicates) {
                if (!predicate.test(player)) {
                    player.sendMessage(predicate.getMessage().formatted(Formatting.RED));
                    return 0;
                }
            }

            GroupBattleSession session = GroupBattleSessionStorage.get(player.getUuid());
            session.startBattle();

            return Command.SINGLE_SUCCESS;

        } catch (BattleStartException e) {
            return 0;
        }
    }

    public static SessionStorage<GroupBattleSession> getSessionStorage() {
        return sessions;
    }
}
