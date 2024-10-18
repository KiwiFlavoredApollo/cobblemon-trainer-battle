package kiwiapollo.cobblemontrainerbattle.battlefactory;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerIdentifierFactory;
import kiwiapollo.cobblemontrainerbattle.common.SessionValidator;
import kiwiapollo.cobblemontrainerbattle.exception.*;
import kiwiapollo.cobblemontrainerbattle.common.PlayerValidator;
import kiwiapollo.cobblemontrainerbattle.resulthandler.ResultActionHandler;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.resulthandler.ResultHandler;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.*;

public class BattleFactory {
    public static final int LEVEL = 100;
    public static final int POKEMON_COUNT = 3;

    public static Map<UUID, BattleFactorySession> sessionRegistry = new HashMap<>();

    public static int startSession(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            SessionValidator.assertSessionNotExist(sessionRegistry, player);

            List<Identifier> trainersToDefeat = new ArrayList<>();
            for (int i = 0; i < 21; i++) {
                trainersToDefeat.add(new RandomTrainerIdentifierFactory().createForBattleFactory());
            }

            ResultHandler resultHandler = new ResultActionHandler(
                    player,
                    CobblemonTrainerBattle.battleFactoryProfile.onVictory,
                    CobblemonTrainerBattle.battleFactoryProfile.onDefeat
            );

            BattleFactorySession session = new BattleFactorySession(
                    player,
                    trainersToDefeat,
                    resultHandler
            );

            session.showPartyPokemon();

            sessionRegistry.put(player.getUuid(), session);

            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.startsession.success"));
            CobblemonTrainerBattle.LOGGER.info("Started battle factory session: {}", player.getGameProfile().getName());

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_exist");
            player.sendMessage(message.formatted(Formatting.RED));

            return 0;
        }
    }

    public static int stopSession(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            SessionValidator.assertSessionExist(sessionRegistry, player);

            PlayerValidator.assertPlayerNotBusyWithPokemonBattle(player);

            BattleFactorySession session = sessionRegistry.get(player.getUuid());
            session.onSessionStop();

            sessionRegistry.remove(player.getUuid());

            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.stopsession.success"));
            CobblemonTrainerBattle.LOGGER.info("Stopped battle factory session: {}", player.getGameProfile().getName());

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
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

            BattleFactorySession session = sessionRegistry.get(player.getUuid());
            session.startBattle();

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));

            return 0;

        } catch (BattleStartException e) {
            throw new RuntimeException(e);
        }
    }

    public static int tradePokemons(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            SessionValidator.assertSessionExist(sessionRegistry, player);

            int playerSlot = IntegerArgumentType.getInteger(context, "playerslot");
            int trainerSlot = IntegerArgumentType.getInteger(context, "trainerslot");

            BattleFactorySession session = sessionRegistry.get(player.getUuid());
            session.tradePokemon(playerSlot, trainerSlot);

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));

            return 0;
        }
    }

    public static int showTradeablePokemon(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            SessionValidator.assertSessionExist(sessionRegistry, player);

            BattleFactorySession session = sessionRegistry.get(player.getUuid());
            session.showTradeablePokemon();

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));

            return 0;
        }
    }

    public static int showPartyPokemon(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            SessionValidator.assertSessionExist(sessionRegistry, player);

            BattleFactorySession session = sessionRegistry.get(player.getUuid());
            session.showPartyPokemon();

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));

            return 0;
        }
    }

    public static int rerollPokemon(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            SessionValidator.assertSessionExist(sessionRegistry, player);

            BattleFactorySession session = sessionRegistry.get(player.getUuid());
            session.rerollPokemon();

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));

            return 0;
        }
    }
}
