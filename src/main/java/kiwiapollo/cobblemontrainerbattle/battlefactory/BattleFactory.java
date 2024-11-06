package kiwiapollo.cobblemontrainerbattle.battlefactory;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.factory.BattleFactoryParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.factory.BattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.common.IdentifierFactory;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.exception.*;
import kiwiapollo.cobblemontrainerbattle.common.PlayerValidator;
import kiwiapollo.cobblemontrainerbattle.parser.profile.MiniGameProfileStorage;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerProfileStorage;
import kiwiapollo.cobblemontrainerbattle.postbattle.PostBattleActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.postbattle.BattleResultHandler;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.*;

public class BattleFactory {
    private static final int LEVEL = 100;
    private static final int POKEMON_COUNT = 3;
    private static final int ROUND_COUNT = 7;

    public static int startSession(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            BattleFactorySessionStorage.assertSessionNotExist(player);
            IdentifierFactory trainerFactory = new RandomTrainerFactory(BattleFactory::hasMinimumPokemon);

            List<Identifier> trainersToDefeat = new ArrayList<>();
            for (int i = 0; i < ROUND_COUNT; i++) {
                trainersToDefeat.add(trainerFactory.create());
            }

            BattleResultHandler battleResultHandler = new PostBattleActionSetHandler(
                    player,
                    MiniGameProfileStorage.getBattleFactoryProfile().onVictory,
                    MiniGameProfileStorage.getBattleFactoryProfile().onDefeat
            );

            BattleParticipantFactory battleParticipantFactory = new BattleFactoryParticipantFactory(BattleFactory.LEVEL);

            BattleFactorySession session = new BattleFactorySession(
                    player,
                    trainersToDefeat,
                    battleResultHandler,
                    battleParticipantFactory
            );

            session.showPartyPokemon();

            BattleFactorySessionStorage.put(player.getUuid(), session);

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

            BattleFactorySessionStorage.assertSessionExist(player);

            PlayerValidator.assertPlayerNotBusyWithPokemonBattle(player);

            BattleFactorySession session = BattleFactorySessionStorage.get(player.getUuid());
            session.onSessionStop();

            BattleFactorySessionStorage.remove(player.getUuid());

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

            BattleFactorySessionStorage.assertSessionExist(player);

            BattleFactorySession session = BattleFactorySessionStorage.get(player.getUuid());
            session.startBattle();

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));

            return 0;

        } catch (BattleStartException e) {
            return 0;
        }
    }

    public static int tradePokemons(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            BattleFactorySessionStorage.assertSessionExist(player);

            int playerSlot = IntegerArgumentType.getInteger(context, "playerslot");
            int trainerSlot = IntegerArgumentType.getInteger(context, "trainerslot");

            BattleFactorySession session = BattleFactorySessionStorage.get(player.getUuid());
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

            BattleFactorySessionStorage.assertSessionExist(player);

            BattleFactorySession session = BattleFactorySessionStorage.get(player.getUuid());
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

            BattleFactorySessionStorage.assertSessionExist(player);

            BattleFactorySession session = BattleFactorySessionStorage.get(player.getUuid());
            session.showPartyPokemon();

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));

            return 0;
        }
    }

    public static int rerollPartyPokemon(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            BattleFactorySessionStorage.assertSessionExist(player);

            BattleFactorySession session = BattleFactorySessionStorage.get(player.getUuid());
            session.rerollPartyPokemon();

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));

            return 0;
        }
    }

    public static int showWinningStreak(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            BattleFactorySessionStorage.assertSessionExist(player);

            BattleFactorySession session = BattleFactorySessionStorage.get(player.getUuid());

            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.winningstreak.success", session.getDefeatedTrainersCount()));

            return Command.SINGLE_SUCCESS;

        } catch (IllegalStateException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();

            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));

            return 0;
        }
    }

    private static boolean hasMinimumPokemon(Identifier trainer) {
        return TrainerProfileStorage.get(trainer).team().size() > BattleFactory.POKEMON_COUNT;
    }
}
