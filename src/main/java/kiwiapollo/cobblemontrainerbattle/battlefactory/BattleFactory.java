package kiwiapollo.cobblemontrainerbattle.battlefactory;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.PokemonStats;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.PlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.VirtualTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.commands.BattleFactoryCommand;
import kiwiapollo.cobblemontrainerbattle.common.PostBattleAction;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerIdentifierFactory;
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
import java.util.*;

public class BattleFactory {
    public static final int LEVEL = 100;
    public static final int POKEMON_COUNT = 3;
    public static Map<UUID, BattleFactorySession> sessions = new HashMap<>();
    public static Map<UUID, PokemonBattle> trainerBattles = new HashMap<>();

    public static int startSession(CommandContext<ServerCommandSource> context) {
        try {
            BattleFactorySessionValidator validator = new BattleFactorySessionValidator(context.getSource().getPlayer());
            validator.assertNotExistValidSession();

            List<Identifier> trainersToDefeat = new ArrayList<>();
            for (int i = 0; i < 21; i++) {
                trainersToDefeat.add(new RandomTrainerIdentifierFactory().createForBattleFactory());
            }

            BattleFactory.sessions.put(context.getSource().getPlayer().getUuid(), new BattleFactorySession(trainersToDefeat));

            context.getSource().getPlayer().sendMessage(
                    Text.translatable("command.cobblemontrainerbattle.battlefactory.startsession.success"));
            showPartyPokemon(context);
            CobblemonTrainerBattle.LOGGER.info(String.format("%s: Started Battle Factory session",
                    context.getSource().getPlayer().getGameProfile().getName()));

            return Command.SINGLE_SUCCESS;

        } catch (ExistValidSessionException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Valid battle session exists: %s", player.getGameProfile().getName()));
            return 0;
        }
    }

