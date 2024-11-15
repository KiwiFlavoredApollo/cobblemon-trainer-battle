package kiwiapollo.cobblemontrainerbattle.battlefactory;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.exception.SessionOperationException;
import kiwiapollo.cobblemontrainerbattle.predicates.MessagePredicate;
import kiwiapollo.cobblemontrainerbattle.predicates.PlayerNotBusyPredicate;
import kiwiapollo.cobblemontrainerbattle.predicates.SessionExistPredicate;
import kiwiapollo.cobblemontrainerbattle.predicates.SessionNotExistPredicate;
import kiwiapollo.cobblemontrainerbattle.session.BattleFactorySessionStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class BattleFactory {
    private static final int LEVEL = 100;
    private static final int POKEMON_COUNT = 3;
    private static final int ROUND_COUNT = 7;

    public static int startSession(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        List<MessagePredicate<ServerPlayerEntity>> predicates = List.of(
                new SessionNotExistPredicate(BattleFactorySessionStorage.getSessionRegistry()),
                new PlayerNotBusyPredicate<>()
        );

        for (MessagePredicate<ServerPlayerEntity> predicate: predicates) {
            if (!predicate.test(player)) {
                player.sendMessage(predicate.getErrorMessage().formatted(Formatting.RED));
                return 0;
            }
        }
        BattleFactorySession session = new BattleFactorySession(player);

        session.showPartyPokemon();

        BattleFactorySessionStorage.getSessionRegistry().put(player.getUuid(), session);

        player.sendMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.startsession.success"));
        CobblemonTrainerBattle.LOGGER.info("Started battle factory session: {}", player.getGameProfile().getName());

        return Command.SINGLE_SUCCESS;
    }

    public static int stopSession(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        List<MessagePredicate<ServerPlayerEntity>> predicates = List.of(
                new SessionExistPredicate(BattleFactorySessionStorage.getSessionRegistry()),
                new PlayerNotBusyPredicate<>()
        );

        for (MessagePredicate<ServerPlayerEntity> predicate: predicates) {
            if (!predicate.test(player)) {
                player.sendMessage(predicate.getErrorMessage().formatted(Formatting.RED));
                return 0;
            }
        }

        BattleFactorySession session = BattleFactorySessionStorage.getSessionRegistry().get(player.getUuid());
        session.onSessionStop();

        BattleFactorySessionStorage.getSessionRegistry().remove(player.getUuid());

        player.sendMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.stopsession.success"));
        CobblemonTrainerBattle.LOGGER.info("Stopped battle factory session: {}", player.getGameProfile().getName());

        return Command.SINGLE_SUCCESS;
    }

    public static int startBattle(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            List<MessagePredicate<ServerPlayerEntity>> predicates = List.of(
                    new SessionExistPredicate(BattleFactorySessionStorage.getSessionRegistry()),
                    new PlayerNotBusyPredicate<>()
            );

            for (MessagePredicate<ServerPlayerEntity> predicate: predicates) {
                if (!predicate.test(player)) {
                    player.sendMessage(predicate.getErrorMessage().formatted(Formatting.RED));
                    return 0;
                }
            }

            BattleFactorySession session = BattleFactorySessionStorage.getSessionRegistry().get(player.getUuid());
            session.startBattle();

            return Command.SINGLE_SUCCESS;

        } catch (BattleStartException e) {
            return 0;
        }
    }

    public static int tradePokemons(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            List<MessagePredicate<ServerPlayerEntity>> predicates = List.of(
                    new SessionExistPredicate(BattleFactorySessionStorage.getSessionRegistry()),
                    new PlayerNotBusyPredicate<>()
            );

            for (MessagePredicate<ServerPlayerEntity> predicate: predicates) {
                if (!predicate.test(player)) {
                    player.sendMessage(predicate.getErrorMessage().formatted(Formatting.RED));
                    return 0;
                }
            }

            int playerSlot = IntegerArgumentType.getInteger(context, "playerslot");
            int trainerSlot = IntegerArgumentType.getInteger(context, "trainerslot");

            BattleFactorySession session = BattleFactorySessionStorage.getSessionRegistry().get(player.getUuid());
            session.tradePokemon(playerSlot, trainerSlot);

            return Command.SINGLE_SUCCESS;

        } catch (SessionOperationException e) {
            return 0;
        }
    }

    public static int showTradeablePokemon(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            List<MessagePredicate<ServerPlayerEntity>> predicates = List.of(
                    new SessionExistPredicate(BattleFactorySessionStorage.getSessionRegistry()),
                    new PlayerNotBusyPredicate<>()
            );

            for (MessagePredicate<ServerPlayerEntity> predicate: predicates) {
                if (!predicate.test(player)) {
                    player.sendMessage(predicate.getErrorMessage().formatted(Formatting.RED));
                    return 0;
                }
            }

            BattleFactorySession session = BattleFactorySessionStorage.getSessionRegistry().get(player.getUuid());
            session.showTradeablePokemon();

            return Command.SINGLE_SUCCESS;

        } catch (SessionOperationException e) {
            return 0;
        }
    }

    public static int showPartyPokemon(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        List<MessagePredicate<ServerPlayerEntity>> predicates = List.of(
                new SessionExistPredicate(BattleFactorySessionStorage.getSessionRegistry())
        );

        for (MessagePredicate<ServerPlayerEntity> predicate: predicates) {
            if (!predicate.test(player)) {
                player.sendMessage(predicate.getErrorMessage().formatted(Formatting.RED));
                return 0;
            }
        }

        BattleFactorySession session = BattleFactorySessionStorage.getSessionRegistry().get(player.getUuid());
        session.showPartyPokemon();

        return Command.SINGLE_SUCCESS;
    }

    public static int rerollPartyPokemon(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            List<MessagePredicate<ServerPlayerEntity>> predicates = List.of(
                    new SessionExistPredicate(BattleFactorySessionStorage.getSessionRegistry()),
                    new PlayerNotBusyPredicate<>()
            );

            for (MessagePredicate<ServerPlayerEntity> predicate: predicates) {
                if (!predicate.test(player)) {
                    player.sendMessage(predicate.getErrorMessage().formatted(Formatting.RED));
                    return 0;
                }
            }

            BattleFactorySession session = BattleFactorySessionStorage.getSessionRegistry().get(player.getUuid());
            session.rerollPartyPokemon();

            return Command.SINGLE_SUCCESS;

        } catch (SessionOperationException e) {
            return 0;
        }
    }

    public static int showWinningStreak(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        List<MessagePredicate<ServerPlayerEntity>> predicates = List.of(
                new SessionExistPredicate(BattleFactorySessionStorage.getSessionRegistry())
        );

        for (MessagePredicate<ServerPlayerEntity> predicate: predicates) {
            if (!predicate.test(player)) {
                player.sendMessage(predicate.getErrorMessage().formatted(Formatting.RED));
                return 0;
            }
        }

        BattleFactorySession session = BattleFactorySessionStorage.getSessionRegistry().get(player.getUuid());

        player.sendMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.winningstreak.success", session.getDefeatedTrainersCount()));

        return Command.SINGLE_SUCCESS;
    }
}