    public static int stopSession(CommandContext<ServerCommandSource> context) {
        try {
            new BattleFactorySessionValidator(context.getSource().getPlayer()).assertExistValidSession();
            new PlayerValidator(context.getSource().getPlayer()).assertPlayerNotBusyWithPokemonBattle();

            onStopBattleFactorySession(context);
            BattleFactory.sessions.remove(context.getSource().getPlayer().getUuid());

            context.getSource().getPlayer().sendMessage(
                    Text.translatable("command.cobblemontrainerbattle.battlefactory.stopsession.success"));
            CobblemonTrainerBattle.LOGGER.info(String.format("%s: Stopped Battle Factory session",
                    context.getSource().getPlayer().getGameProfile().getName()));

            return Command.SINGLE_SUCCESS;

        } catch (NotExistValidSessionException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
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

    public static int startBattle(CommandContext<ServerCommandSource> context) {
        try {
            BattleFactorySessionValidator sessionValidator = new BattleFactorySessionValidator(context.getSource().getPlayer());
            sessionValidator.assertExistValidSession();
            sessionValidator.assertNotDefeatedAllTrainers();
            sessionValidator.assertNotPlayerDefeated();

            PlayerValidator playerValidator = new PlayerValidator(context.getSource().getPlayer());
            playerValidator.assertPlayerNotBusyWithPokemonBattle();

            BattleFactorySession session = sessions.get(context.getSource().getPlayer().getUuid());
            Identifier trainerIdentifier = session.trainersToDefeat.get(session.defeatedTrainerCount);
            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new PlayerBattleActorFactory().createForBattleFactory(context.getSource().getPlayer())),
                    new BattleSide(new VirtualTrainerBattleActorFactory(context.getSource().getPlayer()).createForBattleFactory(trainerIdentifier, LEVEL)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                UUID playerUuid = context.getSource().getPlayer().getUuid();
                trainerBattles.put(playerUuid, pokemonBattle);

                String trainerName = Paths.get(trainerIdentifier.getPath())
                        .getFileName().toString().replace(".json", "");

                context.getSource().getPlayer().sendMessage(
                        Text.translatable("command.cobblemontrainerbattle.battlefactory.startbattle.success", trainerName));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new BattleFactoryCommand().getLiteral(),
                        context.getSource().getPlayer().getGameProfile().getName(), trainerName));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (NotExistValidSessionException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Valid battle session does not exists: %s", player.getGameProfile().getName()));
            return 0;

        }  catch (DefeatedAllTrainersException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.startbattle.defeated_all_trainers");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player has defeated all trainers: %s", player.getGameProfile().getName()));
            return 0;

        } catch (DefeatedToTrainerException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.startbattle.defeated_to_trainer");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player is defeated: %s", player.getGameProfile().getName()));
            return 0;

        } catch (BusyWithPokemonBattleException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player is busy with Pokemon battle: %s", player.getGameProfile().getName()));
            return 0;
        }
    }

    public static int tradePokemons(CommandContext<ServerCommandSource> context) {
        try {
            BattleFactorySessionValidator sessionValidator = new BattleFactorySessionValidator(context.getSource().getPlayer());
            sessionValidator.assertExistValidSession();
            sessionValidator.assertNotPlayerDefeated();
            sessionValidator.assertExistDefeatedTrainer();
            sessionValidator.assertNotPlayerTradedPokemon();

            int playerslot = IntegerArgumentType.getInteger(context, "playerslot");
            int trainerslot = IntegerArgumentType.getInteger(context, "trainerslot");

            BattleFactorySession session = sessions.get(context.getSource().getPlayer().getUuid());
            Pokemon trainerPokemon = session.tradeablePokemons.get(trainerslot - 1);
            Pokemon playerPokemon = session.partyPokemons.get(playerslot - 1);

            session.partyPokemons = new ArrayList<>(session.partyPokemons);
            session.partyPokemons.set(playerslot - 1, trainerPokemon.clone(true, true));
            session.tradeablePokemons = List.of();

            MutableText pokemonTradeMessage = Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.success",
                    playerPokemon.getDisplayName(), trainerPokemon.getDisplayName()).formatted(Formatting.YELLOW);

            context.getSource().getPlayer().sendMessage(pokemonTradeMessage);
            CobblemonTrainerBattle.LOGGER.info(pokemonTradeMessage.getString());

            return Command.SINGLE_SUCCESS;

        } catch (NotExistValidSessionException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Valid battle session does not exists: %s", player.getGameProfile().getName()));
            return 0;

        } catch (NotExistDefeatedTrainerException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.defeated_trainer_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player has no defeated trainers: %s", player.getGameProfile().getName()));
            return 0;

        } catch (DefeatedToTrainerException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.defeated_to_trainer");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player is defeated: %s", player.getGameProfile().getName()));
            return 0;

        } catch (TradedPokemonException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.already_traded_pokemon");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player has already traded a Pokemon: %s", player.getGameProfile().getName()));
            return 0;
        }
    }

    public static int showTradeablePokemon(CommandContext<ServerCommandSource> context) {
        try {
            BattleFactorySessionValidator sessionValidator = new BattleFactorySessionValidator(context.getSource().getPlayer());
            sessionValidator.assertExistValidSession();
            sessionValidator.assertExistDefeatedTrainer();

            BattleFactorySession session = sessions.get(context.getSource().getPlayer().getUuid());
            printPokemons(context, session.tradeablePokemons);

            return Command.SINGLE_SUCCESS;

        } catch (NotExistValidSessionException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Valid battle session does not exists: %s", player.getGameProfile().getName()));
            return 0;

        } catch (NotExistDefeatedTrainerException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.defeated_trainer_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player has no defeated trainers: %s", player.getGameProfile().getName()));
            return 0;
        }
    }

    public static int showPartyPokemon(CommandContext<ServerCommandSource> context) {
        try {
            new BattleFactorySessionValidator(context.getSource().getPlayer()).assertExistValidSession();
            printPokemons(context, sessions.get(context.getSource().getPlayer().getUuid()).partyPokemons);
            return Command.SINGLE_SUCCESS;

        } catch (NotExistValidSessionException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Valid battle session does not exists: %s", player.getGameProfile().getName()));
            return 0;
        }
    }

    public static int rerollPokemon(CommandContext<ServerCommandSource> context) {
        try {
            BattleFactorySessionValidator sessionValidator = new BattleFactorySessionValidator(context.getSource().getPlayer());
            sessionValidator.assertExistValidSession();
            sessionValidator.assertNotExistDefeatedTrainer();

            BattleFactorySession session = sessions.get(context.getSource().getPlayer().getUuid());

            session.partyPokemons = List.of(
                    PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL),
                    PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL),
                    PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL)
            );
            context.getSource().getPlayer().sendMessage(
                    Text.translatable("command.cobblemontrainerbattle.battlefactory.rerollpokemon.success"));
            showPartyPokemon(context);

            return Command.SINGLE_SUCCESS;

        } catch (NotExistValidSessionException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Valid battle session does not exists: %s", player.getGameProfile().getName()));
            return 0;

        }catch (ExistDefeatedTrainerException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.rerollpokemon.defeated_trainer_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player has defeated trainers: %s", player.getGameProfile().getName()));
            return 0;
        }
    }

    public static int showWinningStreak(CommandContext<ServerCommandSource> context) {
        try {
            new BattleFactorySessionValidator(context.getSource().getPlayer()).assertExistValidSession();

            BattleFactorySession session = sessions.get(context.getSource().getPlayer().getUuid());
            context.getSource().getPlayer().sendMessage(
                    Text.translatable("command.cobblemontrainerbattle.battlefactory.winningstreak.success",
                            session.defeatedTrainerCount));

            return Command.SINGLE_SUCCESS;

        } catch (NotExistValidSessionException e) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            MutableText message = Text.translatable("command.cobblemontrainerbattle.battlefactory.common.valid_session_not_exist");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Valid battle session does not exist: %s", player.getGameProfile().getName()));
            return 0;
        }
    }

    private static void printPokemons(CommandContext<ServerCommandSource> context, List<Pokemon> pokemons) {
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon pokemon = pokemons.get(i);

            context.getSource().getPlayer().sendMessage(
                    Text.literal("[" + (i + 1) + "] ").append(pokemon.getDisplayName()).formatted(Formatting.YELLOW));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("Ability ").append(Text.translatable(pokemon.getAbility().getDisplayName())));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("Nature ").append(Text.translatable(pokemon.getNature().getDisplayName())));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("MoveSet ").append(getPokemonMoveSetReport(pokemon.getMoveSet())));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("EVs ").append(Text.literal(getPokemonStatsReport(pokemon.getEvs()))));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("IVs ").append(Text.literal(getPokemonStatsReport(pokemon.getIvs()))));
        }
    }

    private static Text getPokemonMoveSetReport(MoveSet moveSet) {
        MutableText moveSetReport = Text.literal("");
        for (Move move : moveSet.getMoves()) {
            if (moveSetReport.equals(Text.literal(""))) {
                moveSetReport.append(move.getDisplayName());
            } else {
                moveSetReport.append(Text.literal(" / ")).append(move.getDisplayName());
            }
        }
        return moveSetReport;
    }

    private static String getPokemonStatsReport(PokemonStats stats) {
        return String.format("HP %d / ATK %d / DEF %d / SPA %d / SPD %d / SPE %d",
                stats.get(Stats.HP), stats.get(Stats.ATTACK), stats.get(Stats.DEFENCE),
                stats.get(Stats.SPECIAL_ATTACK), stats.get(Stats.SPECIAL_DEFENCE), stats.get(Stats.SPEED));
    }

    private static void onStopBattleFactorySession(CommandContext<ServerCommandSource> context) {
        if (BattleFactorySessionValidator.isDefeatedAllTrainers(context.getSource().getPlayer())) {
            onVictoryBattleFactorySession(context);
        } else {
            onDefeatBattleFactorySession(context);
        }
    }

    private static void onVictoryBattleFactorySession(CommandContext<ServerCommandSource> context) {
        PostBattleAction onVictory = CobblemonTrainerBattle.battleFactoryConfiguration.onVictory;

        try {
            CobblemonTrainerBattle.economy.addBalance(context.getSource().getPlayer(), onVictory.balance);
        } catch (NullPointerException ignored) {

        }

        try {
            onVictory.commands.forEach(command -> executeCommand(context.getSource().getPlayer(), command));
        } catch (NullPointerException ignored) {

        }
    }

    private static void onDefeatBattleFactorySession(CommandContext<ServerCommandSource> context) {
        PostBattleAction onDefeat = CobblemonTrainerBattle.battleFactoryConfiguration.onDefeat;

        try {
            CobblemonTrainerBattle.economy.removeBalance(context.getSource().getPlayer(), onDefeat.balance);
        } catch (NullPointerException ignored) {

        }

        try {
            onDefeat.commands.forEach(command -> executeCommand(context.getSource().getPlayer(), command));
        } catch (NullPointerException ignored) {

        }
    }

    private static void executeCommand(ServerPlayerEntity player, String command) {
        command = command.replace("%player%", player.getGameProfile().getName());

        MinecraftServer server = player.getCommandSource().getServer();
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

        server.getCommandManager().execute(dispatcher.parse(command, server.getCommandSource()), command);
    }
}
